package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;

public class MainAuthSceneGuard implements SceneGuard {
    @Override
    public boolean canAccess() {
        return false;
    }
}
