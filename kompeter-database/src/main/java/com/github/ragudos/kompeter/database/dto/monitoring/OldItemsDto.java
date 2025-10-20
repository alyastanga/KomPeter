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
public record OldItemsDto(
        String itemName,
        String brandName,
        String categoryName,
        int currentQuantity,
        Timestamp stockedDate,
        int daysInStock) {

    @Override
    public String toString() {
        return "OldItemsDto{"
                + "itemName="
                + itemName
                + ", brandName="
                + brandName
                + ", categoryName="
                + categoryName
                + ", currentQuantity="
                + currentQuantity
                + ", stockedDate="
                + stockedDate
                + ", daysInStock="
                + daysInStock
                + '}';
    }
}
