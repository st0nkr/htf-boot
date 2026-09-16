package com.teto;


import org.apache.commons.lang.SystemUtils;

public interface ISystem {
    default String getOperatingSystemSystemUtils() {
        return SystemUtils.OS_NAME;
    }

    default boolean isWindows() {
        return getOperatingSystemSystemUtils().toLowerCase().contains("windows");
    }

    default String pwd() {
        String dir = System.getProperty("user.dir");
        return dir;
    }
}
