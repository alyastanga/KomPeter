package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.listeners.ButtonSceneNavigationActionListener;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class WelcomeAuthScreen implements Scene {
    public static final String SCENE_NAME = "welcome";

    private final JPanel view = new JPanel();

    public WelcomeAuthScreen() {
        onCreate();
    }

    @Override
    public void onCreate() {
        view.setLayout(new MigLayout("", "[grow,center]", "[grow,bottom][grow,top]"));

        /** TITLE * */
        JPanel titleContainer = new JPanel();
        JLabel title = new JLabel(HtmlUtils.wrapInHtml("<h1>KOMPETER</h1>"));
        JLabel subtitle = new JLabel(HtmlUtils.wrapInHtml("<h2>Computer Parts<br>& Accesories</h2>"));

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h0");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h1");

        titleContainer.setLayout(new MigLayout("gapx 32px, wrap", "[right][left]", "[center]"));

        titleContainer.add(title, "cell 0 0");
        titleContainer.add(subtitle, "cell 1 0");

        /** BUTTONS * */
        JPanel buttonContainer = new JPanel();
        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");

        signInButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        signUpButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        signInButton.setActionCommand(SceneNames.AuthScenes.SIGN_IN_AUTH_SCENE);
        signUpButton.setActionCommand(SceneNames.AuthScenes.SIGN_UP_AUTH_SCENE);

        signInButton.addActionListener(new ButtonSceneNavigationActionListener());
        signUpButton.addActionListener(new ButtonSceneNavigationActionListener());

        buttonContainer.setLayout(new MigLayout("gapx 96px,wrap", "[right][left]", "[grow,center]"));

        buttonContainer.add(signInButton, "width :150px:");
        buttonContainer.add(signUpButton, "width :150px:");

        view.add(titleContainer, "cell 0 0");
        view.add(buttonContainer, "cell 0 1");
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
