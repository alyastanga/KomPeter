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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetLoader;
import com.github.ragudos.kompeter.app.desktop.components.ImagePanel;
import com.github.ragudos.kompeter.app.desktop.menu.KompeterDrawerBuilder;
import com.github.ragudos.kompeter.app.desktop.system.FormManager;
import com.github.ragudos.kompeter.app.desktop.system.MainForm;
import com.github.ragudos.kompeter.auth.Authentication;
import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.utilities.constants.Metadata;
import com.github.ragudos.kompeter.utilities.io.FileUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;

public class KompeterDesktopApp extends JFrame {
    private static final Logger LOGGER = KompeterLogger.getLogger(KompeterDesktopApp.class);
    private static JFrame ROOT_FRAME;

    public static JFrame getRootFrame() {
        return ROOT_FRAME;
    }

    private static class Splash extends JWindow {
        ImagePanel image;
        JLabel label;

        public Splash() {
            image = new ImagePanel(AssetLoader.loadImage("logo.png", false));
            image.setMinimumSize(new Dimension(150, 150));
            image.setMaximumSize(new Dimension(150, 150));

            label = new JLabel("Loading...");

            setLayout(new MigLayout("insets 12, flowx, wrap, al center center", "[grow, fill, center]"));

            label.setHorizontalAlignment(JLabel.CENTER);

            add(image);
            add(label, "gaptop 24px");

            setMinimumSize(new Dimension(225, 225));
            setPreferredSize(new Dimension(225, 225));
            setMaximumSize(new Dimension(225, 225));

            pack();
            setLocationRelativeTo(null);
        }
    }

    private static Splash splash;

    public static void main(final String[] args) {
        if (!KompeterLightFlatLaf.setup()) {
            LOGGER.log(Level.SEVERE, "Failed to setup custom L&F");
            LOGGER.log(Level.SEVERE, "Using default L&F");
        }

        SwingUtilities.invokeLater(() -> {
            splash = new Splash();
            splash.setVisible(true);
        });

        FileUtils.setupConfig();

        SwingUtilities.invokeLater(() -> {
            splash.label.setText("Setting up database...");
        });

        AbstractMigratorFactory.setupSqlite();

        SwingUtilities.invokeLater(() -> {
            splash.label.setText("Setting up fonts...");
        });

        FontSetup.setup();

        Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());

        try {
            SwingUtilities.invokeLater(() -> {
                splash.label.setText("Logging in from old session.");
            });

            Authentication.signInFromStoredSessionToken();
        } catch (final AuthenticationException e) {
            SwingUtilities.invokeLater(() -> {
                splash.dispose();
                JOptionPane.showMessageDialog(null,
                        "Application cannot be started safely. Please contact the developers.\nReason:\n\n"
                                + e.getMessage(),
                        "Failed to start up", JOptionPane.ERROR_MESSAGE);
            });

            return;
        }

        SwingUtilities.invokeLater(() -> {
            splash.label.setText("Welcome!");

            try {
                Thread.sleep(150);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

            splash.dispose();
            splash = null;
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
