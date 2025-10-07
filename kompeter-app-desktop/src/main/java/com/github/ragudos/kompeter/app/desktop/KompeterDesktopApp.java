package com.github.ragudos.kompeter.app.desktop;

import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.exceptions.GlobalUncaughtExceptionHandler;
import com.github.ragudos.kompeter.app.desktop.frames.MainFrame;
import com.github.ragudos.kompeter.app.desktop.laf.KompeterLightFlatLaf;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class KompeterDesktopApp {
    private static final Logger LOGGER = KompeterLogger.getLogger(KompeterDesktopApp.class);

    public static void main(String[] args) {
        try (ScanResult scanResult =
                new ClassGraph()
                        .acceptModules(AssetManager.class.getModule().toString())
                        .acceptPackages(AssetManager.class.getPackageName())
                        .acceptPaths("fonts")
                        .scan()) {
            for (Resource res : scanResult.getResourcesWithExtension("ttf")) {
                try (InputStream is = res.open()) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(font);

                    LOGGER.info("Successfully registered font: " + font.getFamily());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to register font", e);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to scan for fonts", e);
        }

        boolean lafSetupResult = KompeterLightFlatLaf.setup();

        if (!lafSetupResult) {
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
