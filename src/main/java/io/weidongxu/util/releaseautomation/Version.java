package io.weidongxu.util.releaseautomation;

public class Version {

    public static String PREV_VERSION;
    public static String RELEASE_VERSION;

    public static String snapShotVersion(String version) {
        int dotAt;
        for (dotAt = version.length() - 1; dotAt >= 0; --dotAt) {
            if (version.charAt(dotAt) == '.')
                break;
        }
        if (dotAt < 0) return version;  // abort
        return version.substring(0, dotAt + 1) + (Integer.parseInt(version.substring(dotAt + 1)) + 1) + "-SNAPSHOT";
    }

    public static String majorVersion(String version) {
        return version.endsWith(".0") ? version.substring(0, version.length() - 2) : version;
    }
}
