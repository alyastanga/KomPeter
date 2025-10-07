package com.github.ragudos.kompeter.app.desktop.navigation;

import org.jetbrains.annotations.NotNull;

public record SceneEntry(@NotNull SceneFactory sceneFactory, @NotNull SceneGuard sceneGuard) {}
