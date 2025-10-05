package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;

import net.miginfocom.swing.MigLayout;

public class WelcomeAuthScreen implements Scene {
    public static final String SCENE_NAME = "welcome";

    private final JPanel view = new JPanel();

    public WelcomeAuthScreen() {
        onCreate();
    }

    @Override
    public void onCreate() {
        view.setLayout(new MigLayout("", "[grow,center]", "[grow,bottom][grow,top]"));

        /** TITLE **/
        JPanel titleContainer = new JPanel();
        JLabel title = new JLabel("KOMPETER");
        JLabel subtitle = new JLabel("Computer Parths & Accesories");

        title.setFont(new Font("Jersey10", Font.BOLD, 32));

        titleContainer.setLayout(new MigLayout("", "[right]16px[left]", "[center]"));
        titleContainer.add(title, "");
        titleContainer.add(subtitle, "cell 1 0");

        /** BUTTONS **/
        JPanel buttonContainer = new JPanel();
        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");

        buttonContainer.setLayout(new MigLayout("", "[center][center]", "[grow,center]"));
        buttonContainer.add(signInButton);
        buttonContainer.add(signUpButton);

        view.add(titleContainer, "cell 0 0");
        view.add(buttonContainer, "cell 0 1");
    }

    @Override
    public void onHide() {
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onDestroy() {
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
