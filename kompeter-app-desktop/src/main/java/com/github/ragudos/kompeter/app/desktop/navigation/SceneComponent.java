/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

public interface SceneComponent {
    void destroy();

    void initialize();

    default boolean isBusy() {
        return false;
    }

    boolean isInitialized();

    @NotNull JPanel view();
}
