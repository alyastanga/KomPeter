/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KompeterLogger {
    private static final Logger PARENT_LOGGER = Logger.getLogger("com.github.ragudos.kompeter");
    private static final Handler HANDLER = new KompeterLogHandler();

    static {
        HANDLER.setFormatter(new KompeterLogFormatter());
        PARENT_LOGGER.setUseParentHandlers(false);
        PARENT_LOGGER.addHandler(HANDLER);
    }

    public static Logger getDetachedLogger(final Class<?> c) {
        var logger = getLogger(c);
        logger.setUseParentHandlers(false);
        return logger;
    }

    public static Level getLevel() {
        return PARENT_LOGGER.getLevel();
    }

    public static Logger getLogger(final Class<?> c) {
        var logger = Logger.getLogger(c.getName());
        logger.setUseParentHandlers(true);
        logger.setParent(PARENT_LOGGER);

        return logger;
    }

    public static <T> T log(final T obj) {
        PARENT_LOGGER.info(String.valueOf(obj));
        return obj;
    }

    public static void setLevel(final Level level) {
        PARENT_LOGGER.setLevel(level);
        HANDLER.setLevel(level);
    }
}
