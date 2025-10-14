package com.github.ragudos.kompeter.app.desktop.scenes.home;

import com.github.ragudos.kompeter.app.desktop.components.MainHeader;
import com.github.ragudos.kompeter.app.desktop.components.MainSidebar;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames.HomeScenes.SettingsScenes;
import com.github.ragudos.kompeter.app.desktop.scenes.home.inventory.InventoryScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring.MonitoringScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.PointOfSaleScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.profile.ProfileScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.settings.SettingsScene;
import com.github.ragudos.kompeter.auth.Session;
import com.github.ragudos.kompeter.auth.SessionManager;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class HomeScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "home";

    public static final SceneGuard SCENE_GUARD = new SceneGuard() {
        @Override
        public boolean canAccess() {
            return true;
            // return SessionManager.getInstance().session() != null;
        }
    };

    private final SceneManager sceneManager = new StaticSceneManager();

    private final JPanel view = new JPanel();
    private final MainHeader mainHeader = new MainHeader();
    private final MainSidebar mainSidebar = new MainSidebar();

    public HomeScene() {
        onCreate();
    }

    private void registerDynamicScenes() {
        Session session = SessionManager.getInstance().session();

        // testing purposes (no sign in yet)
        if (session == null) {
            sceneManager.unregisterScene(PointOfSaleScene.SCENE_NAME);
            sceneManager.unregisterScene(MonitoringScene.SCENE_NAME);
            sceneManager.unregisterScene(InventoryScene.SCENE_NAME);

            sceneManager.registerScene(
                    PointOfSaleScene.SCENE_NAME, () -> new PointOfSaleScene(), PointOfSaleScene.SCENE_GUARD);
            sceneManager.registerScene(
                    MonitoringScene.SCENE_NAME, () -> new MonitoringScene());
            sceneManager.registerScene(
                    InventoryScene.SCENE_NAME, () -> new InventoryScene());

            return;
        }

        if (session.user().isPurchasingOfficer() || session.user().isLogistics() || session.user().isAdmin()) {
            sceneManager.unregisterScene(PointOfSaleScene.SCENE_NAME);

            sceneManager.registerScene(
                    MonitoringScene.SCENE_NAME, () -> new MonitoringScene());
            sceneManager.registerScene(
                    InventoryScene.SCENE_NAME, () -> new InventoryScene());
        }

        if (session.user().isClerk() || session.user().isAdmin()) {
            sceneManager.unregisterScene(MonitoringScene.SCENE_NAME);
            sceneManager.unregisterScene(InventoryScene.SCENE_NAME);

            sceneManager.registerScene(
                    PointOfSaleScene.SCENE_NAME, () -> new PointOfSaleScene(), PointOfSaleScene.SCENE_GUARD);
        }
    }

    @Override
    public boolean navigateTo(@NotNull String name) {
        return sceneManager.navigateTo(name);
    }

    @Override
    public void navigateToDefault() {
        SceneNavigator.getInstance().navigateTo(SceneNames.HomeScenes.ProfileScenes.PROFILE_SCENE);
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
        view.add(mainSidebar.view(), "grow, dock west");
        view.add(sceneManager.view(), "grow");

        mainSidebar.initialize();
        mainHeader.initialize();

        sceneManager.registerScene(ProfileScene.SCENE_NAME, () -> new ProfileScene());
        sceneManager.registerScene(SettingsScene.SCENE_NAME, () -> new SettingsScene());
    }

    @Override
    public void onShow() {
        mainSidebar.initialize();
        mainHeader.initialize();

        registerDynamicScenes();
    }

    @Override
    public void onHide() {
        mainHeader.destroy();
        mainSidebar.destroy();
    }
}
