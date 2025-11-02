/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.utilities;

import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Validator {
    public static final boolean validateAreaField(final JTextArea field, final JLabel errorLabel,
            final Function<String, String> validator) {
        final String res = validator.apply(field.getText().trim());

        field.putClientProperty("JComponent.outline", res == null ? null : "error");
        errorLabel.setText(res == null ? "" : res);

        return res == null;
    }

    public static final boolean validateField(final JTextField field, final JLabel errorLabel,
            final Function<String, String> validator) {
        final String res = validator.apply(field.getText().trim());

        field.putClientProperty("JComponent.outline", res == null ? null : "error");
        errorLabel.setText(res == null ? "" : res);

        return res == null;
    }

    public static final boolean validatePasswordField(final JPasswordField field, final JLabel errorLabel,
            final Function<char[], String> validator) {
        final String res = validator.apply(field.getPassword());

        field.putClientProperty("JComponent.outline", res == null ? null : "error");
        errorLabel.setText(res == null ? "" : res);

        return res == null;
    }
}
