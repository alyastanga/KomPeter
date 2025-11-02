/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.assets;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import com.github.ragudos.kompeter.utilities.cache.LRU;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class AssetLoader {
    private static final LRU<String, BufferedImage> IMAGES = new LRU<>(50);
    private static final Logger LOGGER = KompeterLogger.getLogger(AssetLoader.class);

    public static BufferedImage loadImage(final String path, final boolean isAbs) {
        final String p = isAbs ? path : "images/" + path;

        if (IMAGES.containsKey(p)) {
            return IMAGES.get(p);
        }

        try (InputStream inputStream = AssetLoader.class.getResourceAsStream(p)) {
            if (inputStream == null) {
                try (FileInputStream fileInputStream = new FileInputStream(p)) {
                    final BufferedImage image = ImageIO.read(fileInputStream);

                    IMAGES.update(p, image);

                    return image;
                }
            }

            final BufferedImage image = ImageIO.read(inputStream);

            IMAGES.update(p, image);

            return image;
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "", e);

            return null;
        }
    }

    public static void loadImageAsync(final String path, final boolean isAbs, final Consumer<BufferedImage> callback) {
        final String p = isAbs ? path : "images/" + path;
        final BufferedImage cached = IMAGES.get(p);

        if (cached != null) {
            callback.accept(cached);
            return;
        }

        CompletableFuture.supplyAsync(() -> loadImage(p, true)) // reuse existing method
                .thenAccept(image -> {
                    if (image != null) {
                        IMAGES.update(p, image);
                    }

                    SwingUtilities.invokeLater(() -> callback.accept(image));
                });
    }
}
