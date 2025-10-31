/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(final Image image) {
        setOpaque(false);

        if (image == null) {
            return;
        }

        this.image = makeTransparent(image);
        updatePreferredSize();
    }

    public void setImage(final Image image) {
        this.image = makeTransparent(image);
        updatePreferredSize();
        repaint();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (image == null)
            return;

        final Graphics2D g2 = (Graphics2D) g.create();

        // 🪄 Smooth rendering
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int panelW = getWidth();
        final int panelH = getHeight();
        final int imgW = image.getWidth(null);
        final int imgH = image.getHeight(null);

        if (panelW <= 0 || panelH <= 0 || imgW <= 0 || imgH <= 0) {
            g2.dispose();
            return;
        }

        // 🔍 Get UI scale
        final GraphicsConfiguration gc = getGraphicsConfiguration();
        double scaleX = 1.0, scaleY = 1.0;
        if (gc != null) {
            scaleX = gc.getDefaultTransform().getScaleX();
            scaleY = gc.getDefaultTransform().getScaleY();
        }

        final double displayW = panelW * scaleX;
        final double displayH = panelH * scaleY;

        final double imgRatio = (double) imgW / imgH;
        final double panelRatio = displayW / displayH;

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
            final BufferedImage b = new BufferedImage(img.getWidth(null), img.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g2 = b.createGraphics();
            g2.drawImage(img, 0, 0, null);
            g2.dispose();
            img = b;
        }

        final BufferedImage src = (BufferedImage) img;
        final BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                final int rgb = src.getRGB(x, y);
                final Color c = new Color(rgb, true);

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

        final int imgW = image.getWidth(null);
        final int imgH = image.getHeight(null);
        if (imgW <= 0 || imgH <= 0)
            return;

        // 🔍 Get the UI scale (DPI scaling)
        final GraphicsConfiguration gc = getGraphicsConfiguration();
        double scaleX = 1.0, scaleY = 1.0;
        if (gc != null) {
            scaleX = gc.getDefaultTransform().getScaleX();
            scaleY = gc.getDefaultTransform().getScaleY();
        }

        // 🔧 Adjust preferred size based on scale
        final int displayW = (int) Math.round(imgW / scaleX);
        final int displayH = (int) Math.round(imgH / scaleY);

        setPreferredSize(new Dimension(displayW, displayH));
        revalidate();
    }
}
