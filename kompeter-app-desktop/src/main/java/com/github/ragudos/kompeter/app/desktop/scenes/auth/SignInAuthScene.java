package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.factory.TextFieldFactory;
import com.github.ragudos.kompeter.app.desktop.listeners.ButtonSceneNavigationActionListener;
import com.github.ragudos.kompeter.app.desktop.listeners.EnterKeyListener;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.validator.EmailValidator;
import com.github.ragudos.kompeter.utilities.validator.PasswordValidator;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class SignInAuthScene implements Scene {
    public static final String SCENE_NAME = "sign-in";

    private final EnterKeyListener inputEnterKeyListener =
            new EnterKeyListener(this::handleInputEnterKey);

    private final JPanel view = new JPanel();

    private final JTextField emailInput =
            TextFieldFactory.createTextField("Email", JTextField.CENTER, inputEnterKeyListener);
    private final JLabel emailInputError = new JLabel();

    private final JPasswordField passwordInput =
            TextFieldFactory.createPasswordField(
                    "Password", JPasswordField.CENTER, inputEnterKeyListener);
    private final JLabel passwordInputError = new JLabel();

    private final AtomicBoolean busy = new AtomicBoolean(false);

    public SignInAuthScene() {
        onCreate();
    }

    private void handleSubmit(ActionEvent e) {
        signIn(true);
    }

    private void handleInputEnterKey(KeyEvent e) {
        if (busy.get()) {
            return;
        }

        Object source = e.getSource();

        if (source.equals(emailInput) && validateEmail()) {
            SwingUtilities.invokeLater(
                    () -> {
                        emailInputError.setText("");
                        emailInput.putClientProperty("JComponent.outline", null);
                    });
            goToNearestEmptyFieldOrSignIn();
        } else if (source.equals(passwordInput) && validatePassword()) {
            SwingUtilities.invokeLater(
                    () -> {
                        passwordInputError.setText("");
                        passwordInput.putClientProperty("JComponent.outline", null);
                    });
            goToNearestEmptyFieldOrSignIn();
        }
    }

    private void goToNearestEmptyFieldOrSignIn() {
        JTextField nearestEmtpyField = getNearestEmptyField();

        if (nearestEmtpyField != null) {
            SwingUtilities.invokeLater(
                    () -> {
                        nearestEmtpyField.requestFocusInWindow();
                    });
        } else {
            signIn(false);
        }
    }

    private JTextField getNearestEmptyField() {
        if (emailInput.getText().isEmpty()) {
            return emailInput;
        } else if (passwordInput.getPassword().length == 0) {
            return passwordInput;
        }

        return null;
    }

    private boolean validateEmail() {
        if (!EmailValidator.isEmailValid(emailInput.getText(), EmailValidator.EMAIL_REGEX)) {
            SwingUtilities.invokeLater(
                    () -> {
                        emailInputError.setText(HtmlUtils.wrapInHtml("Invalid email"));
                        emailInput.putClientProperty("JComponent.outline", "error");
                    });

            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        if (!PasswordValidator.isPasswordValid(
                passwordInput.getPassword(), PasswordValidator.STRONG_PASSWORD)) {
            SwingUtilities.invokeLater(
                    () -> {
                        passwordInputError.setText(
                                HtmlUtils.wrapInHtml(PasswordValidator.STRONG_PASSWORD_ERROR_MESSAGE));
                        passwordInput.putClientProperty("JComponent.outline", "error");
                    });

            return false;
        }

        return true;
    }

    private void signIn(boolean shouldValidate) {
        if (busy.get()) {
            return;
        }

        SwingUtilities.invokeLater(this::clearErrors);

        busy.set(true);

        try {
            if (shouldValidate && (!validateEmail() | !validatePassword())) {
                return;
            }

            // Authentication.signIn(emailInput.getText(), passwordInput.getPassword());
            SceneNavigator.getInstance().navigateTo(SceneNames.HomeScenes.HOME_SCENE);
            /*
             * } catch (AuthenticationException e1) { SwingUtilities.invokeLater(() -> {
             * JOptionPane.showMessageDialog(view,
             * "We cannot sign you in at this moment. Sorry.", e1.getMessage(),
             * JOptionPane.ERROR_MESSAGE); });
             */
        } finally {
            busy.set(false);
        }
    }

    private void clearErrors() {
        emailInputError.setText("");
        emailInput.putClientProperty("JComponent.outline", null);
        passwordInput.putClientProperty("JComponent.outline", null);
        passwordInputError.setText("");
    }

    private void clearInputs() {
        emailInputError.setText("");
        passwordInput.setText("");

        clearErrors();
    }

    @Override
    public void onCreate() {
        view.setLayout(new MigLayout("insets 0", "[grow,center]", "[grow,center]"));

        /** TITLE * */
        JPanel titleContainer = new JPanel();
        JLabel title = new JLabel(HtmlUtils.wrapInHtml("<h1>KOMPETER</h1>"));
        JLabel subtitle =
                new JLabel(
                        HtmlUtils.wrapInHtml("<h2 align=\"justify\">Computer Parts<br>& Accesories</h2>"));

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h0");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h1");

        titleContainer.setLayout(
                new MigLayout("insets 0, gapx 16px, wrap, flowx", "[right][left]", "[center]"));

        titleContainer.add(title);
        titleContainer.add(subtitle);

        /** FORM * */
        JPanel formContainer = new JPanel();

        /** INPUT * */
        JPanel inputFormContainer = new JPanel();
        JPanel emailInputContainer = new JPanel();
        JPanel passwordInputContainer = new JPanel();
        JButton submitButton = new JButton("Sign in");

        submitButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        submitButton.addActionListener(this::handleSubmit);

        emailInput.setHorizontalAlignment(JTextField.CENTER);
        emailInput.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email");
        // emailInput.addKeyListener(emailInputEnterKeyListener);

        emailInputError.setHorizontalAlignment(JLabel.CENTER);

        passwordInput.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        passwordInput.setHorizontalAlignment(JTextField.CENTER);
        // passwordInput.addKeyListener(passwordInputEnterKeyListener);

        passwordInputError.setHorizontalAlignment(JLabel.CENTER);

        emailInputContainer.setLayout(
                new MigLayout("insets 0, flowy, fillx", "[grow,center]", "[center]"));

        emailInputContainer.add(emailInput, "growx");
        emailInputContainer.add(emailInputError);

        passwordInputContainer.setLayout(
                new MigLayout("insets 0, flowy, fillx", "[grow,center]", "[center]"));

        passwordInputContainer.add(passwordInput, "growx");
        passwordInputContainer.add(passwordInputError);

        inputFormContainer.setLayout(
                new MigLayout("insets 0, gapy 9px, flowy, fillx", "[grow,center]", "[center]"));

        inputFormContainer.add(emailInputContainer, "growx");
        inputFormContainer.add(passwordInputContainer, "growx");
        inputFormContainer.add(submitButton, "growx");

        /** NAVIGATION * */
        JPanel navigationButtonsContainer = new JPanel();
        JButton createAccountButton = new JButton("Create an account");

        createAccountButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");

        createAccountButton.setActionCommand(SceneNames.AuthScenes.SIGN_UP_AUTH_SCENE);

        createAccountButton.addActionListener(new ButtonSceneNavigationActionListener());

        navigationButtonsContainer.setLayout(
                new MigLayout("insets 0, flowy, fillx", "[grow,center]", "[center]"));

        navigationButtonsContainer.add(createAccountButton, "growx");

        formContainer.setLayout(
                new MigLayout("insets 0, gapy 21px, flowy, fillx", "[grow,center]", "[center]"));

        formContainer.add(inputFormContainer, "growx");
        formContainer.add(createAccountButton, "growx");

        JPanel scrollPaneContent = new JPanel();
        JScrollPane scrollPane = new JScrollPane(scrollPaneContent);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        scrollPaneContent.setLayout(
                new MigLayout("insets 0, gapy 28px, flowy, fillx", "[grow,center]", "[grow,center]"));

        scrollPaneContent.add(titleContainer);
        scrollPaneContent.add(formContainer, "grow, height :300px:, width ::350px");

        view.add(scrollPane, "grow");
    }

    @Override
    public boolean canHide() {
        return !busy.get();
    }

    @Override
    public void onShow() {
        emailInput.requestFocusInWindow();
    }

    @Override
    public void onHide() {
        clearInputs();
    }

    @Override
    public void onDestroy() {}

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
