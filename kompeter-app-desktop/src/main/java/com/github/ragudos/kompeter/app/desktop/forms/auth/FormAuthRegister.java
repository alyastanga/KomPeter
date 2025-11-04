/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms.auth;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.system.FormManager;
import com.github.ragudos.kompeter.auth.Authentication;
import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.constants.StringLimits;
import com.github.ragudos.kompeter.utilities.validator.EmailValidator;
import com.github.ragudos.kompeter.utilities.validator.PasswordValidator;

import net.miginfocom.swing.MigLayout;
import raven.extras.SlidePane;
import raven.extras.SlidePaneTransition;
import raven.modal.component.DropShadowBorder;
import raven.modal.slider.PanelSlider.SliderCallback;

public class FormAuthRegister extends Form {
    private Icon cachedRegisterButtonIcon;
    private JPanel contentContainer;

    private final AtomicInteger currentStep;

    private JLabel displayNameError;
    private JLabel displayNameLabel;
    private JTextField displayNameTextField;
    private JLabel emailError;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel firstNameError;
    private JLabel firstNameLabel;
    private JTextField firstNameTextField;
    private final AtomicBoolean isBusy;
    private JLabel lastNameError;
    private JLabel lastNameLabel;
    private JTextField lastNameTextField;
    private JButton loginButton;
    private SlidePane paneSlider;
    private JLabel passwordError;
    private JLabel passwordLabel;
    private JPasswordField passwordTextField;
    private JButton previousStepButton;
    private JButton registerButton;
    private JPanel step1Panel;
    private JPanel step2Panel;
    private JPanel step3Panel;
    private JLabel stepLabel;

    public FormAuthRegister() {
        currentStep = new AtomicInteger(1);
        isBusy = new AtomicBoolean(false);

        init();
    }

    @Override
    public void formAfterOpen() {
        firstNameTextField.requestFocusInWindow();
    }

    private void applyShadowBorder(final JPanel panel) {
        if (panel != null) {
            panel.setBorder(new DropShadowBorder(new Insets(4, 8, 12, 8), 1, 25));
        }
    }

    private void clearStep1Errors() {
        firstNameTextField.putClientProperty("JComponent.outline", null);
        lastNameTextField.putClientProperty("JComponent.outline", null);
        firstNameError.setText("");
        lastNameError.setText("");
    }

    private void clearStep2Errors() {
        displayNameTextField.putClientProperty("JComponent.outline", null);
        emailTextField.putClientProperty("JComponent.outline", null);
        displayNameError.setText("");
        emailError.setText("");
    }

    private void clearStep3Errors() {
        passwordTextField.putClientProperty("JComponent.outline", null);
        passwordError.setText("");
    }

    private void createRegister() {
        paneSlider = new SlidePane();

        final JPanel container = new JPanel(new BorderLayout()) {
            @Override
            public void updateUI() {
                super.updateUI();
                applyShadowBorder(this);
            }
        };

        container.setOpaque(false);
        applyShadowBorder(container);

        contentContainer = new JPanel(new MigLayout("fillx, wrap, insets 35 35 25 35", "[fill, 300]"));

        final JLabel title = new JLabel("Welcome!");
        final JLabel description = new JLabel("Sign up to create an account.");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2 primary");
        description.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 muted");

        contentContainer.add(title);
        contentContainer.add(description);

        stepLabel = new JLabel(String.format("Step: %s", currentStep.get()));

        step1Panel = new JPanel(new MigLayout("fillx, wrap, insets 0", "[fill, 300]"));
        step2Panel = new JPanel(new MigLayout("fillx, wrap, insets 0", "[fill, 300]"));
        step3Panel = new JPanel(new MigLayout("fillx, wrap, insets 0", "[fill, 300]"));

        firstNameLabel = new JLabel("First Name*");
        firstNameTextField = new JTextField();
        firstNameError = new JLabel();
        lastNameLabel = new JLabel("Last Name*");
        lastNameTextField = new JTextField();
        lastNameError = new JLabel();
        displayNameLabel = new JLabel("Display Name*");
        displayNameTextField = new JTextField();
        displayNameError = new JLabel();
        emailLabel = new JLabel("Email*");
        emailTextField = new JTextField();
        emailError = new JLabel();
        passwordLabel = new JLabel("Password*");
        passwordTextField = new JPasswordField();
        passwordError = new JLabel();
        previousStepButton = new JButton("Back", new SVGIconUIColor("move-left.svg", 0.75f, "foreground.muted"));
        registerButton = new JButton("Continue", new SVGIconUIColor("move-right.svg", 0.75f, "foreground.primary"));
        loginButton = new JButton("I already have an account");

        stepLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold;");

        firstNameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your first name");
        lastNameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your last name");
        displayNameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your display name");
        emailTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        passwordTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        firstNameError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        lastNameError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        displayNameError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        emailError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        passwordError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");

        firstNameError.putClientProperty(FlatClientProperties.STYLE, "font:9;");
        lastNameError.putClientProperty(FlatClientProperties.STYLE, "font:9;");
        displayNameError.putClientProperty(FlatClientProperties.STYLE, "font:9;");
        emailError.putClientProperty(FlatClientProperties.STYLE, "font:9;");
        passwordError.putClientProperty(FlatClientProperties.STYLE, "font:9;");

        previousStepButton.setIconTextGap(16);
        previousStepButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        previousStepButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        registerButton.setIconTextGap(16);
        registerButton.setHorizontalTextPosition(SwingConstants.LEFT);
        registerButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        loginButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "link");

        container.putClientProperty(FlatClientProperties.STYLE, "[dark]:tint($Panel.background, 1%);");
        contentContainer.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        paneSlider.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        step1Panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        step2Panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        step3Panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");

        contentContainer.add(stepLabel, "gapy 25");
        contentContainer.add(paneSlider, "width 100%, gapy 10");

        paneSlider.addSlide(step1Panel);

        step1Panel.add(firstNameLabel);
        step1Panel.add(firstNameTextField);
        step1Panel.add(firstNameError, "gapy 2");

        step1Panel.add(lastNameLabel, "gapy 5");
        step1Panel.add(lastNameTextField);
        step1Panel.add(lastNameError, "gapy 2");

        step2Panel.add(displayNameLabel);
        step2Panel.add(displayNameTextField);
        step2Panel.add(displayNameError, "gapy 2");

        step2Panel.add(emailLabel, "gapy 5");
        step2Panel.add(emailTextField);
        step2Panel.add(emailError, "gapy 2");

        step3Panel.add(passwordLabel);
        step3Panel.add(passwordTextField);
        step3Panel.add(passwordError, "gapy 2");

        contentContainer.add(registerButton, "gapy 20");
        contentContainer.add(loginButton, "gapy 35");

        container.add(contentContainer);
        add(container);

        firstNameTextField.addKeyListener(new EnterKeyListener());
        lastNameTextField.addKeyListener(new EnterKeyListener());
        displayNameTextField.addKeyListener(new EnterKeyListener());
        emailTextField.addKeyListener(new EnterKeyListener());
        passwordTextField.addKeyListener(new EnterKeyListener());
        previousStepButton.addActionListener(new PreviousStepButtonListener(this));
        loginButton.addActionListener(new LoginButtonActionListener());
        registerButton.addActionListener(new RegisterButtonActionListener(this));
    }

    private void init() {
        setLayout(new MigLayout("al center center"));

        createRegister();
    }

    private boolean validateDisplayName() {
        final String displayName = displayNameTextField.getText();

        if (displayName.length() < StringLimits.DISPLAY_NAME.min()
                || displayName.length() > StringLimits.DISPLAY_NAME.max()) {
            displayNameTextField.putClientProperty("JComponent.outline", "error");
            displayNameError.setText(HtmlUtils.wrapInHtml(String.format("Display name must be >= %s and <= %s",
                    StringLimits.DISPLAY_NAME.min(), StringLimits.DISPLAY_NAME.max())));

            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        final String email = emailTextField.getText();

        if (!EmailValidator.isEmailValid(email, EmailValidator.EMAIL_REGEX)) {
            emailTextField.putClientProperty("JComponent.outline", "error");
            emailError.setText("Invalid email");

            return false;
        }

        return true;
    }

    private boolean validateFirstName() {
        final String firstName = firstNameTextField.getText();

        if (firstName.length() < StringLimits.FIRST_NAME.min() || firstName.length() > StringLimits.FIRST_NAME.max()) {
            firstNameTextField.putClientProperty("JComponent.outline", "error");
            firstNameError.setText(HtmlUtils.wrapInHtml(String.format("Display name must be >= %s and <= %s",
                    StringLimits.FIRST_NAME.min(), StringLimits.FIRST_NAME.max())));

            return false;
        }

        return true;
    }

    private boolean validateLastName() {
        final String lastName = lastNameTextField.getText();

        if (lastName.length() < StringLimits.LAST_NAME.min() || lastName.length() > StringLimits.LAST_NAME.max()) {
            lastNameTextField.putClientProperty("JComponent.outline", "error");
            lastNameError.setText(HtmlUtils.wrapInHtml(String.format("Display name must be >= %s and <= %s",
                    StringLimits.LAST_NAME.min(), StringLimits.LAST_NAME.max())));

            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        final char[] password = passwordTextField.getPassword();

        if (!PasswordValidator.isPasswordValid(password, PasswordValidator.STRONG_PASSWORD)) {
            passwordTextField.putClientProperty("JComponent.outline", "error");
            passwordError.setText(HtmlUtils.wrapInHtml(PasswordValidator.STRONG_PASSWORD_ERROR_MESSAGE));

            return false;
        }

        return true;
    }

    private class EnterKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(final KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                return;
            }

            final Object source = e.getSource();

            if (source == firstNameTextField && validateFirstName()) {
                firstNameTextField.putClientProperty("JComponent.outline", null);
                firstNameError.setText("");

                if (lastNameTextField.getText().isEmpty()) {
                    lastNameTextField.requestFocusInWindow();
                } else {
                    registerButton.doClick();
                }
            } else if (source == lastNameTextField && validateLastName()) {
                lastNameTextField.putClientProperty("JComponent.outline", null);
                lastNameError.setText("");

                if (firstNameTextField.getText().isEmpty()) {
                    firstNameTextField.requestFocusInWindow();
                } else {
                    registerButton.doClick();
                }
            } else if (source == displayNameTextField && validateDisplayName()) {
                displayNameTextField.putClientProperty("JComponent.outline", null);
                displayNameError.setText("");

                if (emailTextField.getText().isEmpty()) {
                    emailTextField.requestFocusInWindow();
                } else {
                    registerButton.doClick();
                }
            } else if (source == emailTextField && validateEmail()) {
                emailTextField.putClientProperty("JComponent.outline", null);
                emailError.setText("");

                if (displayNameTextField.getText().isEmpty()) {
                    displayNameTextField.requestFocusInWindow();
                } else {
                    registerButton.doClick();
                }
            } else if (source == passwordTextField && validatePassword()) {
                passwordTextField.putClientProperty("JComponent.outline", null);
                passwordError.setText("");

                registerButton.doClick();
            }
        }
    }

    private class LoginButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            FormManager.showAuthForm(new FormAuthLogin());
        }
    }

    private class PreviousStepButtonListener implements ActionListener {
        private final JPanel owner;

        public PreviousStepButtonListener(final JPanel owner) {
            this.owner = owner;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isBusy.get()) {
                return;
            }

            switch (currentStep.get()) {
                case 1 :
                    stepOne();
                    break;
                case 2 :
                    stepTwo();
                    break;
                case 3 :
                    stepThree();
                    break;
                default :
                    JOptionPane.showMessageDialog(owner, "Something went wrong");
            }
        }

        private void stepOne() {
            // no op, shouldnt be reached
        }

        private void stepThree() {
            isBusy.set(true);

            registerButton.setIcon(cachedRegisterButtonIcon);
            registerButton.setText("Continue");

            paneSlider.addSlide(step2Panel, SlidePaneTransition.create(SlidePaneTransition.Type.BACK),
                    new SliderCallback() {
                        @Override
                        public void complete() {
                            stepLabel.setText(String.format("Step: %s", currentStep.decrementAndGet()));
                            emailTextField.requestFocusInWindow();
                            isBusy.set(false);
                        }
                    });
        }

        private void stepTwo() {
            isBusy.set(true);

            contentContainer.remove(previousStepButton);
            contentContainer.remove(registerButton);
            contentContainer.add(registerButton, "gapy 20", 4);

            contentContainer.repaint();
            contentContainer.revalidate();

            paneSlider.addSlide(step1Panel, SlidePaneTransition.create(SlidePaneTransition.Type.BACK),
                    new SliderCallback() {
                        @Override
                        public void complete() {
                            stepLabel.setText(String.format("Step: %s", currentStep.decrementAndGet()));
                            lastNameTextField.requestFocusInWindow();
                            isBusy.set(false);
                        }
                    });
        }
    }

    private class RegisterButtonActionListener implements ActionListener {
        private final JPanel owner;

        public RegisterButtonActionListener(final JPanel owner) {
            this.owner = owner;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isBusy.get()) {
                return;
            }

            switch (currentStep.get()) {
                case 1 :
                    stepOne();
                    break;
                case 2 :
                    stepTwo();
                    break;
                case 3 :
                    stepThree();
                    break;
                default :
                    JOptionPane.showMessageDialog(owner, "Something went wrong");
            }
        }

        private void stepOne() {
            clearStep1Errors();

            if (!validateFirstName() | !validateLastName()) {
                return;
            }

            isBusy.set(true);

            contentContainer.remove(registerButton);
            contentContainer.add(previousStepButton, "gapy 20", 4);
            contentContainer.add(registerButton, "gapy 5", 5);

            contentContainer.repaint();
            contentContainer.revalidate();

            paneSlider.addSlide(step2Panel, SlidePaneTransition.create(SlidePaneTransition.Type.FORWARD),
                    new SliderCallback() {
                        @Override
                        public void complete() {
                            stepLabel.setText(String.format("Step: %s", currentStep.incrementAndGet()));
                            displayNameTextField.requestFocusInWindow();
                            isBusy.set(false);
                        }
                    });
        }

        private void stepThree() {
            clearStep3Errors();

            if (!validatePassword()) {
                return;
            }

            isBusy.set(true);

            try {
                Authentication.signUp(displayNameTextField.getText(), firstNameTextField.getText(),
                        lastNameTextField.getText(), emailTextField.getText(), passwordTextField.getPassword());

                JOptionPane.showMessageDialog(owner, "Your account has been registered. Please sign in.",
                        "Registration Success :)", JOptionPane.INFORMATION_MESSAGE);

                SwingUtilities.invokeLater(() -> {
                    FormManager.showAuthForm(new FormAuthLogin());
                });
            } catch (final AuthenticationException err) {
                JOptionPane.showMessageDialog(owner, err.getMessage(), "Sign Up Failure :(", JOptionPane.ERROR_MESSAGE);
            } finally {
                isBusy.set(false);
            }
        }

        private void stepTwo() {
            clearStep2Errors();

            if (!validateDisplayName() | !validateEmail()) {
                return;
            }

            isBusy.set(true);

            cachedRegisterButtonIcon = registerButton.getIcon();
            registerButton.setIcon(null);
            registerButton.setText("Sign Up");

            paneSlider.addSlide(step3Panel, SlidePaneTransition.create(SlidePaneTransition.Type.FORWARD),
                    new SliderCallback() {
                        @Override
                        public void complete() {
                            stepLabel.setText(String.format("Step: %s", currentStep.incrementAndGet()));
                            passwordTextField.requestFocusInWindow();
                            isBusy.set(false);
                        }
                    });
        }
    }
}
