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
public record InventoryCountDto(
        Timestamp date, int totalInventoryCount, int totalPurchaseCount, int totalSalesCount) {

    @Override
    public String toString() {
        return "InventoryCountDto{"
                + "date="
                + date
                + ", totalInventoryCount="
                + totalInventoryCount
                + ", totalPurchaseCount="
                + totalPurchaseCount
                + ", totalSalesCount="
                + totalSalesCount
                + '}';
    }
}
