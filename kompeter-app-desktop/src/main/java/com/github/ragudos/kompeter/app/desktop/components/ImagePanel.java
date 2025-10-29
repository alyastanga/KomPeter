/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(Image image) {
        this.image = makeTransparent(image);
        setOpaque(false);
        updatePreferredSize();
    }

    public void setImage(Image image) {
        this.image = image;
        updatePreferredSize();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null)
            return;

        Graphics2D g2 = (Graphics2D) g.create();

        // 🪄 Smooth rendering
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelW = getWidth();
        int panelH = getHeight();
        int imgW = image.getWidth(null);
        int imgH = image.getHeight(null);

        if (panelW <= 0 || panelH <= 0 || imgW <= 0 || imgH <= 0) {
            g2.dispose();
            return;
        }

        // 🔍 Get UI scale
        GraphicsConfiguration gc = getGraphicsConfiguration();
        double scaleX = 1.0, scaleY = 1.0;
        if (gc != null) {
            scaleX = gc.getDefaultTransform().getScaleX();
            scaleY = gc.getDefaultTransform().getScaleY();
        }

        double displayW = panelW * scaleX;
        double displayH = panelH * scaleY;

        double imgRatio = (double) imgW / imgH;
        double panelRatio = displayW / displayH;

        double scale;
        int drawW, drawH;
        int drawX, drawY;

        // 🎯 COVER behavior: fill entire panel, maintain aspect ratio
        if (imgRatio > panelRatio) {
            scale = displayH / imgH;
        } else {
            scale = displayW / imgW;
        }

        drawW = (int) (imgW * scale / scaleX);
        drawH = (int) (imgH * scale / scaleY);
        drawX = (panelW - drawW) / 2;
        drawY = (panelH - drawH) / 2;

        g2.drawImage(image, drawX, drawY, drawW, drawH, this);
        g2.dispose();
    }

    private Image makeTransparent(Image img) {
        if (!(img instanceof BufferedImage)) {
            BufferedImage b = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = b.createGraphics();
            g2.drawImage(img, 0, 0, null);
            g2.dispose();
            img = b;
        }

        BufferedImage src = (BufferedImage) img;
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int rgb = src.getRGB(x, y);
                Color c = new Color(rgb, true);

                // 🎨 make "almost white" pixels transparent
                if (c.getRed() > 240 && c.getGreen() > 240 && c.getBlue() > 240) {
                    dest.setRGB(x, y, 0x00FFFFFF); // fully transparent
                } else {
                    dest.setRGB(x, y, rgb);
                }
            }
        }

        return dest;
    }

    /**
     * Updates the panel’s preferred size based on image dimensions and UI scale.
     */
    private void updatePreferredSize() {
        if (image == null)
            return;

        int imgW = image.getWidth(null);
        int imgH = image.getHeight(null);
        if (imgW <= 0 || imgH <= 0)
            return;

        // 🔍 Get the UI scale (DPI scaling)
        GraphicsConfiguration gc = getGraphicsConfiguration();
        double scaleX = 1.0, scaleY = 1.0;
        if (gc != null) {
            scaleX = gc.getDefaultTransform().getScaleX();
            scaleY = gc.getDefaultTransform().getScaleY();
        }

        // 🔧 Adjust preferred size based on scale
        int displayW = (int) Math.round(imgW / scaleX);
        int displayH = (int) Math.round(imgH / scaleY);

        setPreferredSize(new Dimension(displayW, displayH));
        revalidate();
    }
}
