package io.weidongxu.util.releaseautomation;

import java.io.*;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.weidongxu.util.releaseautomation.Version.*;

public class JavaRelease implements Release {

    private final String projectRoot;

    public JavaRelease(String projectRoot) {
        this.projectRoot = projectRoot;
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
        Util.processFile(readmeFile, (lines, line) -> {
            if (line.startsWith("This README is based on the released stable version") ||
                    line.startsWith(":triangular_flag_on_post:") ||
                    line.startsWith("If you are using released builds from")) {
                line = line.replaceAll(PREV_VERSION, RELEASE_VERSION);
            } else if (line.startsWith("    <version>")) {
                line = line.replaceAll(PREV_VERSION, RELEASE_VERSION);
                line = line.replaceAll(snapShotVersion(PREV_VERSION), snapShotVersion(RELEASE_VERSION));
            } else if (line.startsWith("If you are migrating your code")) {
                line = line.replaceAll(PREV_VERSION, RELEASE_VERSION);

                Pattern pattern = Pattern.compile(".* from (?<version>\\d+\\.\\d+\\.\\d+) to .*");
                Matcher matcher = pattern.matcher(line);
                matcher.find();
                String prev_prev_version = matcher.group("version");
                line = line.replaceAll(prev_prev_version, PREV_VERSION);
            } else if (line.startsWith("| " + PREV_VERSION + "    ")) {
                String newLine = line.replace(PREV_VERSION, RELEASE_VERSION).replace(majorVersion(PREV_VERSION), majorVersion(RELEASE_VERSION));
                lines.add(newLine);
            }
            lines.add(line);
            return lines;
        });
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
                .replace("{new_version}", RELEASE_VERSION)
                .replace("{prev_version_major}", majorVersion(PREV_VERSION))
                .replace("{new_version_major}", majorVersion(RELEASE_VERSION));

        Util.writeToFile(prepareNoteFile, text);
    }
}
