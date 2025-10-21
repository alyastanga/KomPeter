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
public record Top10OldItemsDto(
        String itemName,
        String brandName,
        String categoryName,
        int totalQuantity,
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
                + ", totalQuantity="
                + totalQuantity
                + ", stockedDate="
                + stockedDate
                + ", daysInStock="
                + daysInStock
                + '}';
    }
}
