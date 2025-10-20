/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.factory;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.assets.SVGIconUIColor;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;

public final class TextFieldFactory {
    public static JTextField createSearchTextField(final int alignment, final @NotNull Runnable clearCallback) {
        JTextField textField = new JTextField();

        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                AssetManager.getOrLoadIcon("search.svg", 0.75f, "TextField.placeholderForeground"));
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_CLEAR_CALLBACK, clearCallback);
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        textField.setHorizontalAlignment(JTextField.CENTER);

        return textField;
    }

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
