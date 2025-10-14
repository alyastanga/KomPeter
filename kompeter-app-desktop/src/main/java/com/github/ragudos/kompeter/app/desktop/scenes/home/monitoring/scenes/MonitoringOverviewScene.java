package com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring.scenes;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public final class MonitoringOverviewScene implements Scene {
    public static final String SCENE_NAME = "overview";

    private final JPanel view = new JPanel();

    public MonitoringOverviewScene() {
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
