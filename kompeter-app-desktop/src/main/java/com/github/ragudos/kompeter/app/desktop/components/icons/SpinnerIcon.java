/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.util.AnimatedIcon;
import com.github.ragudos.kompeter.app.desktop.assets.SVGIconUIColor;

public class SpinnerIcon implements AnimatedIcon {
    private final @NotNull SVGIconUIColor icon;
    private final float rotationsPerSecond;

    public SpinnerIcon(@NotNull SVGIconUIColor icon) {
        this(icon, 1f); // default 1 rotation per second
    }

    public SpinnerIcon(@NotNull SVGIconUIColor icon, float rotationsPerSecond) {
        this.icon = icon;
        this.rotationsPerSecond = rotationsPerSecond;
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public float getValue(Component c) {
        // continuously loops from 0 to 1
        long now = System.currentTimeMillis();
        float period = 1000f / rotationsPerSecond; // ms per rotation
        return (now % (int) period) / period;
    }

    @Override
    public void paintIconAnimated(Component c, Graphics g, int x, int y, float animatedValue) {
        Graphics2D g2 = (Graphics2D) g.create();

        int w = getIconWidth();
        int h = getIconHeight();
        int cx = x + w / 2;
        int cy = y + h / 2;

        // compute rotation based on animation value
        float angle = (float) (animatedValue * 2 * Math.PI * rotationsPerSecond);

        AffineTransform old = g2.getTransform();
        g2.rotate(angle, cx, cy);

        icon.paintIcon(c, g2, x, y);

        g2.setTransform(old);
        g2.dispose();
    }
}
