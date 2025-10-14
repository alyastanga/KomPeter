package com.github.ragudos.kompeter.app.desktop.scenes.auth;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.factory.TextFieldFactory;
import com.github.ragudos.kompeter.app.desktop.listeners.ButtonSceneNavigationActionListener;
import com.github.ragudos.kompeter.app.desktop.listeners.EnterKeyListener;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.constants.StringLimits;
import com.github.ragudos.kompeter.utilities.validator.EmailValidator;
import com.github.ragudos.kompeter.utilities.validator.PasswordValidator;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class SignUpAuthScene implements Scene {
    public static final String SCENE_NAME = "sign-up";

    private final JPanel view = new JPanel();

    private EnterKeyListener inputKeyEnterListener = new EnterKeyListener(this::handleInputEnterKey);

    private final JTextField emailInput = TextFieldFactory.createTextField("Email", JTextField.CENTER,
            inputKeyEnterListener);
    private final JLabel emailInputError = new JLabel();

    private final JTextField displayNameInput = TextFieldFactory.createTextField("Display Name", JTextField.CENTER,
            inputKeyEnterListener);
    private final JLabel displayNameInputError = new JLabel();

    private final JTextField firstNameInput = TextFieldFactory.createTextField("First Name", JTextField.CENTER,
            inputKeyEnterListener);
    private final JLabel firstNameInputError = new JLabel();

    private final JTextField lastNameInput = TextFieldFactory.createTextField("Last Name", JTextField.CENTER,
            inputKeyEnterListener);
    private final JLabel lastNameInputError = new JLabel();

    private final JPasswordField passwordInput = TextFieldFactory.createPasswordField(
            "Password", JPasswordField.CENTER, inputKeyEnterListener);
    private final JLabel passwordInputError = new JLabel();

    private final JPasswordField confirmPasswordInput = TextFieldFactory.createPasswordField(
            "Confirm Password", JPasswordField.CENTER, inputKeyEnterListener);
    private final JLabel confirmPasswordInputError = new JLabel();

    private final JButton submitButton = new JButton("Sign up");

    private final JButton navigateToLoginButton = new JButton("Already have an account");

    private final AtomicBoolean busy = new AtomicBoolean(false);

    public SignUpAuthScene() {
        onCreate();
    }

    private void handleSubmit(ActionEvent ev) {
        signUp(true);
    }

    private void handleInputEnterKey(KeyEvent e) {
        Object source = e.getSource();

        if (source.equals(emailInput) && validateEmail()) {
            SwingUtilities.invokeLater(
                    () -> {
                        emailInputError.setText("");
                        emailInput.putClientProperty("JComponent.outline", null);
                    });
            goToNearestEmptyFieldOrSignUp();
        } else if (source.equals(displayNameInput) && validateDisplayName()) {
            SwingUtilities.invokeLater(
                    () -> {
                        displayNameInputError.setText("");
                        displayNameInput.putClientProperty("JComponent.outline", null);
                    });
            goToNearestEmptyFieldOrSignUp();
        } else if (source.equals(firstNameInput) && validateFirstName()) {
            SwingUtilities.invokeLater(
                    () -> {
                        firstNameInputError.setText("");
                        firstNameInput.putClientProperty("JComponent.outline", null);
                    });
            goToNearestEmptyFieldOrSignUp();
        } else if (source.equals(lastNameInput) && validateLastName()) {
            SwingUtilities.invokeLater(
                    () -> {
                        lastNameInputError.setText("");
                        lastNameInput.putClientProperty("JComponent.outline", null);
                    });
            goToNearestEmptyFieldOrSignUp();
        } else if (source.equals(passwordInput) && validatePassword()) {
            SwingUtilities.invokeLater(
                    () -> {
                        passwordInputError.setText("");
                        passwordInput.putClientProperty("JComponent.outline", null);
                    });
            goToNearestEmptyFieldOrSignUp();
        } else if (source.equals(confirmPasswordInput)) {
            SwingUtilities.invokeLater(
                    () -> {
                        confirmPasswordInputError.setText("");
                        confirmPasswordInput.putClientProperty("JComponent.outline", null);
                    });
            goToNearestEmptyFieldOrSignUp();
        }
    }

    private void goToNearestEmptyFieldOrSignUp() {
        JTextField nearestEmtpyField = getNearestEmptyField();

        if (nearestEmtpyField != null) {
            SwingUtilities.invokeLater(
                    () -> {
                        nearestEmtpyField.requestFocusInWindow();
                    });
        } else {
            signUp(false);
        }
    }

    private JTextField getNearestEmptyField() {
        if (emailInput.getText().isEmpty()) {
            return emailInput;
        } else if (displayNameInput.getText().isEmpty()) {
            return displayNameInput;
        } else if (firstNameInput.getText().isEmpty()) {
            return firstNameInput;
        } else if (lastNameInput.getText().isEmpty()) {
            return lastNameInput;
        } else if (passwordInput.getPassword().length == 0) {
            return passwordInput;
        }

        return null;
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

    private boolean validateLastName() {
        final String lastName = lastNameInput.getText();

        if (lastName.length() < StringLimits.LAST_NAME.min()
                || lastName.length() > StringLimits.LAST_NAME.max()) {
            SwingUtilities.invokeLater(
                    () -> {
                        lastNameInputError.setText(
                                HtmlUtils.wrapInHtml(
                                        String.format(
                                                "Display name must be >= %s and <= %s",
                                                StringLimits.LAST_NAME.min(), StringLimits.LAST_NAME.max())));
                        lastNameInput.putClientProperty("JComponent.outline", "error");
                    });

            return false;
        }

        return true;
    }

    private boolean validateFirstName() {
        final String firstName = firstNameInput.getText();

        if (firstName.length() < StringLimits.FIRST_NAME.min()
                || firstName.length() > StringLimits.FIRST_NAME.max()) {
            SwingUtilities.invokeLater(
                    () -> {
                        firstNameInputError.setText(
                                HtmlUtils.wrapInHtml(
                                        String.format(
                                                "Display name must be >= %s and <= %s",
                                                StringLimits.FIRST_NAME.min(), StringLimits.FIRST_NAME.max())));
                        firstNameInput.putClientProperty("JComponent.outline", "error");
                    });

            return false;
        }

        return true;
    }

    private boolean validateDisplayName() {
        final String displayName = displayNameInput.getText();

        if (displayName.length() < StringLimits.DISPLAY_NAME.min()
                || displayName.length() > StringLimits.DISPLAY_NAME.max()) {
            SwingUtilities.invokeLater(
                    () -> {
                        displayNameInputError.setText(
                                HtmlUtils.wrapInHtml(
                                        String.format(
                                                "Display name must be >= %s and <= %s",
                                                StringLimits.DISPLAY_NAME.min(), StringLimits.DISPLAY_NAME.max())));
                        displayNameInput.putClientProperty("JComponent.outline", "error");
                    });

            return false;
        }

        return true;
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

    private void signUp(boolean shouldValidate) {
        SwingUtilities.invokeLater(this::clearErrors);

        busy.set(true);

        try {
            if (shouldValidate
                    && (!validateEmail()
                            | !validateDisplayName()
                            | !validateFirstName()
                            | !validateLastName()
                            | !validatePassword())) {
                return;
            }

            /*
             * Authentication.signUp(displayNameInput.getText(), firstNameInput.getText(),
             * lastNameInput.getText(), emailInput.getText(), passwordInput.getPassword(),
             * confirmPasswordInput.getPassword()); ;
             */
            SwingUtilities.invokeLater(() -> clearInputs());
            SwingUtilities.invokeLater(
                    () -> {
                        JOptionPane.showMessageDialog(
                                view,
                                "You have been registered successfully. Please login.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    });

            SceneNavigator.getInstance().navigateTo(SceneNames.AuthScenes.SIGN_IN_AUTH_SCENE);
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
        emailInput.putClientProperty("JComponent.outline", null);
        emailInputError.setText("");
        displayNameInput.putClientProperty("JComponent.outline", null);
        displayNameInputError.setText("");
        firstNameInput.putClientProperty("JComponent.outline", null);
        firstNameInputError.setText("");
        lastNameInput.putClientProperty("JComponent.outline", null);
        lastNameInputError.setText("");
        passwordInput.putClientProperty("JComponent.outline", null);
        passwordInputError.setText("");
        confirmPasswordInput.putClientProperty("JComponent.outline", null);
        confirmPasswordInputError.setText("");
    }

    private void clearInputs() {
        emailInput.setText("");
        displayNameInput.setText("");
        firstNameInput.setText("");
        lastNameInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");

        clearErrors();
    }

    @Override
    public void onCreate() {
        view.setLayout(
                new MigLayout("insets 0, gapy 28px, flowy, fillx", "[grow,center]", "[grow,center]"));

        /** TITLE * */
        JPanel titleContainer = new JPanel();
        JLabel title = new JLabel(HtmlUtils.wrapInHtml("<h1>KOMPETER</h1>"));
        JLabel subtitle = new JLabel(
                HtmlUtils.wrapInHtml("<h2 align=\"justify\">Computer Parts<br>& Accesories</h2>"));

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h0");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h1");

        titleContainer.setLayout(
                new MigLayout("insets 0, gapx 16px, wrap, flowx", "[right][left]", "[grow,top]"));

        titleContainer.add(title);
        titleContainer.add(subtitle);

        /** FORM * */
        JPanel formContainer = new JPanel();
        JPanel inputFormContainer = new JPanel();
        JPanel emailDisplayNameInputContainer = new JPanel();
        JPanel nameInputContainer = new JPanel();
        JPanel passwordInputContainer = new JPanel();

        submitButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        submitButton.addActionListener(this::handleSubmit);

        formContainer.setLayout(
                new MigLayout("insets 0, gapy 16px, flowy, fillx", "[grow,center]", "[center]"));
        inputFormContainer.setLayout(
                new MigLayout("insets 0, gapy 9px, flowy, fillx", "[grow,center]", "[center]"));

        emailDisplayNameInputContainer.setLayout(
                new MigLayout("insets 0, gapx 4px, flowx", "[grow,center]", "[center]"));
        nameInputContainer.setLayout(
                new MigLayout("insets 0, gapx 4px, flowx, fillx", "[grow,center]", "[center]"));
        passwordInputContainer.setLayout(
                new MigLayout("insets 0 ,flowy, fillx", "[grow,center]", "[center]"));

        JPanel emailInputContainer = new JPanel();
        JPanel displayNameInputContainer = new JPanel();

        emailInputError.setHorizontalAlignment(JLabel.CENTER);

        // displayNameInput.addKeyListener(displayNameInputEnterKeyListener);

        displayNameInputError.setHorizontalAlignment(JLabel.CENTER);

        emailInputContainer.setLayout(
                new MigLayout("insets 0, flowy, fillx", "[grow,center]", "[center]"));
        displayNameInputContainer.setLayout(
                new MigLayout("insets 0, flowy, fillx", "[grow,center]", "[center]"));

        emailInputContainer.add(emailInput, "growx");
        emailInputContainer.add(emailInputError, "growx");

        displayNameInputContainer.add(displayNameInput, "growx");
        displayNameInputContainer.add(displayNameInputError, "growx");

        emailDisplayNameInputContainer.add(emailInputContainer, "growx");
        emailDisplayNameInputContainer.add(displayNameInputContainer, "growx");

        JPanel firstNameInputContainer = new JPanel();
        JPanel lastNameInputContainer = new JPanel();

        firstNameInputError.setHorizontalAlignment(JLabel.CENTER);

        lastNameInputError.setHorizontalAlignment(JLabel.CENTER);

        firstNameInputContainer.setLayout(
                new MigLayout("insets 0, flowy, fillx", "[grow,center]", "[center]"));
        lastNameInputContainer.setLayout(
                new MigLayout("insets 0, flowy, fillx", "[grow,center]", "[center]"));

        firstNameInputContainer.add(firstNameInput, "growx");
        firstNameInputContainer.add(firstNameInputError, "growx");

        lastNameInputContainer.add(lastNameInput, "growx");
        lastNameInputContainer.add(lastNameInputError, "growx");

        nameInputContainer.add(firstNameInputContainer, "growx");
        nameInputContainer.add(lastNameInputContainer, "growx");

        passwordInputError.setHorizontalAlignment(JLabel.CENTER);

        passwordInputContainer.add(passwordInput, "growx");
        passwordInputContainer.add(passwordInputError, "growx");

        /** NAVIGATION * */
        JPanel navigationButtonsContainer = new JPanel();

        navigateToLoginButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
        navigateToLoginButton.setActionCommand(SceneNames.AuthScenes.SIGN_IN_AUTH_SCENE);
        navigateToLoginButton.addActionListener(new ButtonSceneNavigationActionListener());

        navigationButtonsContainer.setLayout(
                new MigLayout("insets 0, flowy, fillx", "[grow,center]", "[center]"));

        navigationButtonsContainer.add(navigateToLoginButton, "growx");

        inputFormContainer.add(emailDisplayNameInputContainer, "growx");
        inputFormContainer.add(nameInputContainer, "growx");
        inputFormContainer.add(passwordInputContainer, "growx");
        inputFormContainer.add(submitButton, "growx");

        formContainer.add(inputFormContainer, "growx");
        formContainer.add(navigationButtonsContainer, "growx");

        JPanel scrollPaneContent = new JPanel();
        JScrollPane scrollPane = new JScrollPane(scrollPaneContent);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        scrollPaneContent.setLayout(
                new MigLayout("insets 0 ,gapy 28px, flowy, fillx", "[grow,center]", "[grow,bottom][grow,top]"));

        scrollPaneContent.add(titleContainer);
        scrollPaneContent.add(formContainer, "growx, width ::350px");

        view.add(scrollPane, "grow");
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
    public void onDestroy() {
        submitButton.removeActionListener(this::handleSubmit);
        navigateToLoginButton.removeActionListener(ButtonSceneNavigationActionListener.LISTENER);

        firstNameInput.removeKeyListener(inputKeyEnterListener);
        displayNameInput.removeKeyListener(inputKeyEnterListener);
        lastNameInput.removeKeyListener(inputKeyEnterListener);
        emailInput.removeKeyListener(inputKeyEnterListener);
        passwordInput.removeKeyListener(inputKeyEnterListener);
        confirmPasswordInput.removeKeyListener(inputKeyEnterListener);

        view.removeAll();
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
