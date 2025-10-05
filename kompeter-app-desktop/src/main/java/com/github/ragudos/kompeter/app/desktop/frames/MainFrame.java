package com.github.ragudos.kompeter.app.desktop.frames;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.navigation.StaticSceneManager;
import com.github.ragudos.kompeter.app.desktop.scenes.auth.MainAuthScene;
import com.github.ragudos.kompeter.utilities.constants.Metadata;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
    private class MainFrameWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            // TODO: Cleanup operations
            dispose();
        }
    }

    private final MainFrameWindowListener windowListener = new MainFrameWindowListener();

    public MainFrame() {
        addWindowListener(windowListener);

        setTitle(Metadata.APP_TITLE + " " + Metadata.APP_VERSION);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        SceneManager sceneManager = new StaticSceneManager();
        SceneNavigator sceneNavigator = SceneNavigator.getInstance();

        sceneNavigator.initialize(sceneManager);

        sceneManager.registerScene(MainAuthScene.SCENE_NAME, () -> new MainAuthScene(), MainAuthScene.SCENE_GUARD);

        sceneManager.navigateTo(MainAuthScene.SCENE_NAME);

        pack();
        setLocationRelativeTo(null);
    }
}
