package com.github.ragudos.kompeter.lookandfeel;

import com.formdev.flatlaf.FlatLightLaf;

public class KompeterLightFlatLaf extends FlatLightLaf {
    public static final String NAME = "KompeterLightFlatLaf";

    public static void installLafInfo() {
        installLafInfo(NAME, KompeterLightFlatLaf.class);
    }

    public static boolean setup() {
        return setup(new KompeterLightFlatLaf());
    }

    @Override
    public String getName() {
        return NAME;
    }
}
