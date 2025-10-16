/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home;

import com.github.ragudos.kompeter.app.desktop.components.MainFooter;
import com.github.ragudos.kompeter.app.desktop.components.MainHeader;
import com.github.ragudos.kompeter.app.desktop.components.MainSidebar;
import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
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

    public static final SceneGuard SCENE_GUARD =
            new SceneGuard() {
                @Override
                public boolean canAccess() {
                    return true;
                    // return SessionManager.getInstance().session() != null;
                }
            };

    private final SceneManager sceneManager = new StaticSceneManager();

    private final JPanel view = new JPanel(new MigLayout("insets 9", "[grow, fill]", "[grow, fill]"));
    private final MainHeader mainHeader = new MainHeader();
    private final MainSidebar mainSidebar = new MainSidebar();
    private final MainFooter mainFooter = new MainFooter();

    private void registerDynamicScenes() {
        Session session = SessionManager.getInstance().session();

        // testing purposes (no sign in yet)
        if (session == null) {
            sceneManager.registerScene(
                    PointOfSaleScene.SCENE_NAME, () -> new PointOfSaleScene(), PointOfSaleScene.SCENE_GUARD);
            sceneManager.registerScene(MonitoringScene.SCENE_NAME, () -> new MonitoringScene());
            sceneManager.registerScene(InventoryScene.SCENE_NAME, () -> new InventoryScene());

            return;
        }

        if (session.user().isPurchasingOfficer()
                || session.user().isLogistics()
                || session.user().isAdmin()) {
            sceneManager.registerScene(MonitoringScene.SCENE_NAME, () -> new MonitoringScene());
            sceneManager.registerScene(InventoryScene.SCENE_NAME, () -> new InventoryScene());
        }

        if (session.user().isClerk() || session.user().isAdmin()) {
            sceneManager.registerScene(
                    PointOfSaleScene.SCENE_NAME, () -> new PointOfSaleScene(), PointOfSaleScene.SCENE_GUARD);
        }
    }

    @Override
    public boolean navigateTo(@NotNull ParsedSceneName parsedSceneName) {
        return sceneManager.navigateTo(parsedSceneName);
    }

    @Override
    public String getDefaultScene() {
        return ProfileScene.SCENE_NAME;
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
        view.add(mainHeader.view(), "grow, dock north");
        view.add(mainFooter.view(), "grow, dock south");
        view.add(mainSidebar.view(), "grow, dock west");
        view.add(sceneManager.view(), "grow");
    }

    @Override
    public void onShow() {
        mainSidebar.initialize();
        mainHeader.initialize();
        mainFooter.initialize();

        sceneManager.registerScene(ProfileScene.SCENE_NAME, () -> new ProfileScene());
        sceneManager.registerScene(SettingsScene.SCENE_NAME, () -> new SettingsScene());

        registerDynamicScenes();
    }

    @Override
    public void onHide() {
        mainHeader.destroy();
        mainSidebar.destroy();
        mainFooter.destroy();

        // destroy because if we navigate away from home, means we are going to auth
        sceneManager.destroy();
    }

    @Override
    public void onDestroy() {
        mainHeader.destroy();
        mainSidebar.destroy();
        mainFooter.destroy();

        sceneManager.destroy();
    }
}
