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
import com.github.ragudos.kompeter.auth.Authentication;
import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.utilities.io.FileUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class KompeterDesktopApp {
    private static final Logger LOGGER = KompeterLogger.getLogger(KompeterDesktopApp.class);

    public static void main(String[] args) {
        FileUtils.setupConfig();
        AbstractMigratorFactory.setupSqlite();
        FontSetup.setup();

        if (!KompeterLightFlatLaf.setup()) {
            LOGGER.log(Level.SEVERE, "Failed to setup custom L&F");
            LOGGER.log(Level.SEVERE, "Using default L&F");
        }

        Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());

        try {
            Authentication.signInFromStoredSessionToken();
        } catch (AuthenticationException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Application cannot be started safely. Please contact the developers.\nReason:\n\n"
                            + e.getMessage(),
                    "Failed to start up",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(
                () -> {
                    MainFrame mainFrame = new MainFrame();

                    mainFrame.setVisible(true);
                });
    }
}
