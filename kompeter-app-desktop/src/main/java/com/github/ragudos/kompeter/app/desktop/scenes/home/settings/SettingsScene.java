package com.github.ragudos.kompeter.app.desktop.scenes.home.settings;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.app.desktop.scenes.home.settings.scenes.MainSettingsScene;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public final class SettingsScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "settings";

    private final JPanel view = new JPanel();

    private final SceneManager sceneManager = new StaticSceneManager();

    public SettingsScene() {
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

        sceneManager.registerScene(MainSettingsScene.SCENE_NAME, () -> new MainSettingsScene());
    }

    @Override
    public boolean navigateTo(@NotNull String name) {
        return sceneManager.navigateTo(name);
    }

    @Override
    public void navigateToDefault() {
        SceneNavigator.getInstance().navigateTo(SceneNames.HomeScenes.SettingsScenes.MAIN_SETTINGS_SCENE);
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
    }
}
