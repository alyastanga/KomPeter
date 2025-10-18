/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.monitoring;

import java.sql.Timestamp;

/**
 * @author Hanz Mapua
 */
public record PurchaseUnitDto(
        Timestamp date, int totalPurchasedUnits, int cumulativePurchasedUnits) {

    @Override
    public String toString() {
        return "PurchaseUnitDto{"
                + "date="
                + date
                + ", totalPurchasedUnits="
                + totalPurchasedUnits
                + ", cumulativePurchasedUnits="
                + cumulativePurchasedUnits
                + '}';
    }
}
