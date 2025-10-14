package com.github.ragudos.kompeter.app.desktop.scenes.home.profile;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneWithSubScenes;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.app.desktop.scenes.home.profile.scenes.EditProfileScene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.profile.scenes.ReadProfileScene;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class ProfileScene implements SceneWithSubScenes {
    public static final String SCENE_NAME = "profile";

    private final JPanel view = new JPanel();

    private final SceneManager sceneManager = new StaticSceneManager();

    public ProfileScene() {
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

        sceneManager.registerScene(ReadProfileScene.SCENE_NAME, () -> new ReadProfileScene());
        sceneManager.registerScene(EditProfileScene.SCENE_NAME, () -> new EditProfileScene());
    }

    @Override
    public boolean navigateTo(@NotNull String name) {
        return sceneManager.navigateTo(name);
    }

    @Override
    public void navigateToDefault() {
        SceneNavigator.getInstance().navigateTo(SceneNames.HomeScenes.ProfileScenes.READ_PROFILE_SCENE);
    }

    @Override
    public SceneManager sceneManager() {
        return sceneManager;
    }
}
