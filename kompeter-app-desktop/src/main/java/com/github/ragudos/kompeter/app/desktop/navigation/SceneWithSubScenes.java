package com.github.ragudos.kompeter.app.desktop.navigation;

import org.jetbrains.annotations.NotNull;

public interface SceneWithSubScenes extends Scene {
    boolean navigateTo(@NotNull String name);

    void navigateToDefault();

    SceneManager sceneManager();
}
