/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.assets;

import com.formdev.flatlaf.extras.FlatSVGIcon;
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
    private static final LRU<String, FlatSVGIcon> icons = new LRU<>(50);

    private static FlatSVGIcon loadIcon(@NotNull final String path) {
        return new FlatSVGIcon(AssetManager.class.getResource(path));
    }

    public static FlatSVGIcon getOrLoadIcon(@NotNull final String path) {
        var icon = icons.get(path);

        if (icon == null) {
            icon = loadIcon(path);
        }

        icons.update(path, icon);

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
