/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.configurations;

import com.github.ragudos.kompeter.utilities.constants.Metadata;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.logging.Logger;

public class ApplicationConfig {
    public class FileSystemApplicationConfig extends AbstractFileSystemConfigProperties {
        public FileSystemApplicationConfig() {
            super();
        }

        @Override
        public String getFileName() {
            return ApplicationConfig.class.getSimpleName() + "-" + Metadata.APP_ENV;
        }
    }

    public class ReadonlyApplicationConfig extends AbstractResourceConfigProperties {
        public ReadonlyApplicationConfig() {
            super();
        }

        @Override
        public String getFileName() {
            return ApplicationConfig.class.getSimpleName() + "-" + Metadata.APP_ENV;
        }

        @Override
        public Class<? extends AbstractResourceConfig> getResourceClass() {
            return ReadonlyApplicationConfig.class;
        }
    }

    public static final Logger LOGGER = KompeterLogger.getLogger(ApplicationConfig.class);

    private static ApplicationConfig instance;

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            return new ApplicationConfig();
        }

        return instance;
    }

    private final FileSystemApplicationConfig config;

    private final ReadonlyApplicationConfig defaultConfig;

    private ApplicationConfig() {
        super();

        config = new FileSystemApplicationConfig();
        defaultConfig = new ReadonlyApplicationConfig();
    }

    public FileSystemApplicationConfig getConfig() {
        return config;
    }

    public ReadonlyApplicationConfig getDefaultConfig() {
        return defaultConfig;
    }

    public String getProperty(String key) {
        var value = config.getProperty(key);

        if (value == null) {
            value = defaultConfig.getProperty(key);
        }

        return value;
    }
}
