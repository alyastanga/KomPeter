package com.github.ragudos.kompeter.app.desktop.navigation;

import org.jetbrains.annotations.NotNull;

public record ParsedSceneName(
        @NotNull String fullPath, @NotNull String parentSceneName, String subSceneName) {

    public static final String SEPARATOR = "/";

    public static ParsedSceneName parse(@NotNull String sceneName) {
        if (sceneName.isEmpty()) {
            return new ParsedSceneName("", "", null);
        }

        int index = sceneName.indexOf(SEPARATOR);

        if (index == -1) {
            return new ParsedSceneName(sceneName, sceneName, null);
        }

        String parentSceneName = sceneName.substring(0, index);
        String subSceneName = sceneName.substring(index + 1);

        return new ParsedSceneName(sceneName, parentSceneName, subSceneName);
    }
}
