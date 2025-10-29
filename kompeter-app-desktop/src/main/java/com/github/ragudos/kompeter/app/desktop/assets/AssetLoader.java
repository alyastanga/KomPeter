/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.assets;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AssetLoader {
    public static BufferedImage loadImage(String relPath) {
        try {
            return ImageIO.read(AssetLoader.class.getResourceAsStream("images/" + relPath));
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
