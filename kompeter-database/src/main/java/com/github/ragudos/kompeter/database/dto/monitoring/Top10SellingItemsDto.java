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
public record Top10SellingItemsDto(
        String itemName, String brandName, String categoryName, int totalSold, float totalRevenue) {

    @Override
    public String toString() {
        return "TopSellingDto{"
                + "itemName="
                + itemName
                + ", brandName="
                + brandName
                + ", categoryName="
                + categoryName
                + ", totalSold="
                + totalSold
                + ", totalRevenue="
                + totalRevenue
                + '}';
    }
}
