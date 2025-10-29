/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities;

import java.text.NumberFormat;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;

public final class StringUtils {
    public static @NotNull String formatDouble(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.of("tl", "ph"));
        String formatted = formatter.format(amount);

        return formatted;
    }

    public static @NotNull String[] splitTrim(@NotNull String input, @NotNull String delimiter) {
        return input.split("\\s*" + delimiter + "\\s*");
    }

    public static @NotNull String upperCaseFirstLetters(@NotNull String input) {
        if (input == null || input.isEmpty())
            return input;

        String[] parts = input.split("([\\s\\-_]+)"); // split by space, -, or _
        StringBuilder result = new StringBuilder();

        int index = 0;
        for (String part : parts) {
            if (part.isEmpty())
                continue;

            // Capitalize first letter and append
            result.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1)
                result.append(part.substring(1).toLowerCase());

            // Add back the original delimiter if any
            if (index + part.length() < input.length()) {
                char next = input.charAt(index + part.length());
                if (next == ' ' || next == '-' || next == '_')
                    result.append(next);
            }

            index = input.indexOf(part, index) + part.length();
        }

        return result.toString();
    }
}
