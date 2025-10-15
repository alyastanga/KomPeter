/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class MainAuthScene implements SceneWithSubScenes {
    public static class MainAuthSceneGuard implements SceneGuard {
        @Override
        public boolean canAccess() {
            return true;
        }
    }

    public static final String SCENE_NAME = "auth";
    public static final SceneGuard SCENE_GUARD = new MainAuthSceneGuard();

    private final JPanel view = new JPanel();
    private final SceneManager sceneManager = new StaticSceneManager();

    public static void main(String[] args) {
        new MainAuthScene();
    }

    public MainAuthScene() {
        onCreate();
    }

    @Override
    public void onCreate() {
        view.setLayout(new BorderLayout());

        view.add(sceneManager.view(), BorderLayout.CENTER);

        sceneManager.registerScene(WelcomeAuthScreen.SCENE_NAME, () -> new WelcomeAuthScreen());
        sceneManager.registerScene(SignInAuthScene.SCENE_NAME, () -> new SignInAuthScene());
        sceneManager.registerScene(SignUpAuthScene.SCENE_NAME, () -> new SignUpAuthScene());
    }

    @Override
    public void onShow() {}

    @Override
    public void onHide() {}

    @Override
    public void onDestroy() {
        sceneManager.destroy();
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
    public void navigateToDefault() {
        SceneNavigator.getInstance().navigateTo(SceneNames.AuthScenes.WELCOME_AUTH_SCENE);
    }

    @Override
    public boolean navigateTo(@NotNull String name) {
        return sceneManager.navigateTo(name);
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
    }
}
