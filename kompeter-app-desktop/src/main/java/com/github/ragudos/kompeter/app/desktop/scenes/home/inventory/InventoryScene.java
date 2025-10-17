/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.inventory;

import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.home.inventory.scenes.ProductListScene;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class InventoryScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "inventory";
    public static final SceneGuard SCENE_GUARD =
            new SceneGuard() {
                @Override
                public boolean canAccess() {
                    // Session session = SessionManager.getInstance().session();

                    // return session.user().isAdmin() || session.user().isLogistics();

                    return true;
                }
                ;
            };

    private final JPanel view = new JPanel();

    private final SceneManager sceneManager = new StaticSceneManager();

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }

    @Override
    public void onCreate() {
        view.setLayout(new MigLayout("insets 0", "[grow, center]", "[grow, center]"));

        view.add(sceneManager.view(), "grow");

        sceneManager.registerScene(ProductListScene.SCENE_NAME, () -> new ProductListScene());
    }

    @Override
    public String getDefaultScene() {
        return ProductListScene.SCENE_NAME;
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
