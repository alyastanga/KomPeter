/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

public interface Scene {
    default boolean canHide() {
        return true;
    }

    default boolean canShow() {
        return true;
    }

    default @NotNull Scene self() {
        return this;
    }

    @NotNull String name();

    @NotNull JPanel view();

    default void onBeforeHide() {}

    default void onBeforeShow() {}

    default void onCannotHide() {}

    default void onCannotShow() {}

    void onCreate();

    default void onDestroy() {}

    default void onHide() {}

    default void onShow() {}

    default void syncLookAndFeel() {
        SwingUtilities.updateComponentTreeUI(view());
    }

    default boolean supportsSubScenes() {
        return this instanceof SceneWithSubScenes;
    }
}
