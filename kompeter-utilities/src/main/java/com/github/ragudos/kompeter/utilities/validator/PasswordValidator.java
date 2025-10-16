/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.validator;

import java.nio.CharBuffer;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public final class PasswordValidator {
    /**
     *
     *
     * <ul>
     *   <li>Has a minimum of 8 characters {8,}
     *   <li>At least one upper case English letter. {@code (?=.*?[A-Z])}
     *   <li>At least one lower case English letter. {@code (?=.*?[a-z])}
     *   <li>At least one digit. {@code (?=.*?[0-9])}
     *   <li>At least one special character.{@code (?=.*? [#?!@$%^&*-])}
     * </ul>
     */
    public static @NotNull final Pattern STRONG_PASSWORD =
            Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");

    public static @NotNull final String STRONG_PASSWORD_ERROR_MESSAGE =
            "Password must be 8+ characters with upper, lower, number, and special character.";

    public static boolean isPasswordValid(
            @NotNull final char[] password, @NotNull final Pattern regex) {
        return regex.matcher(CharBuffer.wrap(password)).find();
    }
}
