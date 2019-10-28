package io.weidongxu.util.releaseautomation;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.weidongxu.util.releaseautomation.UtilVersion.*;

public class JavaRelease implements Release {

    private final String projectRoot;
    private final Context context;

    public JavaRelease(Context context, String projectRoot) {
        this.projectRoot = projectRoot;
        this.context = context;
    }

    @Override
    public void processReadme(String readmeFilename) throws IOException {
        this.processReadme(Paths.get(this.projectRoot, readmeFilename).toFile());
    }

    @Override
    public void processPrepareNote(String prepareNoteFilename) throws IOException {
        this.processPrepareNote(Paths.get(this.projectRoot, prepareNoteFilename).toFile());
    }

    @Override
    public void processReadme(File readmeFile) throws IOException {
        UtilFile.processFile(readmeFile, (lines, line) -> DotNetRelease.processReadmeLine(context, lines, line));
    }

    static List<String> processReadmeLine(Context context, List<String> lines, String line) {
        final String previousVersion = context.getPreviousVersion();
        final String releaseVersion = context.getReleaseVersion();

        if (line.startsWith("This README is based on the released stable version") ||
                line.startsWith(":triangular_flag_on_post:") ||
                line.startsWith("If you are using released builds from")) {
            line = line.replace(previousVersion, releaseVersion);
        } else if (line.startsWith("    <version>")) {
            line = line.replace(previousVersion, releaseVersion);
            line = line.replace(snapShotVersion(previousVersion), snapShotVersion(releaseVersion));
        } else if (line.startsWith("If you are migrating your code")) {
            line = line.replace(previousVersion, releaseVersion);

            Pattern pattern = Pattern.compile(".* from (?<version>\\d+\\.\\d+\\.\\d+) to .*");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            String prev_prev_version = matcher.group("version");
            line = line.replace(prev_prev_version, previousVersion);
        } else if (line.startsWith("| " + previousVersion + "    ")) {
            String newLine = line.replace(previousVersion, releaseVersion);
            lines.add(newLine);
        }
        lines.add(line);
        return lines;
    }

    private static String PREPARE_NOTE_TEMPLATE =
            "# Prepare for Azure Management Libraries for Java {new_version} #\n" +
            "\n" +
            "Steps to migrate code that uses Azure Management Libraries for Java from {prev_version_major} to {new_version_major} ...\n" +
            "\n" +
            "> If this note missed any breaking changes, please open a pull request.\n" +
            "\n" +
            "V{new_version_major} is backwards compatible with V{prev_version_major} in the APIs intended for public use that reached the general availability (stable) stage in V1.x." +
            "\n";

    @Override
    public void processPrepareNote(File prepareNoteFile) throws IOException {
        String text = PREPARE_NOTE_TEMPLATE
                .replace("{new_version}", context.getReleaseVersion())
                .replace("{prev_version_major}", majorVersion(context.getPreviousVersion()))
                .replace("{new_version_major}", majorVersion(context.getReleaseVersion()));

        UtilFile.writeToFile(prepareNoteFile, text);
    }
}
