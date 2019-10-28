package io.weidongxu.util.releaseautomation;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.text.MessageFormat;

public class Main {

    private enum ReleaseType {
        JAVA,
        DOT_NET
    }

    public static void main(String args[]) {
        try {
            Configure configure = null;
            Yaml yaml = new Yaml();
            try (InputStream in = Main.class.getResourceAsStream("/configure.yml")) {
                configure = yaml.loadAs(in, Configure.class);
            }
            ReleaseType releaseType = ReleaseType.valueOf(configure.getReleaseType());

            Context context = new Context(configure.getPreviousVersion(), configure.getReleaseVersion());

            Release release = (releaseType == ReleaseType.JAVA) ?
                    new JavaRelease(context, configure.getProjectRootJava()) :
                    new DotNetRelease(context, configure.getProjectRootDotNet());
            release.processReadme(configure.getReadmeFile());
            release.processPrepareNote(MessageFormat.format(configure.getReleaseNoteFileTemplate(), context.getReleaseVersion()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
