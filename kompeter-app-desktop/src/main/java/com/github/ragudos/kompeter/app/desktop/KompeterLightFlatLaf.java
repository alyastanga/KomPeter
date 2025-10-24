/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop;

import com.formdev.flatlaf.FlatLightLaf;

public final class KompeterLightFlatLaf extends FlatLightLaf {
    public static final String NAME = "KompeterLightFlatLaf";

    static {
        FlatLightLaf.registerCustomDefaultsSource("com.github.ragudos.kompeter.app.desktop.laf");
    }

    public static void installLafInfo() {
        installLafInfo(NAME, KompeterLightFlatLaf.class);
    }

    public static boolean setup() {
        return setup(new KompeterLightFlatLaf());
    }

    public KompeterLightFlatLaf() {
        super();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
