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
public class InventoryValueDto {
    public Timestamp date;
    public float totalInventoryValue;
    public float totalPurchaseValue;
    public float totalSalesValue;

    public InventoryValueDto(
            Timestamp date, float totalInventoryValue, float totalPurchaseValue, float totalSalesValue) {
        this.date = date;
        this.totalInventoryValue = totalInventoryValue;
        this.totalPurchaseValue = totalPurchaseValue;
        this.totalSalesValue = totalSalesValue;
    }

    public InventoryValueDto(Timestamp date, float totalInventoryValue) {
        this.date = date;
        this.totalInventoryValue = totalInventoryValue;
    }

    public Timestamp getDate() {
        return date;
    }

    public float getTotalInventoryValue() {
        return totalInventoryValue;
    }

    public float getTotalPurchaseValue() {
        return totalPurchaseValue;
    }

    public float getTotalSalesValue() {
        return totalSalesValue;
    }

    @Override
    public String toString() {
        return "InventoryValueDTO{"
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
