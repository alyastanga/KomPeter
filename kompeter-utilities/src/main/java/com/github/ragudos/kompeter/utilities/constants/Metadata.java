/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.constants;

import com.github.ragudos.kompeter.utilities.io.PropertiesIO;
import java.io.IOException;
import java.util.Properties;

public final class Metadata {
    private static final Properties properties = new Properties();

    public static final String APP_TITLE;
    public static final String APP_VERSION;
    public static final String APP_ENV;

    static {
        try {
            PropertiesIO.loadProperties(Metadata.class, properties, "metadata");
        } catch (IllegalArgumentException e) {
            // DO nothing
        } catch (IOException e) {
            throw new RuntimeException("Failed to load metadata.properties", e);
        }

        APP_TITLE = properties.getProperty(PropertyKey.Metadata.APP_TITLE);
        APP_VERSION = properties.getProperty(PropertyKey.Metadata.APP_VERSION);
        APP_ENV = properties.getProperty(PropertyKey.Metadata.APP_ENV);
    }

    private Metadata() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
