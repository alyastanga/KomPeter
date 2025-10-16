/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestKompeterLogger {
    private static final Logger LOGGER = KompeterLogger.getLogger(TestKompeterLogger.class);

    public static void main(String[] args) {
        // 🔥 Now test different types of logs
        LOGGER.info("Hello, this is an info log!");
        LOGGER.warning("Warning: something looks odd...");
        LOGGER.severe("Severe Error happened!");

        // 🔥 Multi-line log message
        LOGGER.info("This is a multi-line\nlog message\nto see how it behaves.");

        // 🔥 Log with parameters
        LOGGER.log(Level.INFO, "User {0} logged in from {1}", new Object[] {"Alice", "192.168.1.5"});

        // 🔥 Log with exception
        try {
            throw new RuntimeException("Test Exception!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Caught an exception:", e);
        }

        try {
            // Simulate a situation where an exception throws another exception with
            // suppressed ones
            throw new RuntimeException("Outer Exception", new Throwable("Inner Exception"));
        } catch (RuntimeException outerException) {
            // Suppress the exception and create the log record
            outerException.addSuppressed(new Exception("Suppressed Exception 1"));
            outerException.addSuppressed(new Exception("Suppressed Exception 2"));

            LOGGER.log(Level.SEVERE, "Caught an outer exception:", outerException);
        }

        try {
            throw new RuntimeException("Outer Exception", new Throwable("Cause of the problem"));
        } catch (RuntimeException outerException) {
            LOGGER.log(Level.SEVERE, "Caught an outer exception:", outerException);
        }
    }
}
