/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    protected Image image;
    protected boolean scaleImage = true;

    public ImagePanel(Image image) {
        super();

        this.image = image;

        if (image != null) {
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
            setSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        }

        setOpaque(false);
    }

    public void setImage(Image image) {
        this.image = image;

        setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        setSize(new Dimension(image.getWidth(this), image.getHeight(this)));

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            var g2 = (Graphics2D) g.create();

            if (!scaleImage) {
                g2.drawImage(image, 0, 0, this); // Draw at original size
            } else {

                Insets insets = getInsets(); // Get the border insets
                var panelX = insets.left;
                var panelY = insets.top;
                var panelWidth = getWidth() - insets.left - insets.right;
                var panelHeight = getHeight() - insets.top - insets.bottom;
                var imgWidth = image.getWidth(null);
                var imgHeight = image.getHeight(null);

                if (imgWidth > 0 && imgHeight > 0) {

                    var scaleX = panelWidth / (double) imgWidth;
                    var scaleY = panelHeight / (double) imgHeight;
                    var scale = Math.min(scaleX, scaleY);

                    var drawWidth = (int) (imgWidth * scale);
                    var drawHeight = (int) (imgHeight * scale);

                    var x = panelX + (panelWidth - drawWidth) / 2;
                    var y = panelY + (panelHeight - drawHeight) / 2;

                    g2.drawImage(image, x, y, drawWidth, drawHeight, this);
                }
            }
            g2.dispose();
        }
    }
}
