/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.icons;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import com.formdev.flatlaf.util.AnimatedIcon;
import com.formdev.flatlaf.util.UIScale;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import javax.swing.AbstractButton;
import org.jetbrains.annotations.NotNull;

public class RevealEyeIcon implements AnimatedIcon {
    private final @NotNull FlatSVGIcon icon;
    private final int space;

    public RevealEyeIcon() {
        this(new FlatSVGIcon(AssetManager.class.getResource("icons/eye.svg")), 3);
    }

    public RevealEyeIcon(@NotNull FlatSVGIcon icon, int space) {
        this.icon = icon;
        this.space = space;
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
    public void paintIconAnimated(Component c, Graphics g, int x, int y, float animatedValue) {
        Graphics2D g2 = (Graphics2D) g.create();
        int s = UIScale.scale(space);

        icon.paintIcon(c, g2, x, y);

        icon.setColorFilter(new ColorFilter((color) -> c.getForeground()));

        if (animatedValue > 0) {
            float startX = x + s;
            float startY = y + getIconHeight() - s;

            float endX = x + getIconWidth() - s;
            float endY = y + s;

            Shape shape =
                    new Line2D.Float(
                            startX,
                            startY,
                            startX + (endX - startX) * animatedValue,
                            startY + (endY - startY) * animatedValue);

            drawLine(g2, shape, c.getForeground(), 1.5f);
        }

        g2.dispose();
    }

    private void drawLine(Graphics2D g2, Shape shape, Color color, float size) {
        g2.setColor(color);
        g2.setStroke(
                new BasicStroke(UIScale.scale(size), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(shape);
    }

    @Override
    public float getValue(Component c) {
        return ((AbstractButton) c).isSelected() ? 0 : 1;
    }
}
