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

    private static final LRU<String, Image> IMAGES = new LRU<>(30);

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
