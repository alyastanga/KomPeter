/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.io;

import com.github.ragudos.kompeter.utilities.constants.Directories;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public final class FileUtils {
    private static final Logger LOGGER = KompeterLogger.getLogger(FileUtils.class);

    public static final void setupConfig() {
        createDirectoryIfNotExists(Directories.IMAGES_DIRECTORY);
        createDirectoryIfNotExists(Directories.LOGS_DIRECTORY);
        createDirectoryIfNotExists(Directories.CONFIG_DIRECTORY);
    }

    public static final void createDirectoryIfNotExists(@NotNull final String directoryPath) {
        var directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                LOGGER.severe("Failed to create directory: " + directoryPath);
            }
        }
    }

    public static void createFileIfNotExists(@NotNull final String filePath) {
        var file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create file in " + filePath, e);
            }
        }
    }
}
