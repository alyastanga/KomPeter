/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.profile.scenes;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public final class ReadProfileScene implements Scene {
    public static final String SCENE_NAME = "read_profile";

    private final JPanel view = new JPanel();

    public ReadProfileScene() {
        onCreate();
    }

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
