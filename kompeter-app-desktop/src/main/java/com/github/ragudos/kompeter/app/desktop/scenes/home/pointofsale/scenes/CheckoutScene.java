/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;

public final class CheckoutScene implements Scene {
    public static final String SCENE_NAME = "checkout";

    private final JPanel view = new JPanel();

    @Override
    public boolean canHide() {
        return Scene.super.canHide();
    }

    @Override
    public boolean canShow() {

        return Scene.super.canShow();
    }

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
