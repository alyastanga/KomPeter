package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.listeners.ButtonSceneNavigationActionListener;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jetbrains.annotations.NotNull;

public class SignInAuthScene implements Scene {
    public static final String SCENE_NAME = "sign-in";

    private final JPanel view = new JPanel();

    private final JTextField emailInput = new JTextField();
    private final JLabel emailInputError = new JLabel();
    private final JPasswordField passwordInput = new JPasswordField();
    private final JLabel passwordInputError = new JLabel();

    private class SignInButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public SignInAuthScene() {
        onCreate();
    }

    @Override
    public void onCreate() {
        view.setLayout(new MigLayout("gapy 28px, flowy, fillx", "[grow,center]", "[center]"));

        /** TITLE **/
        JPanel titleContainer = new JPanel();
        JLabel title = new JLabel(HtmlUtils.wrapInHtml("<h1>KOMPETER</h1>"));
        JLabel subtitle = new JLabel(HtmlUtils.wrapInHtml("<h2>Computer Parts<br>& Accesories</h2>"));

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h0");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h1");

        titleContainer.setLayout(new MigLayout("gapx 16px, wrap, flowx", "[right][left]", "[center]"));

        titleContainer.add(title);
        titleContainer.add(subtitle);

        /** FORM **/
        JPanel formContainer = new JPanel();

        /** INPUT **/
        JPanel inputFormContainer = new JPanel();
        JPanel emailInputContainer = new JPanel();
        JPanel passwordInputContainer = new JPanel();
        JButton submitButton = new JButton("Sign in");

        submitButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        submitButton.addActionListener(new SignInButtonListener());

        emailInput.setHorizontalAlignment(JTextField.CENTER);
        emailInput.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email");

        passwordInput.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        passwordInput.setHorizontalAlignment(JTextField.CENTER);

        emailInputContainer.setLayout(new MigLayout("flowy, fillx", "[grow,center]", "[center]"));

        emailInputContainer.add(emailInput, "growx");
        emailInputContainer.add(emailInputError);

        passwordInputContainer.setLayout(new MigLayout("flowy, fillx", "[grow,center]", "[center]"));

        passwordInputContainer.add(passwordInput, "growx");
        passwordInputContainer.add(passwordInputError);

        inputFormContainer.setLayout(new MigLayout("gapy 9px, flowy, fillx", "[grow,center]", "[center]"));

        inputFormContainer.add(emailInputContainer, "growx");
        inputFormContainer.add(passwordInputContainer, "growx");
        inputFormContainer.add(submitButton, "growx");

        /** NAVIGATION **/
        JPanel navigationButtonsContainer = new JPanel();
        JButton createAccountButton = new JButton("Create an account");

        createAccountButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");

        createAccountButton.setActionCommand(SceneNames.AuthScenes.SIGN_UP_AUTH_SCENE);

        createAccountButton.addActionListener(new ButtonSceneNavigationActionListener());

        navigationButtonsContainer.setLayout(new MigLayout("flowy, fillx", "[grow,center]", "[center]"));

        navigationButtonsContainer.add(createAccountButton, "growx");

        formContainer.setLayout(new MigLayout("gapy 21px, flowy, fillx", "[grow,center]", "[center]"));

        formContainer.add(inputFormContainer, "growx");
        formContainer.add(createAccountButton, "growx");

        view.add(titleContainer);
        view.add(formContainer, "grow, width ::250px");
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
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
