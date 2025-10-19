/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.ItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemRestockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemStockStorageLocationDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteStorageLocationDao;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author Peter M. Dela Cruz
 */
public class StockService implements Stock {
    private final SqliteItemRestockDao sqliteItemRestockDao;
    private final SqliteItemStockDao sqliteItemStockDao;
    private final SqliteStorageLocationDao sqliteStorageLocationDao;
    private final SqliteItemStockStorageLocationDao sqliteItemStockStorageLocationDao;

    public StockService(
            SqliteItemStockDao sqliteItemStockDao,
            SqliteItemRestockDao sqliteItemRestockDao,
            SqliteStorageLocationDao sqliteStorageLocationDao,
            SqliteItemStockStorageLocationDao sqliteItemStockStorageLocationDao) {
        this.sqliteItemStockDao = sqliteItemStockDao;
        this.sqliteItemRestockDao = sqliteItemRestockDao;
        this.sqliteStorageLocationDao = sqliteStorageLocationDao;
        this.sqliteItemStockStorageLocationDao = sqliteItemStockStorageLocationDao;
    }

    @Override
    public void setStockLocation(int _itemStockId, int _storageLocId, int qty)
            throws InventoryException {
        try {
            sqliteItemStockStorageLocationDao.setItemStockStorageLocation(
                    _itemStockId, _storageLocId, qty);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to create new item stock record.", e);
        }
    }

    @Override
    public void addStorageLoc(String name, String description) throws InventoryException {
        try {
            sqliteStorageLocationDao.insertStorageLocation(name, description);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to create new storage location.", e);
        }
    }

    /*
     * input 1 in the storageLocationId if you want to restock in main display floor
     */
    @Override
    public void addRestock(int qtyAdded, int itemStockId, int storageLocationId)
            throws InventoryException {
        try {
            int qtyBefore = getItemStockById(itemStockId);
            int qtyAfter = qtyBefore + qtyAdded;

            sqliteItemRestockDao.insertItemRestock(itemStockId, qtyBefore, qtyAfter, qtyAdded);

            sqliteItemStockStorageLocationDao.updateItemStockQuantity(
                    qtyAfter, itemStockId, storageLocationId);

        } catch (SQLException | IOException e) {
            System.err.println(
                    "Restock failed for ID " + itemStockId + ". Transaction integrity compromised.");
            throw new InventoryException("Restock operation failed due to a database error.", e);
        }
    }

    @Override
    public void setItemStocks(int _itemId, int _itemBrandId, BigDecimal _unitPrice, int min_quantity)
            throws InventoryException {
        try {
            sqliteItemStockDao.insertItemStock(_itemId, _itemBrandId, _unitPrice, min_quantity);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to create new item stock record.", e);
        }
    }

    @Override
    public int getItemStockById(int id) throws InventoryException {
        try {
            Optional<ItemStockDto> optionalStock = sqliteItemStockDao.getItemStockById(id);
            return optionalStock.map(ItemStockDto::quantity).orElse(0);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve item stock quantity for ID: " + id, e);
        }
    }
}
