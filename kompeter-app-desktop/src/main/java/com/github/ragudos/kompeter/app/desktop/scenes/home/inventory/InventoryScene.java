package com.github.ragudos.kompeter.app.desktop.scenes.home.inventory;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.app.desktop.scenes.home.inventory.scenes.ProductListScene;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class InventoryScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "inventory";
    public static final SceneGuard SCENE_GUARD =
            new SceneGuard() {
                public boolean canAccess() {
                    // Session session = SessionManager.getInstance().session();

                    // return session.user().isAdmin() || session.user().isLogistics();

                    return true;
                }
                ;
            };

    private final JPanel view = new JPanel();

    private final SceneManager sceneManager = new StaticSceneManager();

    public InventoryScene() {
        onCreate();
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
    public void onCreate() {
        view.setLayout(new MigLayout("insets 0", "[grow, center]", "[grow, center]"));

        view.add(sceneManager.view(), "grow");

        sceneManager.registerScene(ProductListScene.SCENE_NAME, () -> new ProductListScene());
    }

    @Override
    public void navigateToDefault() {
        SceneNavigator.getInstance()
                .navigateTo(SceneNames.HomeScenes.InventoryScenes.PRODUCT_LIST_SCENE);
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
