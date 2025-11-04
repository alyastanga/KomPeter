/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.inventory;

import java.util.Locale;

public enum ItemStatus {
    /** For sale */
    ACTIVE,
    /** Technically deleted */
    ARCHIVED,
    /** Not for sale */
    INACTIVE;

    public static ItemStatus fromString(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase(Locale.of("en"));

        if (s.equals("archived")) {
            return ARCHIVED;
        } else if (s.equals("inactive")) {
            return INACTIVE;
        } else if (s.equals("active")) {
            return ACTIVE;
        }

        return null;
    }

    public int priority() {
        return switch (this) {
            case ACTIVE -> 3;
            case INACTIVE -> 2;
            case ARCHIVED -> 1;
        };
    }
}
