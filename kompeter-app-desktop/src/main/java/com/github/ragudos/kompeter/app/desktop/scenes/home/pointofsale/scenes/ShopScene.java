package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public final class ShopScene implements Scene {
    public static final String SCENE_NAME = "shop";

    private final JPanel view = new JPanel();

    public ShopScene() {
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
