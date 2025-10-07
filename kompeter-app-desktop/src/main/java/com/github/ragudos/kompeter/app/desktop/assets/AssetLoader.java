package com.github.ragudos.kompeter.app.desktop.assets;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.jetbrains.annotations.NotNull;

public class AssetLoader {
    public static Image loadImage(@NotNull final String path)
            throws IOException, InterruptedException, IllegalArgumentException {
        try (InputStream resourceStream = AssetLoader.class.getResourceAsStream(path)) {
            if (resourceStream == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }

            return ImageIO.read(resourceStream);
        }
    }
}
