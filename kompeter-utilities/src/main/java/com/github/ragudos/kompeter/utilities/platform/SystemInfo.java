/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.platform;

import java.util.Locale;

public class SystemInfo {
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION =
            System.getProperty("os.version").toLowerCase(Locale.ENGLISH);
    public static final String JAVA_HOME = System.getProperty("java.home");
    public static final String JAVA_VENDOR = System.getProperty("java.vendor");
    public static final String JAVA_VERSION = System.getProperty("java.version").trim();

    public static final boolean isWindows;
    public static final boolean isOS2;
    public static final boolean isMac;
    public static final boolean isLinux;
    public static final boolean isUnix;

    public static final boolean isFileSystemCaseSensitive;

    static {
        var _OS_NAME = OS_NAME.toLowerCase(Locale.ENGLISH);

        isWindows = _OS_NAME.startsWith("windows");
        isOS2 = _OS_NAME.startsWith("os/2") || _OS_NAME.startsWith("os2");
        isMac = _OS_NAME.startsWith("mac");
        isLinux = _OS_NAME.startsWith("linux");
        isUnix = !isWindows && !isOS2;
        isFileSystemCaseSensitive = isUnix && !isMac;
    }

    private SystemInfo() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
