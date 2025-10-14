package com.github.ragudos.kompeter.app.desktop.scenes.home;

import com.github.ragudos.kompeter.app.desktop.components.MainHeader;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.PointOfSaleScene;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class HomeScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "home";

    public static final SceneGuard SCENE_GUARD =
            new SceneGuard() {
                @Override
                public boolean canAccess() {
                    return true;
                    // return SessionManager.getInstance().session() != null;
                }
            };

    private final SceneManager sceneManager = new StaticSceneManager();

    private final JPanel view = new JPanel();
    private final MainHeader mainHeader = new MainHeader();

    public HomeScene() {
        onCreate();
    }

    @Override
    public boolean navigateTo(@NotNull String name) {
        return sceneManager.navigateTo(name);
    }

    @Override
    public void navigateToDefault() {
        // TODO: create a main menu/default home scene
        sceneManager.navigateTo(PointOfSaleScene.SCENE_NAME);
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
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
        view.setLayout(new MigLayout("", "[grow,center]", "[grow,center]"));

        view.add(mainHeader.view(), "grow,dock north");
        view.add(sceneManager.view(), "grow");

        sceneManager.registerScene(
                PointOfSaleScene.SCENE_NAME, () -> new PointOfSaleScene(), PointOfSaleScene.SCENE_GUARD);
    }
}
