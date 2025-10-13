/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * @author Peter M. Dela Cruz
 */
public record InventoryMetadataDto(
        @Range(from = 0, to = Integer.MAX_VALUE) int _itemId,
        @Range(from = 0, to = Integer.MAX_VALUE)
                int _itemStockId, // hide this shit na lang sa table, necessarry lang sya if i-edit yung
        // price
        @Range(from = 0, to = Integer.MAX_VALUE)
                int _stockLocationId, // hide this shit also, necessarry if i-edit lang ang yung location ng
        // stock
        @NotNull Timestamp _createdAt,
        @NotNull String categoryName,
        @NotNull String itemName,
        @NotNull String itemDescription,
        @NotNull String brandName,
        @NotNull double itemPricePhp,
        @NotNull int quantity,
        @NotNull String location) {}
;
