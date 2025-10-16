/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import org.jetbrains.annotations.NotNull;

public interface SceneWithSubScenes extends Scene {
    boolean navigateTo(@NotNull ParsedSceneName parsedSceneName);

    String getDefaultScene();

    SceneManager sceneManager();

    @Override
    default void onBeforeHide() {
        if (sceneManager().currentScene() == null) {
            return;
        }

        sceneManager().currentScene().onBeforeHide();
    }

    @Override
    default void onBeforeShow() {

        if (sceneManager().currentScene() == null) {
            return;
        }

        sceneManager().currentScene().canShow();
    }

    @Override
    default boolean canHide() {
        if (sceneManager().currentScene() == null) {
            return true;
        }

        return sceneManager().currentScene().canHide();
    }

    @Override
    default boolean canShow() {
        if (sceneManager().currentScene() == null) {
            return true;
        }

        return sceneManager().currentScene().canShow();
    }

    @Override
    default void onCannotHide() {
        if (sceneManager().currentScene() == null) {
            return;
        }

        sceneManager().currentScene().onCannotHide();
    }

    @Override
    default void onCannotShow() {
        if (sceneManager().currentScene() == null) {
            return;
        }

        sceneManager().currentScene().onCannotShow();
    }
}
