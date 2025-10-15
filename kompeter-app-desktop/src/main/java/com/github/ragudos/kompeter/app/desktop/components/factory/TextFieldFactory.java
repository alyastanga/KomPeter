/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.factory;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;

public final class TextFieldFactory {
    public static JTextField createTextField(final @NotNull String placeholder, final int alignment) {
        JTextField textField = new JTextField();

        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        textField.setHorizontalAlignment(JTextField.CENTER);

        return textField;
    }

    public static JPasswordField createPasswordField(
            final @NotNull String placeholder, final int alignment) {
        JPasswordField textField = new JPasswordField();

        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        textField.setHorizontalAlignment(JTextField.CENTER);

        return textField;
    }
}
