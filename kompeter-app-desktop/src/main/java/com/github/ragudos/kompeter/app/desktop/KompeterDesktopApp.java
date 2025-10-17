/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop;

import com.github.ragudos.kompeter.app.desktop.exceptions.GlobalUncaughtExceptionHandler;
import com.github.ragudos.kompeter.app.desktop.frames.MainFrame;
import com.github.ragudos.kompeter.app.desktop.laf.KompeterLightFlatLaf;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class KompeterDesktopApp {
    private static final Logger LOGGER = KompeterLogger.getLogger(KompeterDesktopApp.class);

    public static void main(String[] args) {
        FontSetup.setup();

        if (!KompeterLightFlatLaf.setup()) {
            LOGGER.log(Level.SEVERE, "Failed to setup custom L&F");
            LOGGER.log(Level.SEVERE, "Using default L&F");
        }

        Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());

        SwingUtilities.invokeLater(
                () -> {
                    MainFrame mainFrame = new MainFrame();

                    mainFrame.setVisible(true);
                });
    }
}
