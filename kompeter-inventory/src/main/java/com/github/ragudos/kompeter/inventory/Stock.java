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
    int setStockLocation(int _itemStockId, int _storageLocId, int qty) throws InventoryException;

    int addStorageLoc(String name, @Nullable String description) throws InventoryException;

    int addRestock(int qtyAdded, int itemStockId, int storageLocationId) throws InventoryException;

    int setItemStocks(
            int _itemId, int _itemBrandId, BigDecimal _unitPrice, @Nullable int min_quantity)
            throws InventoryException;

    int getItemStockById(int id) throws InventoryException;

    int updateItemBrand(int brandId, int itemId) throws InventoryException;

    int updateRestockQtyAfter(int qtyAfter, int id) throws InventoryException;

    int updateRestockQtyAdded(int qtyAdded, int id) throws InventoryException;

    int updateRestockQtyBefore(int qtyAdded, int id) throws InventoryException;

    int updateItemStockStorageLocationQtyById(int itemStockId, int storageLocId, int qty)
            throws InventoryException;

    int deleteRestockById(int id) throws InventoryException;

    int deleteItemStockById(int id) throws InventoryException;

    int deleteStockStorageLocationById(int id) throws InventoryException;

    int deleteStorageLocationById(int id) throws InventoryException;
}
