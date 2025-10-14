package com.github.ragudos.kompeter.app.desktop.listeners;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ButtonSceneNavigationActionListener implements ActionListener {
    public static final ButtonSceneNavigationActionListener LISTENER =
            new ButtonSceneNavigationActionListener();

    @Override
    public void actionPerformed(ActionEvent e) {
        SceneNavigator.getInstance().navigateTo(e.getActionCommand());
    }
}
