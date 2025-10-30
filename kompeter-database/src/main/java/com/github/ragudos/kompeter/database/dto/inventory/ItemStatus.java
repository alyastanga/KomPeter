/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.inventory;

public enum ItemStatus {
    /** For sale */
    ACTIVE,
    /** Technically deleted */
    ARCHIVED,
    /** Not for sale */
    INACTIVE;

    public static ItemStatus fromString(String s) {
        if (s.equals("archived")) {
            return ARCHIVED;
        } else if (s.equals("inactive")) {
            return INACTIVE;
        } else if (s.equals("active")) {
            return ACTIVE;
        }

        return null;
    }
}
