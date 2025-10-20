/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale;

import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.CheckoutScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.ShopScene;
import com.github.ragudos.kompeter.auth.Session;
import com.github.ragudos.kompeter.auth.SessionManager;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class PointOfSaleScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "point_of_sale";
    public static final SceneGuard SCENE_GUARD = new SceneGuard() {
        @Override
        public boolean canAccess() {
            Session session = SessionManager.getInstance().session();

            return session.user().isAdmin() || session.user().isClerk();
        }
    };

    private final JPanel view = new JPanel(new MigLayout("insets 0", "[grow, center]", "[grow, center]"));

    private final SceneManager sceneManager = new StaticSceneManager();

    @Override
    public void onCreate() {
        view.add(sceneManager.view(), "grow");

        sceneManager.registerScene(ShopScene.SCENE_NAME, () -> new ShopScene());
        sceneManager.registerScene(CheckoutScene.SCENE_NAME, () -> new CheckoutScene());
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
        return ShopScene.SCENE_NAME;
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
