/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.border;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public final class KompeterBorderFactory {
    public static Border createDashedBorder(float mitlerLimit, float strokeWidth, float[] dash, float dashPhase,
            Color color) {
        return BorderFactory.createStrokeBorder(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, mitlerLimit, dash, dashPhase), color);
    }
}
