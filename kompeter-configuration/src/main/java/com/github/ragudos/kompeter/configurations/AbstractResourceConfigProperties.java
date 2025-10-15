/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.configurations;

import com.github.ragudos.kompeter.utilities.io.FileLoadLeniency;
import com.github.ragudos.kompeter.utilities.io.PropertiesIO;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public abstract class AbstractResourceConfigProperties extends AbstractResourceConfig {
    protected Properties properties;

    public AbstractResourceConfigProperties() {
        super();

        var classNameString = getResourceClass().getName();

        LOGGER.config(
                "Initializing configuration in resource for path: "
                        + getFileDirectory()
                        + getFileName()
                        + " for class: "
                        + classNameString);

        try {
            properties =
                    PropertiesIO.loadProperties(
                            getResourceClass(), getFileName(), getFileDirectory(), FileLoadLeniency.MANDATORY);

            LOGGER.config(
                    "Successfully loaded configuration from resource for path: "
                            + getFileDirectory()
                            + getFileName()
                            + " for class: "
                            + classNameString);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load properties file: " + getFileName(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "getFileName() or getFilePath() returned null or empty string.", e);
        }
    }

    @Override
    public final String getFileExtension() {
        return ".properties";
    }

    public final synchronized String getProperty(final String key) {
        return properties.getProperty(key);
    }

    public final synchronized String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
