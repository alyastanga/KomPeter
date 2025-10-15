/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import org.jetbrains.annotations.NotNull;

public record SceneEntry(@NotNull SceneFactory sceneFactory, @NotNull SceneGuard sceneGuard) {}
