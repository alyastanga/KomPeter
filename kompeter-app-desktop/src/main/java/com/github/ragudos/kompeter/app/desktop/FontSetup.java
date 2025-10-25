/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;

public final class FontSetup {
    private static final Logger LOGGER = KompeterLogger.getLogger(FontSetup.class);

    public static final void setup() {
        try (ScanResult scanResult = new ClassGraph().acceptPackages("com/github/ragudos/kompeter/app/desktop/assets")
                .acceptPaths("fonts").scan()) {
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
    }
}
