/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.io;

import com.github.ragudos.kompeter.utilities.DateUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public class PropertiesIO {
    private static final Logger LOGGER = KompeterLogger.getLogger(PropertiesIO.class);
    public static final String PROPERTIES_FILE_EXTENSION = ".properties";

    public static final void loadProperties(
            @NotNull final Class<?> clazz,
            @NotNull final Properties properties,
            @NotNull final String name)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, "", FileLoadLeniency.MANDATORY);
    }

    public static final void loadProperties(
            @NotNull final Class<?> clazz,
            @NotNull final Properties properties,
            @NotNull final String name,
            FileLoadLeniency leniency)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, "", leniency);
    }

    public static final void loadProperties(
            @NotNull final Class<?> clazz,
            @NotNull final Properties properties,
            @NotNull final String name,
            @NotNull final String path)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, path, FileLoadLeniency.MANDATORY);
    }

    public static final void loadProperties(
            @NotNull final Class<?> clazz,
            @NotNull final Properties properties,
            @NotNull final String name,
            @NotNull final String path,
            FileLoadLeniency leniency)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Properties file name cannot be null or empty");
        }

        if (path == null) {
            throw new IllegalArgumentException("Properties file path cannot be null");
        }

        var filePath = path + name + PROPERTIES_FILE_EXTENSION;
        LOGGER.info("Loading properties file: " + filePath);

        try (var inputStream = clazz.getResourceAsStream(filePath)) {
            if (properties != null) {
                properties.load(inputStream);
            } else {
                switch (leniency) {
                    case MANDATORY -> {
                        throw new FileNotFoundException("Properties file not found: " + filePath);
                    }
                    case LOG_MISSING -> {
                        LOGGER.warning("Properties file not found: " + filePath);
                    }
                    case CREATE_FILE_IF_MISSING -> {
                        // DO nothing since we cannot write to a resource file
                        // as we don't know the path of the clazz on runtime.
                    }
                    case ALLOW_MISSING -> {
                        LOGGER.info("Properties file not found: " + filePath);
                    }
                }
            }
        } catch (NullPointerException e) {
            // DO nothing
        }
    }

    public static final @NotNull Properties loadProperties(
            @NotNull final Class<?> clazz, @NotNull final String name)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name);

        return properties;
    }

    public static final @NotNull Properties loadProperties(
            @NotNull final Class<?> clazz, @NotNull final String name, @NotNull final String path)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name, path);

        return properties;
    }

    public static final @NotNull Properties loadProperties(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final String path,
            FileLoadLeniency leniency)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name, path, leniency);

        return properties;
    }

    public static final Properties loadPropertiesFromFileSystem(@NotNull final String filePath)
            throws FileNotFoundException, IOException {
        var properties = new Properties();
        loadPropertiesFromFileSystem(filePath, properties);
        return properties;
    }

    public static final Properties loadPropertiesFromFileSystem(
            @NotNull final String filePath, @NotNull final FileLoadLeniency leniency)
            throws FileNotFoundException, IOException {
        var properties = new Properties();
        loadPropertiesFromFileSystem(filePath, properties, leniency);
        return properties;
    }

    public static final void loadPropertiesFromFileSystem(
            @NotNull final String filePath, @NotNull final Properties properties)
            throws FileNotFoundException, IOException {
        loadPropertiesFromFileSystem(filePath, properties, FileLoadLeniency.MANDATORY);
    }

    public static final void loadPropertiesFromFileSystem(
            @NotNull final String filePath,
            @NotNull final Properties properties,
            @NotNull final FileLoadLeniency leniency)
            throws FileNotFoundException, IOException {
        try (var inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            switch (leniency) {
                case MANDATORY -> {
                    throw new FileNotFoundException("Properties file not found: " + filePath);
                }
                case LOG_MISSING -> {
                    LOGGER.warning("Properties file not found: " + filePath);
                }
                case CREATE_FILE_IF_MISSING -> {
                    new File(filePath).createNewFile();
                }
                case ALLOW_MISSING -> {
                    LOGGER.info("Properties file not found: " + filePath);
                }
            }
        }
    }

    public static final void savePropertiesInFileSystem(
            @NotNull final Properties properties, @NotNull final String filePath)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        savePropertiesInFileSystem(properties, filePath, null);
    }

    public static final void savePropertiesInFileSystem(
            @NotNull final Properties properties,
            @NotNull final String filePath,
            @NotNull final String headerComment)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        var comment = DateUtils.getDateWithFormat();

        if (headerComment != null && !headerComment.isEmpty()) {
            comment = headerComment + "\n" + comment;
        }

        try (var outputStream = new FileOutputStream(filePath)) {
            properties.store(outputStream, comment);
        } catch (SecurityException e) {
            LOGGER.severe("Denied access to file: " + filePath);
        } catch (ClassCastException e) {
            // TODO: A way to clean the properties file if it is corrupted.
            LOGGER.log(Level.SEVERE, "Configuration file in " + filePath + " contains invalid data. ", e);
        }
    }

    private PropertiesIO() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
