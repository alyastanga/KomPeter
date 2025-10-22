/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.monitoring;

/**
 * @author Hanz Mapua
 */
public record Top10LowStockItemsDto(String itemName, String brandName, String categoryName, int quantity) {

    @Override
    public String toString() {
        return "LowStockItemsDto{" + "itemName=" + itemName + ", brandName=" + brandName + ", categoryName="
                + categoryName + ", quantity=" + quantity + '}';
    }
}
