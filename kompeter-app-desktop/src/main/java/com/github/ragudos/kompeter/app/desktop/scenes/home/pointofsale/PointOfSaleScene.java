package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneGuard;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class PointOfSaleScene implements Scene {
    public static final String SCENE_NAME = "pos";
    public static final SceneGuard SCENE_GUARD =
            new SceneGuard() {
                @Override
                public boolean canAccess() {
                    return true;
                    // return SessionManager.getInstance().session().user().isClerk();
                }
            };

    private final JPanel view = new JPanel();

    public PointOfSaleScene() {
        onCreate();
    }

    @Override
    public void onCreate() {
        view.setLayout(new MigLayout());

        view.add(new JLabel("Hello"));
    }

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
