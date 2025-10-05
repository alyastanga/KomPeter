package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;

public class SignUpAuthScene implements Scene {
    public static final String SCENE_NAME = "sign-in";

    private final JPanel view = new JPanel();

    public SignUpAuthScene() {
        onCreate();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
