/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring;

import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring.scenes.MonitoringOverviewScene;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public final class MonitoringScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "monitoring";
    public static final SceneGuard SCENE_GUARD =
            new SceneGuard() {
                @Override
                public boolean canAccess() {
                    // Session session = SessionManager.getInstance().session();

                    // return session.user().isAdmin() || session.user().isPurchasingOfficer();

                    return true;
                }
                ;
            };

    private final JPanel view =
            new JPanel(new MigLayout("insets 0", "[grow, center]", "[grow, center]"));

    private SceneManager sceneManager = new StaticSceneManager();

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
        view.add(sceneManager.view(), "grow");

        sceneManager.registerScene(
                MonitoringOverviewScene.SCENE_NAME, () -> new MonitoringOverviewScene());
    }

    @Override
    public boolean navigateTo(@NotNull ParsedSceneName parsedSceneName) {
        return sceneManager.navigateTo(parsedSceneName);
    }

    @Override
    public String getDefaultScene() {
        return MonitoringOverviewScene.SCENE_NAME;
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
    }
}
