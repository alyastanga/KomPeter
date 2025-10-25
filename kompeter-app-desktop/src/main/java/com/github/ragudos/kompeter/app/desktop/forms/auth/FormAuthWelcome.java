/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms.auth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.system.FormManager;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

import net.miginfocom.swing.MigLayout;

public class FormAuthWelcome extends Form {
    public FormAuthWelcome() {
        init();
    }

    private void createWelcome() {
        JPanel titleContainer = new JPanel(new MigLayout("gapx 9px"));
        JLabel title = new JLabel("KomPeter");
        JLabel subtitle = new JLabel(HtmlUtils.wrapInHtml("Computer Parts<br>& Accessories"));

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h00 primary");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1 primary");

        titleContainer.add(title);
        titleContainer.add(subtitle);

        JPanel buttonContainer = new JPanel(new MigLayout("gapx 48px, al center center"));
        JButton signInButton = new JButton("Sign in");
        JButton signUpButton = new JButton("Sign up");

        signInButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        signUpButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        buttonContainer.add(signInButton);
        buttonContainer.add(signUpButton);

        signInButton.addActionListener(new SignInButtonActionListener());
        signUpButton.addActionListener(new SignUpButtonActionListener());

        add(titleContainer, "growx");
        add(buttonContainer, "growx");
    }

    private void init() {
        setLayout(new MigLayout("flowy, gapy 18px, al center center"));

        createWelcome();
    }

    private class SignInButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FormManager.showAuthForm(new FormAuthLogin());
        }
    }

    private class SignUpButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FormManager.showAuthForm(new FormAuthRegister());
        }
    }
}
