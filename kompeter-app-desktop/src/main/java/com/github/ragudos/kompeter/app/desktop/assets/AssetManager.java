/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.assets;

import com.github.ragudos.kompeter.utilities.cache.LRU;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.awt.Image;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public class AssetManager {
    private static final Logger LOGGER = KompeterLogger.getLogger(AssetManager.class);

    private static final LRU<String, Image> IMAGES = new LRU<>(50);
    private static final LRU<String, SVGIconUIColor> icons = new LRU<>(50);
    private static final String ICONS_BASE_PATH =
            AssetManager.class.getPackageName().replace('.', '/');

    private static SVGIconUIColor loadIcon(
            @NotNull final String path, float scale, @NotNull String colorKey) {
        System.out.println(ICONS_BASE_PATH + "/icons/" + path);
        return new SVGIconUIColor(ICONS_BASE_PATH + "/icons/" + path, scale, colorKey);
    }

    public static SVGIconUIColor getOrLoadIcon(
            @NotNull final String path, float scale, @NotNull String colorKey) {
        var icon = icons.get(path + String.format("%s", scale) + colorKey);

        if (icon == null) {
            icon = loadIcon(path, scale, colorKey);
        }

        icons.update(path + String.format("%s", scale) + colorKey, icon);

        return icon;
    }

    public static Optional<Image> getImage(@NotNull final String path) {
        Image image = IMAGES.get(path);

        if (image != null) {
            return Optional.of(image);
        }

        try {
            Image img = AssetLoader.loadImage(path);

            IMAGES.update(path, img);

            return Optional.of(img);
        } catch (IOException | InterruptedException | IllegalArgumentException err) {
            LOGGER.log(Level.SEVERE, "Cannot load image: " + path, err);
        }

        return Optional.empty();
    }
}
