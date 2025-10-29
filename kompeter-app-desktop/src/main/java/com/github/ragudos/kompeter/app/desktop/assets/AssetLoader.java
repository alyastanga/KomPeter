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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.github.ragudos.kompeter.utilities.cache.LRU;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class AssetLoader {
    private static final Logger LOGGER = KompeterLogger.getLogger(AssetLoader.class);
    private static final LRU<String, BufferedImage> IMAGES = new LRU<>(50);

    public static BufferedImage loadImage(String path, boolean isAbs) {
        String p = isAbs ? path : "images/" + path;

        if (IMAGES.containsKey(p)) {
            return IMAGES.get(p);
        }

        try (InputStream inputStream = AssetLoader.class.getResourceAsStream(p)) {
            if (inputStream == null) {
                try (FileInputStream fileInputStream = new FileInputStream(p)) {
                    BufferedImage image = ImageIO.read(fileInputStream);

                    IMAGES.update(p, image);

                    return image;
                }
            }

            BufferedImage image = ImageIO.read(inputStream);

            IMAGES.update(p, image);

            return image;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);

            return null;
        }
    }
}
