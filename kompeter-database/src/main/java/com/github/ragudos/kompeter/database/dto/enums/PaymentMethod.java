/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.enums;

import org.jetbrains.annotations.NotNull;

public enum PaymentMethod {
    CASH,
    GCASH,
    BANK_TRANSFER;

    public static PaymentMethod fromString(final @NotNull String name) {
        return switch (name) {
            case "cash" -> CASH;
            case "gcash" -> GCASH;
            case "bank_transfer" -> BANK_TRANSFER;
            default -> null;
        };
    }

    @Override
    public final String toString() {
        return switch (this) {
            case CASH -> "cash";
            case GCASH -> "gcash";
            case BANK_TRANSFER -> "bank_transfer";
        };
    }
}
