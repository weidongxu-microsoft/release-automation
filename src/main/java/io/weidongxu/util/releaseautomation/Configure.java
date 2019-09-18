package io.weidongxu.util.releaseautomation;

import lombok.Data;

@Data
public class Configure {
    private String projectRootJava;
    private String projectRootDotNet;
    private String readmeFile;
    private String releaseNoteFileTemplate;

    private String previousVersion;
    private String releaseVersion;

    private String releaseType;
}
