package com.github.ragudos.kompeter.app.desktop.components.factory;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.event.KeyListener;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jetbrains.annotations.NotNull;

public final class TextFieldFactory {
    public static JTextField createTextField(
            final @NotNull String placeholder, final int alignment, KeyListener keyListener) {
        JTextField textField = new JTextField();

        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.addKeyListener(keyListener);

        return textField;
    }

    public static JPasswordField createPasswordField(
            final @NotNull String placeholder, final int alignment, KeyListener keyListener) {
        JPasswordField textField = new JPasswordField();

        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.addKeyListener(keyListener);

        return textField;
    }
}
