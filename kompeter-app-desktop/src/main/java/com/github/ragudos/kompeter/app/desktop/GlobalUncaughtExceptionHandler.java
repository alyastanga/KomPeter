/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.github.ragudos.kompeter.utilities.constants.Directories;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogFormatter;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class GlobalUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private static final Logger LOGGER = KompeterLogger.getLogger(GlobalUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            FileHandler fHandler = new FileHandler(Directories.LOGS_DIRECTORY + "/global_uncaight_exception_%u.log",
                    1024 * 1024, 5, true);

            fHandler.setFormatter(new KompeterLogFormatter());

            LOGGER.addHandler(fHandler);
            LOGGER.log(Level.SEVERE, "Uncaught exception in thread: " + t.getName(), e);

            fHandler.close();
        } catch (Exception err) {
            LOGGER.log(Level.SEVERE, "Failed to save log of uncaught exception", err);
        }

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        });
    }
}
