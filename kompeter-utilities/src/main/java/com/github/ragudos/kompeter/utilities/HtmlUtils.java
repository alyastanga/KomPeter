package com.github.ragudos.kompeter.utilities;

import org.jetbrains.annotations.NotNull;

public final class HtmlUtils {
    public static @NotNull String escapeHtml(@NotNull final String input) {
        var escaped = new StringBuilder(input.length());

        for (var c : input.toCharArray()) {
            var ch = mapCharToHtmlEntity(c);
            escaped.append(ch);
        }

        return escaped.toString();
    }

    public static @NotNull String mapCharToHtmlEntity(@NotNull final char c) {
        return switch (c) {
            case '&' -> "&amp;";
            case '<' -> "&lt;";
            case '>' -> "&gt;";
            case '"' -> "&quot;";
            case '\'' -> "&#39;";
            default -> String.valueOf(c);
        };
    }

    /** Wraps a {@code String} in {@code <html>} tags */
    public static @NotNull String wrapInHtml(@NotNull final String input) {
        return "<html>" + input + "</html>";
    }
}
