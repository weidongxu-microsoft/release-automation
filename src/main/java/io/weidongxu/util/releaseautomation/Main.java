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

            Version.PREV_VERSION = configure.getPreviousVersion();
            Version.RELEASE_VERSION = configure.getReleaseVersion();

            ReleaseType releaseType = ReleaseType.valueOf(configure.getReleaseType());

            Release release = releaseType == ReleaseType.JAVA ? new JavaRelease(configure.getProjectRootJava()) : new DotNetRelease(configure.getProjectRootDotNet());
            release.processReadme(configure.getReadmeFile());
            release.processPrepareNote(MessageFormat.format(configure.getReleaseNoteFileTemplate(), Version.RELEASE_VERSION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
