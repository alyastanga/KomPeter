package com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.app.desktop.scenes.home.monitoring.scenes.MonitoringOverviewScene;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public final class MonitoringScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "monitoring";
    public static final SceneGuard SCENE_GUARD =
            new SceneGuard() {
                public boolean canAccess() {
                    // Session session = SessionManager.getInstance().session();

                    // return session.user().isAdmin() || session.user().isPurchasingOfficer();

                    return true;
                }
                ;
            };

    private final JPanel view = new JPanel();

    private SceneManager sceneManager = new StaticSceneManager();

    public MonitoringScene() {
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

        sceneManager.registerScene(
                MonitoringOverviewScene.SCENE_NAME, () -> new MonitoringOverviewScene());
    }

    @Override
    public boolean navigateTo(@NotNull String name) {
        return sceneManager.navigateTo(name);
    }

    @Override
    public void navigateToDefault() {
        SceneNavigator.getInstance().navigateTo(SceneNames.HomeScenes.MonitoringScenes.OVERVIEW_SCENE);
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
    }
}
