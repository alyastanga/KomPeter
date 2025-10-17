/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public final class CheckoutScene implements Scene {
    public static final String SCENE_NAME = "checkout";

    private final JPanel view = new JPanel();

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }

    @Override
    public void onCreate() {}
}
