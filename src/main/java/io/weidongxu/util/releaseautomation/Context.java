package io.weidongxu.util.releaseautomation;

import lombok.Data;

@Data
public class Context {
    private final String previousVersion;
    private final String releaseVersion;
}
