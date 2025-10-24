/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.icons;

import java.awt.Color;

import javax.swing.UIManager;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.ColorFunctions;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;

public class SVGIconUIColor extends FlatSVGIcon {

    public static final String ICONS_BASE_PATH = KompeterDesktopApp.class.getPackageName().replace('.', '/')
            + "/assets/icons/";
    private float alpha;

    private String colorKey;

    public SVGIconUIColor(String name, float scale, String colorKey) {
        this(name, scale, colorKey, 1f);
    }

    public SVGIconUIColor(String name, float scale, String colorKey, float alpha) {
        super(ICONS_BASE_PATH + name, scale);
        this.colorKey = colorKey;
        this.alpha = alpha;
        setColorFilter(new ColorFilter(color -> {
            Color uiColor = UIManager.getColor(getColorKey());
            if (uiColor != null) {
                return getAlpha() == 1 ? uiColor : ColorFunctions.fade(uiColor, getAlpha());
            }
            return color;
        }));
    }

    public float getAlpha() {
        return alpha;
    }

    public String getColorKey() {
        return colorKey;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setColorKey(String colorKey) {
        this.colorKey = colorKey;
    }
}
