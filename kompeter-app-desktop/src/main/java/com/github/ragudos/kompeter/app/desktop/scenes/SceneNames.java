package com.github.ragudos.kompeter.app.desktop.scenes;

import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.MainAuthScene;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.SignInAuthScene;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.SignUpAuthScene;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.WelcomeAuthScreen;
import com.github.ragudos.kompeter.app.desktop.scenes.home.HomeScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.PointOfSaleScene;

public final class SceneNames {
    public static class AuthScenes {
        public static final String MAIN_AUTH_SCENE = MainAuthScene.SCENE_NAME;
        public static final String WELCOME_AUTH_SCENE =
                MainAuthScene.SCENE_NAME + ParsedSceneName.SEPARATOR + WelcomeAuthScreen.SCENE_NAME;
        public static final String SIGN_IN_AUTH_SCENE =
                MainAuthScene.SCENE_NAME + ParsedSceneName.SEPARATOR + SignInAuthScene.SCENE_NAME;
        public static final String SIGN_UP_AUTH_SCENE =
                MainAuthScene.SCENE_NAME + ParsedSceneName.SEPARATOR + SignUpAuthScene.SCENE_NAME;
    }

    public static class HomeScenes {
        public static final String HOME_SCENE = HomeScene.SCENE_NAME;

        public static final class PointOfSaleScenes {
            public static final String POINT_OF_SALE_SCENE = PointOfSaleScene.SCENE_NAME;
        }
    }
}
