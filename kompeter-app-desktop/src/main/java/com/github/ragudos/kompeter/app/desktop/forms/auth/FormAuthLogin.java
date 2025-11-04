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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.system.FormManager;
import com.github.ragudos.kompeter.auth.Authentication;
import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.validator.EmailValidator;
import com.github.ragudos.kompeter.utilities.validator.PasswordValidator;

import net.miginfocom.swing.MigLayout;
import raven.modal.component.DropShadowBorder;

public class FormAuthLogin extends Form {
    private JLabel emailError;
    private JTextField emailTextField;
    private final AtomicBoolean isBusy;
    private JButton loginButton;
    private JLabel passwordError;
    private JPasswordField passwordTextField;
    private JButton registerButton;

    public FormAuthLogin() {
        isBusy = new AtomicBoolean(false);

        init();
    }

    @Override
    public void formAfterOpen() {
        emailTextField.requestFocusInWindow();
    }

    private void applyShadowBorder(final JPanel panel) {
        if (panel != null) {
            panel.setBorder(new DropShadowBorder(new Insets(4, 8, 12, 8), 1, 25));
        }
    }

    private void clearErrors() {
        emailError.setText("");
        passwordError.setText("");
    }

    private void createLogin() {
        final JPanel container = new JPanel(new BorderLayout()) {
            @Override
            public void updateUI() {
                super.updateUI();
                applyShadowBorder(this);
            }
        };

        container.setOpaque(false);
        applyShadowBorder(container);

        final JPanel contentContainer = new JPanel(new MigLayout("fillx, wrap, insets 35 35 25 35", "[fill, 300]"));

        final JLabel title = new JLabel("Welcome back!");
        final JLabel description = new JLabel("Please sign in to access your account.");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2 primary");
        description.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 muted");

        contentContainer.add(title);
        contentContainer.add(description);

        emailTextField = new JTextField();
        emailError = new JLabel();
        passwordTextField = new JPasswordField();
        passwordError = new JLabel();
        loginButton = new JButton("Sign in");
        registerButton = new JButton("I don't have an account yet.");

        emailTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        passwordTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        passwordTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        passwordTextField.putClientProperty("PasswordField.showCapsLock", true);

        emailError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        passwordError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        emailError.putClientProperty(FlatClientProperties.STYLE, "font:9;");
        passwordError.putClientProperty(FlatClientProperties.STYLE, "font:9;");

        registerButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "link");
        loginButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        container.putClientProperty(FlatClientProperties.STYLE, "[dark]:tint($Panel.background, 1%);");
        contentContainer.putClientProperty(FlatClientProperties.STYLE, "background:null;");

        contentContainer.add(new JLabel("Email*"), "gapy 25");
        contentContainer.add(emailTextField);
        contentContainer.add(emailError, "gapy 2");

        contentContainer.add(new JLabel("Password*"), "gapy 10");
        contentContainer.add(passwordTextField);
        contentContainer.add(passwordError, "gapy 2");

        contentContainer.add(loginButton, "gapy 20");
        contentContainer.add(registerButton, "gapy 35");

        container.add(contentContainer);

        add(container);

        emailTextField.addKeyListener(new EnterKeyListener());
        passwordTextField.addKeyListener(new EnterKeyListener());
        loginButton.addActionListener(new LoginButtonActionListener(this));
        registerButton.addActionListener(new RegisterButtonActionListener());
    }

    private void init() {
        setLayout(new MigLayout("al center center"));

        createLogin();
    }

    private boolean validateEmail() {
        final String email = emailTextField.getText();

        if (!EmailValidator.isEmailValid(email, EmailValidator.EMAIL_REGEX)) {
            emailTextField.putClientProperty(FlatClientProperties.OUTLINE_ERROR, true);
            emailError.setText("Invalid email");

            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        final char[] password = passwordTextField.getPassword();

        if (!PasswordValidator.isPasswordValid(password, PasswordValidator.STRONG_PASSWORD)) {
            passwordTextField.putClientProperty(FlatClientProperties.OUTLINE_ERROR, true);
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

            if (source == emailTextField && validateEmail()) {
                emailError.setText("");

                if (passwordTextField.getPassword().length == 0) {
                    passwordTextField.requestFocusInWindow();
                } else {
                    loginButton.doClick();
                }
            } else if (source == passwordTextField && validatePassword()) {
                passwordError.setText("");

                if (emailTextField.getText().isEmpty()) {
                    emailTextField.requestFocusInWindow();
                } else {
                    loginButton.doClick();
                }
            }
        }
    }

    private class LoginButtonActionListener implements ActionListener {
        private final JPanel owner;

        public LoginButtonActionListener(final JPanel owner) {
            this.owner = owner;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isBusy.get()) {
                return;
            }

            try {
                isBusy.set(true);

                clearErrors();

                if (!validateEmail() | !validatePassword()) {
                    return;
                }

                Authentication.signIn(emailTextField.getText(), passwordTextField.getPassword());

                SwingUtilities.invokeLater(() -> {
                    FormManager.login();
                });
            } catch (final AuthenticationException err) {
                JOptionPane.showMessageDialog(owner, err.getMessage(), "Sign In Failure :(", JOptionPane.ERROR_MESSAGE);
            } finally {
                isBusy.set(false);
            }
        }
    }

    private class RegisterButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            FormManager.showAuthForm(new FormAuthRegister());
        }
    }
}
