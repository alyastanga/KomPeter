/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
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

    private final JPanel view = new JPanel(new BorderLayout());
    private final SceneManager sceneManager = new StaticSceneManager();

    @Override
    public void onCreate() {
        view.add(sceneManager.view(), BorderLayout.CENTER);
    }

    @Override
    public void onShow() {
        sceneManager.registerScene(WelcomeAuthScreen.SCENE_NAME, () -> new WelcomeAuthScreen());
        sceneManager.registerScene(SignInAuthScene.SCENE_NAME, () -> new SignInAuthScene());
        sceneManager.registerScene(SignUpAuthScene.SCENE_NAME, () -> new SignUpAuthScene());
    }

    @Override
    public void onHide() {
        // destroy because if we navigate away from main auth, it means
        // we login
        sceneManager.destroy();
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
    public String getDefaultScene() {
        return WelcomeAuthScreen.SCENE_NAME;
    }

    @Override
    public boolean navigateTo(@NotNull ParsedSceneName parsedSceneName) {
        return sceneManager.navigateTo(parsedSceneName);
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
    }
}
