/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.configurations;

import com.github.ragudos.kompeter.utilities.constants.PropertyKey;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SearchHistory {
    private static final Logger LOGGER = KompeterLogger.getLogger(SearchHistory.class);

    public static String[] getRecentSearches(boolean favorite) {
        String searches =
                ApplicationConfig.getInstance()
                        .getConfig()
                        .getProperty(favorite ? PropertyKey.Search.RECENT_FAVORITE : PropertyKey.Search.RECENT);

        if (searches == null || searches.trim().isEmpty()) {
            return null;
        }

        return searches.trim().split(",");
    }

    public static void addRecentSearch(String value, boolean favorite) {
        String[] oldRecent = getRecentSearches(false);
        String[] oldFavorite = getRecentSearches(true);

        if (favorite) {
            if (oldRecent != null) {
                List<String> list = new ArrayList<String>(Arrays.asList(oldRecent));

                list.remove(value);

                try {
                    ApplicationConfig.getInstance()
                            .getConfig()
                            .setProperty(PropertyKey.Search.RECENT, String.join(",", list));
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to save recent search", e);
                }
            }

            if (oldFavorite != null) {
                List<String> list = new ArrayList<String>(Arrays.asList(oldRecent));

                list.remove(value);
                list.add(0, value);

                try {
                    ApplicationConfig.getInstance()
                            .getConfig()
                            .setProperty(PropertyKey.Search.RECENT_FAVORITE, String.join(",", list));
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to save recent search", e);
                }
            } else {
                try {
                    ApplicationConfig.getInstance()
                            .getConfig()
                            .setProperty(PropertyKey.Search.RECENT_FAVORITE, String.join(",", value));
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to save recent search", e);
                }
            }

            return;
        }

        if (oldFavorite != null) {
            List<String> list = new ArrayList<String>(Arrays.asList(oldFavorite));

            if (list.contains(value)) {
                return;
            }
        }

        if (oldRecent == null) {
            try {
                ApplicationConfig.getInstance()
                        .getConfig()
                        .setProperty(PropertyKey.Search.RECENT, String.join(",", value));
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to save recent search", e);
            }

            return;
        }

        List<String> list = new ArrayList<String>(Arrays.asList(oldRecent));

        list.remove(value);
        list.add(0, value);

        try {
            ApplicationConfig.getInstance()
                    .getConfig()
                    .setProperty(PropertyKey.Search.RECENT, String.join(",", list));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save recent search", e);
        }
    }

    public static void removeRecentSearch(String value, boolean favorite) {
        String[] oldRecentStrings = getRecentSearches(favorite);

        if (oldRecentStrings == null) {
            return;
        }

        List<String> list = new ArrayList<String>(Arrays.asList(oldRecentStrings));

        list.remove(value);

        try {
            ApplicationConfig.getInstance()
                    .getConfig()
                    .setProperty(
                            favorite ? PropertyKey.Search.RECENT_FAVORITE : PropertyKey.Search.RECENT,
                            String.join(",", list));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save recent search", e);
        }
    }
}
