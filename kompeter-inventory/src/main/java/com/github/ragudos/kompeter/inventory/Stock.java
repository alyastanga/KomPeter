/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import java.math.BigDecimal;
import org.jetbrains.annotations.Nullable;

/**
 * @author Peter M. Dela Cruz
 */
public interface Stock {
    void setStockLocation(int _itemStockId, int _storageLocId, int qty) throws InventoryException;

    void addStorageLoc(String name, @Nullable String description) throws InventoryException;

    void addRestock(int qtyAdded, int itemStockId, int storageLocationId) throws InventoryException;

    void setItemStocks(
            int _itemId, int _itemBrandId, BigDecimal _unitPrice, @Nullable int min_quantity)
            throws InventoryException;

    int getItemStockById(int id) throws InventoryException;
}
