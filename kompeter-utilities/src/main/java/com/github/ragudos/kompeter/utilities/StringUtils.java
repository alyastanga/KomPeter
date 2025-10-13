package com.github.ragudos.kompeter.utilities;

import org.jetbrains.annotations.NotNull;

public final class StringUtils {
    public static @NotNull String[] splitTrim(@NotNull String input, @NotNull String delimiter) {
        return input.split("\\s*" + delimiter + "\\s*");
    }
}
