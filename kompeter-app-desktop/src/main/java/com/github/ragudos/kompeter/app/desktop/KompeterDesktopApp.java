/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.menu.KompeterDrawerBuilder;
import com.github.ragudos.kompeter.app.desktop.system.FormManager;
import com.github.ragudos.kompeter.app.desktop.system.MainForm;
import com.github.ragudos.kompeter.auth.Authentication;
import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.utilities.constants.Metadata;
import com.github.ragudos.kompeter.utilities.io.FileUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import raven.modal.Drawer;

public class KompeterDesktopApp extends JFrame {
    private static final Logger LOGGER = KompeterLogger.getLogger(KompeterDesktopApp.class);
    private static JFrame ROOT_FRAME;

    public static JFrame getRootFrame() {
        return ROOT_FRAME;
    }

    public static void main(final String[] args) {
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
        } catch (final AuthenticationException e) {
            JOptionPane.showMessageDialog(null,
                    "Application cannot be started safely. Please contact the developers.\nReason:\n\n"
                            + e.getMessage(),
                    "Failed to start up", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new KompeterDesktopApp().setVisible(true);
        });
    }

    private final MainFrameWindowListener windowListener = new MainFrameWindowListener();

    public KompeterDesktopApp() {
        Drawer.installDrawer(this, KompeterDrawerBuilder.getInstance());

        FormManager.install(this);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        addWindowListener(windowListener);
        setTitle(Metadata.APP_TITLE + " " + Metadata.APP_VERSION);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 720));
        setSize(getPreferredSize());
        setLocationRelativeTo(null);

        ROOT_FRAME = this;
    }

    private class MainFrameWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(final WindowEvent e) {
            MainForm.getMemoryBar().uninstallMemoryBar();
            removeWindowListener(windowListener);

            dispose();
            System.exit(0);
        }
    }
}
