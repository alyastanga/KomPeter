package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.CheckoutScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.ShopScene;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class PointOfSaleScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "point_of_sale";
    public static final SceneGuard SCENE_GUARD =
            new SceneGuard() {
                @Override
                public boolean canAccess() {
                    // Session session = SessionManager.getInstance().session();

                    return true;
                    // return session.user().isAdmin() || session.user().isClerk();
                }
            };

    private final JPanel view = new JPanel();

    private final SceneManager sceneManager = new StaticSceneManager();

    public PointOfSaleScene() {
        onCreate();
    }

    @Override
    public void onCreate() {
        view.setLayout(new MigLayout("insets 0", "[grow, center]", "[grow, center]"));

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
    public void navigateToDefault() {
        SceneNavigator.getInstance().navigateTo(SceneNames.HomeScenes.PointOfSaleScenes.SHOP_SCENE);
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
