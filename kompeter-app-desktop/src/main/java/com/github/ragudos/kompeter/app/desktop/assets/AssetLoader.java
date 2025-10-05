package com.github.ragudos.kompeter.app.desktop.assets;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

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
