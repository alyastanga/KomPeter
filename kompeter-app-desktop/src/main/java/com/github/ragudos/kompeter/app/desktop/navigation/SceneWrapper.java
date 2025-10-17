/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.navigation;

import javax.swing.JPanel;

public class SceneWrapper implements Scene {
    private final Scene scene;

    public SceneWrapper(Scene scene) {
        this.scene = scene;
    }

    @Override
    public boolean canHide() {
        Scene currSubScene = getCurrentSubScene();

        if (currSubScene != null) {
            return scene.canHide() && currSubScene.canHide();
        }

        return scene.canHide();
    }

    @Override
    public boolean canShow() {
        var currSubScene = getCurrentSubScene();

        if (currSubScene != null) {
            return scene.canShow() && currSubScene.canShow();
        }

        return scene.canShow();
    }

    private Scene getCurrentSubScene() {
        if (supportsSubScenes()) {
            SceneManager sceneManager = ((SceneWithSubScenes) scene).sceneManager();

            if (sceneManager == null) {
                return null;
            }

            return sceneManager.currentScene();
        }

        return null;
    }

    @Override
    public String name() {
        return scene.name();
    }

    @Override
    public JPanel view() {
        return scene.view();
    }

    @Override
    public Scene self() {
        return scene;
    }

    @Override
    public void onBeforeHide() {
        Scene currSubScene = getCurrentSubScene();

        if (currSubScene != null) {
            currSubScene.onBeforeHide();
        }

        scene.onBeforeHide();
    }

    @Override
    public void onBeforeShow() {
        Scene currSubScene = getCurrentSubScene();

        if (currSubScene != null) {
            currSubScene.onBeforeShow();
        }

        scene.onBeforeShow();
    }

    @Override
    public void onCannotHide() {
        Scene currSubScene = getCurrentSubScene();

        if (currSubScene != null) {
            currSubScene.onCannotHide();
        }

        scene.onCannotHide();
    }

    @Override
    public void onCreate() {
        Scene currSubScene = getCurrentSubScene();

        if (currSubScene != null) {
            currSubScene.onCreate();
        }

        scene.onCreate();
    }

    @Override
    public void onDestroy() {
        if (supportsSubScenes()) {
            SceneManager sceneManager = ((SceneWithSubScenes) scene).sceneManager();

            if (sceneManager != null) {
                sceneManager.destroy();
            }
        }

        scene.onDestroy();
    }

    @Override
    public void onHide() {
        Scene currSubScene = getCurrentSubScene();

        if (currSubScene != null) {
            currSubScene.onHide();
        }

        scene.onHide();
    }

    @Override
    public void onShow() {
        Scene currSubScene = getCurrentSubScene();

        if (currSubScene != null) {
            currSubScene.onShow();
        }

        scene.onShow();
    }

    @Override
    public boolean supportsSubScenes() {
        return scene.supportsSubScenes();
    }
}
