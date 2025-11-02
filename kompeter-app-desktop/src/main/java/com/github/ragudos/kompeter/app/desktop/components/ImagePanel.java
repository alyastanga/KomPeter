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
    public enum ScaleMode {
        /** Fit entirely inside the panel, maintaining aspect ratio (no cropping). */
        CONTAIN,
        /** Fill the entire panel, maintaining aspect ratio (cropping if needed). */
        COVER,
        /** Keep the image at its original size, centered. */
        FIXED
    }

    private Image image;
    private ScaleMode mode = ScaleMode.COVER;

    public Image image() {
        return image;
    }

    public ImagePanel(final Image image) {
        setOpaque(false);

        if (image == null) {
            return;
        }

        this.image = makeTransparent(image);
        updatePreferredSize();
    }

    public void setScaleMode(final ScaleMode mode) {
        this.mode = mode != null ? mode : ScaleMode.COVER;

        updatePreferredSize();
        repaint();
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

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
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

        double drawW = 0, drawH = 0;
        double drawX = 0, drawY = 0;

        switch (mode) {
            case COVER -> {
                final double scale = (imgRatio > panelRatio)
                        ? ((double) panelH * scaleY / imgH)
                        : ((double) panelW * scaleX / imgW);

                drawW = imgW * scale / scaleX;
                drawH = imgH * scale / scaleY;
                drawX = (panelW - drawW) / 2.0;
                drawY = (panelH - drawH) / 2.0;
            }
            case CONTAIN -> {
                final double scale = (imgRatio > panelRatio)
                        ? ((double) panelW * scaleX / imgW)
                        : ((double) panelH * scaleY / imgH);

                drawW = imgW * scale / scaleX;
                drawH = imgH * scale / scaleY;
                drawX = (panelW - drawW) / 2.0;
                drawY = (panelH - drawH) / 2.0;
            }
            case FIXED -> {
                // keep original image size centered
                drawW = imgW / scaleX;
                drawH = imgH / scaleY;
                drawX = (panelW - drawW) / 2.0;
                drawY = (panelH - drawH) / 2.0;
            }
        }

        g2.drawImage(image, (int) drawX, (int) drawY, (int) drawW, (int) drawH, this);
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

        final GraphicsConfiguration gc = getGraphicsConfiguration();
        double scaleX = 1.0, scaleY = 1.0;
        if (gc != null) {
            scaleX = gc.getDefaultTransform().getScaleX();
            scaleY = gc.getDefaultTransform().getScaleY();
        }

        // 🧩 Adjust based on mode
        int displayW, displayH;
        switch (mode) {
            case FIXED -> {
                // Keep the image’s real pixel size (DPI adjusted)
                displayW = (int) Math.round(imgW / scaleX);
                displayH = (int) Math.round(imgH / scaleY);
            }
            case CONTAIN -> {
                // Let layout managers stretch/shrink the panel freely
                // (the panel adapts to parent, not the image)
                displayW = displayH = 0;
            }
            case COVER -> {
                displayW = (int) Math.round(imgW / scaleX);
                displayH = (int) Math.round(imgH / scaleY);
            }
            default -> {
                displayW = (int) Math.round(imgW / scaleX);
                displayH = (int) Math.round(imgH / scaleY);
            }
        }

        if (displayW > 0 && displayH > 0) {
            setPreferredSize(new Dimension(displayW, displayH));
        } else {
            // Reset preferred size to allow flexible resizing
            setPreferredSize(null);
        }

        revalidate();
    }
}
