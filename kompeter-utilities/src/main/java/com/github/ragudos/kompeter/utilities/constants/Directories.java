/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.constants;

import com.github.ragudos.kompeter.utilities.platform.SystemInfo;
import java.io.File;

public final class Directories {
    public static final String LOGS_DIRECTORY;
    public static final String CONFIG_DIRECTORY;
    public static final String SQLITE_DIRECTORY;

    public static final String APP_DATA_DIRECTORY;

    /**
     * The directory where the application is installed. This is the directory where the application
     * stores its data.
     */
    public static final String APP_DIRECTORY;

    static {
        if (SystemInfo.isWindows) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "AppData";
            APP_DIRECTORY = APP_DATA_DIRECTORY + File.separator + "Local";
            CONFIG_DIRECTORY =
                    APP_DIRECTORY + File.separator + Metadata.APP_TITLE + File.separator + "config";
            LOGS_DIRECTORY =
                    APP_DIRECTORY + File.separator + Metadata.APP_TITLE + File.separator + "logs";
        } else if (SystemInfo.isLinux) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + ".local";
            APP_DIRECTORY =
                    APP_DATA_DIRECTORY + File.separator + "share" + File.separator + Metadata.APP_TITLE;
            CONFIG_DIRECTORY =
                    APP_DATA_DIRECTORY
                            + File.separator
                            + "state"
                            + File.separator
                            + Metadata.APP_TITLE
                            + File.separator
                            + "config";
            LOGS_DIRECTORY =
                    APP_DATA_DIRECTORY
                            + File.separator
                            + "state"
                            + File.separator
                            + Metadata.APP_TITLE
                            + File.separator
                            + "logs";
        } else if (SystemInfo.isMac) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "Library";
            APP_DIRECTORY =
                    APP_DATA_DIRECTORY
                            + File.separator
                            + "Application Support"
                            + File.separator
                            + Metadata.APP_TITLE;
            ;
            CONFIG_DIRECTORY =
                    APP_DATA_DIRECTORY
                            + File.separator
                            + "Preferences"
                            + File.separator
                            + Metadata.APP_TITLE
                            + File.separator
                            + "config";
            LOGS_DIRECTORY =
                    APP_DATA_DIRECTORY
                            + File.separator
                            + "Preferences"
                            + File.separator
                            + Metadata.APP_TITLE
                            + File.separator
                            + "logs";
        } else {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "etc";
            APP_DIRECTORY = APP_DATA_DIRECTORY + File.separator + Metadata.APP_TITLE;
            CONFIG_DIRECTORY = APP_DIRECTORY + File.separator + "config";
            LOGS_DIRECTORY = APP_DIRECTORY + File.separator + "logs";
        }

        SQLITE_DIRECTORY = APP_DIRECTORY + File.separator + "sqlite";
    }

    private Directories() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
