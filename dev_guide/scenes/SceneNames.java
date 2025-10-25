/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.MainAuthScene;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.SignInAuthScene;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.SignUpAuthScene;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.WelcomeAuthScreen;
import com.github.ragudos.kompeter.app.desktop.scenes.home.HomeScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.inventory.InventoryScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.inventory.scenes.ProductListScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring.MonitoringScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring.scenes.MonitoringOverviewScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.PointOfSaleScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.CheckoutScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.ShopScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.profile.ProfileScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.profile.scenes.EditProfileScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.profile.scenes.ReadProfileScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.settings.SettingsScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.settings.scenes.MainSettingsScene;

public final class SceneNames {
    public static final String toReadable(@NotNull String name) {
        return Arrays.stream(name.split("_")).filter(s -> !s.isEmpty())
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).collect(Collectors.joining(" "));
    }

    public static class AuthScenes {
        public static final String MAIN_AUTH_SCENE;
        public static final String SIGN_IN_AUTH_SCENE;
        public static final String SIGN_UP_AUTH_SCENE;
        public static final String WELCOME_AUTH_SCENE;

        static {
            MAIN_AUTH_SCENE = MainAuthScene.SCENE_NAME;
            SIGN_IN_AUTH_SCENE = MainAuthScene.SCENE_NAME + ParsedSceneName.SEPARATOR + SignInAuthScene.SCENE_NAME;
            SIGN_UP_AUTH_SCENE = MainAuthScene.SCENE_NAME + ParsedSceneName.SEPARATOR + SignUpAuthScene.SCENE_NAME;
            WELCOME_AUTH_SCENE = MainAuthScene.SCENE_NAME + ParsedSceneName.SEPARATOR + WelcomeAuthScreen.SCENE_NAME;
        }
    }

    public static class HomeScenes {
        public static final String HOME_SCENE = HomeScene.SCENE_NAME;

        public static final class InventoryScenes {
            public static final String INVENTORY_SCENE;
            public static final String PRODUCT_LIST_SCENE;

            static {
                INVENTORY_SCENE = HOME_SCENE + ParsedSceneName.SEPARATOR + InventoryScene.SCENE_NAME;
                PRODUCT_LIST_SCENE = INVENTORY_SCENE + ParsedSceneName.SEPARATOR + ProductListScene.SCENE_NAME;
            }
        }

        public static final class MonitoringScenes {
            public static final String MONITORING_SCENE;
            public static final String OVERVIEW_SCENE;

            static {
                MONITORING_SCENE = HOME_SCENE + ParsedSceneName.SEPARATOR + MonitoringScene.SCENE_NAME;
                OVERVIEW_SCENE = MONITORING_SCENE + ParsedSceneName.SEPARATOR + MonitoringOverviewScene.SCENE_NAME;
            }
        }

        public static final class PointOfSaleScenes {
            public static final String CHECKOUT_SCENE;
            public static final String POINT_OF_SALE_SCENE;
            public static final String SHOP_SCENE;

            static {
                POINT_OF_SALE_SCENE = HOME_SCENE + ParsedSceneName.SEPARATOR + PointOfSaleScene.SCENE_NAME;
                CHECKOUT_SCENE = POINT_OF_SALE_SCENE + ParsedSceneName.SEPARATOR + CheckoutScene.SCENE_NAME;
                SHOP_SCENE = POINT_OF_SALE_SCENE + ParsedSceneName.SEPARATOR + ShopScene.SCENE_NAME;
            }
        }

        public static final class ProfileScenes {
            public static final String EDIT_PROFILE_SCENE;
            public static final String PROFILE_SCENE;
            public static final String READ_PROFILE_SCENE;

            static {
                PROFILE_SCENE = HOME_SCENE + ParsedSceneName.SEPARATOR + ProfileScene.SCENE_NAME;
                READ_PROFILE_SCENE = PROFILE_SCENE + ParsedSceneName.SEPARATOR + ReadProfileScene.SCENE_NAME;
                EDIT_PROFILE_SCENE = PROFILE_SCENE + ParsedSceneName.SEPARATOR + EditProfileScene.SCENE_NAME;
            }
        }

        public static final class SettingsScenes {
            public static final String MAIN_SETTINGS_SCENE;
            public static final String SETTINGS_SCENE;

            static {
                SETTINGS_SCENE = HOME_SCENE + ParsedSceneName.SEPARATOR + SettingsScene.SCENE_NAME;
                MAIN_SETTINGS_SCENE = SETTINGS_SCENE + ParsedSceneName.SEPARATOR + MainSettingsScene.SCENE_NAME;
            }
        }
    }
}
