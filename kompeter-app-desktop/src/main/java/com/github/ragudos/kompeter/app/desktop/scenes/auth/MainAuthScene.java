package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;

public class MainAuthScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "auth";
    public static final SceneGuard SCENE_GUARD = new MainAuthSceneGuard();

    private final JPanel view = new JPanel();
    private final SceneManager sceneManager = new StaticSceneManager();

    public MainAuthScene() {
        onCreate();
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
    }

    @Override
    public void onCreate() {
        sceneManager.registerScene(WelcomeAuthScreen.SCENE_NAME, () -> new WelcomeAuthScreen());
        sceneManager.registerScene(SignInAuthScene.SCENE_NAME, () -> new SignInAuthScene());
        sceneManager.registerScene(SignUpAuthScene.SCENE_NAME, () -> new SignUpAuthScene());
    }

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
        sceneManager.navigateTo(SCENE_NAME + ParsedSceneName.SEPARATOR + WelcomeAuthScreen.SCENE_NAME);
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
