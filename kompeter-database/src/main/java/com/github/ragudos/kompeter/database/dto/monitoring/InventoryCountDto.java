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
public class InventoryCountDto {
    public Timestamp date;
    public int totalInventoryCount;
    public int totalPurchaseCount;
    public int totalSalesCount;

    public InventoryCountDto(
            Timestamp date, int totalInventoryCount, int totalPurchaseCount, int totalSalesCount) {
        this.date = date;
        this.totalInventoryCount = totalInventoryCount;
        this.totalPurchaseCount = totalPurchaseCount;
        this.totalSalesCount = totalSalesCount;
    }

    public InventoryCountDto(Timestamp date, int totalInventoryCount) {
        this.date = date;
        this.totalInventoryCount = totalInventoryCount;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getTotalInventoryCount() {
        return totalInventoryCount;
    }

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    public int getTotalSalesCount() {
        return totalSalesCount;
    }

    @Override
    public String toString() {
        return "InventoryCountDTO{"
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
