/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.enums;

import org.jetbrains.annotations.NotNull;

public enum DiscountType {
    PERCENTAGE,
    FIXED;

    public static DiscountType fromString(@NotNull final String name) {
        return switch (name) {
            case "percentage" -> PERCENTAGE;
            case "fixed" -> FIXED;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case PERCENTAGE -> "percentage";
            case FIXED -> "fixed";
        };
    }
}
