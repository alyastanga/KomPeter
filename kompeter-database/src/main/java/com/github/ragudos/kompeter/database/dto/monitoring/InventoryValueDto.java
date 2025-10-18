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
public record InventoryValueDto(
        Timestamp date, float totalInventoryValue, float totalPurchaseValue, float totalSalesValue) {

    @Override
    public String toString() {
        return "InventoryValueDto{"
                + "date="
                + date
                + ", totalInventoryValue="
                + totalInventoryValue
                + ", totalPurchaseValue="
                + totalPurchaseValue
                + ", totalSalesValue="
                + totalSalesValue
                + '}';
    }
}
