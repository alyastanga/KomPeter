/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SceneFactory {
    @NotNull Scene createScene();
}
