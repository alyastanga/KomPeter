/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.configurations;

import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.logging.Logger;

public abstract class AbstractConfig {
    protected static final Logger LOGGER = KompeterLogger.getLogger(AbstractConfig.class);

    public abstract String getFileDirectory();

    public abstract String getFileExtension();

    public String getFileName() {
        return getClass().getSimpleName();
    }

    public String getFullPath() {
        return getFileDirectory() + getFileName() + getFileExtension();
    }
}
