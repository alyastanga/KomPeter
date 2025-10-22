/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import com.github.ragudos.kompeter.database.dto.inventory.ItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemRestockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemStockStorageLocationDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteStorageLocationDao;

/**
 * @author Peter M. Dela Cruz
 */
public class StockService implements Stock {
    private final SqliteItemRestockDao sqliteItemRestockDao;
    private final SqliteItemStockDao sqliteItemStockDao;
    private final SqliteItemStockStorageLocationDao sqliteItemStockStorageLocationDao;
    private final SqliteStorageLocationDao sqliteStorageLocationDao;

    public StockService(SqliteItemStockDao sqliteItemStockDao, SqliteItemRestockDao sqliteItemRestockDao,
            SqliteStorageLocationDao sqliteStorageLocationDao,
            SqliteItemStockStorageLocationDao sqliteItemStockStorageLocationDao) {
        this.sqliteItemStockDao = sqliteItemStockDao;
        this.sqliteItemRestockDao = sqliteItemRestockDao;
        this.sqliteStorageLocationDao = sqliteStorageLocationDao;
        this.sqliteItemStockStorageLocationDao = sqliteItemStockStorageLocationDao;
    }

    /*
     * input 1 in the storageLocationId if you want to restock in main display floor
     */
    @Override
    public int addRestock(int qtyAdded, int itemStockId, int storageLocationId) throws InventoryException {
        try {
            int qtyBefore = getItemStockById(itemStockId);
            int qtyAfter = qtyBefore + qtyAdded;

            sqliteItemRestockDao.insertItemRestock(itemStockId, qtyBefore, qtyAfter, qtyAdded);

            return sqliteItemStockStorageLocationDao.updateItemStockQuantity(qtyAfter, itemStockId, storageLocationId);

        } catch (SQLException | IOException e) {
            System.err.println("Restock failed for ID " + itemStockId + ". Transaction integrity compromised.");
            throw new InventoryException("Restock operation failed due to a database error.", e);
        }
    }

    @Override
    public int addStorageLoc(String name, String description) throws InventoryException {
        try {
            return sqliteStorageLocationDao.insertStorageLocation(name, description);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to create new storage location.", e);
        }
    }

    @Override
    public int deleteItemStockById(int id) throws InventoryException {
        try {
            return sqliteItemStockDao.deleteItemStockById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to delete stock id: " + id, e);
        }
    }

    @Override
    public int deleteRestockById(int id) throws InventoryException {
        try {
            return sqliteItemRestockDao.deleteRestockById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to delete restock id: " + id, e);
        }
    }

    @Override
    public int deleteStockStorageLocationById(int id) throws InventoryException {
        try {
            return sqliteItemStockStorageLocationDao.deleteIssl(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to delete stock storage location id: " + id, e);
        }
    }

    @Override
    public int deleteStorageLocationById(int id) throws InventoryException {
        try {
            return sqliteStorageLocationDao.deleteStorageLocationById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to delete storage location id: " + id, e);
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

    @Override
    public int setItemStocks(int _itemId, int _itemBrandId, BigDecimal _unitPrice, int min_quantity)
            throws InventoryException {
        try {
            return sqliteItemStockDao.insertItemStock(_itemId, _itemBrandId, _unitPrice, min_quantity);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to create new item stock record.", e);
        }
    }

    @Override
    public int setStockLocation(int _itemStockId, int _storageLocId, int qty) throws InventoryException {
        try {
            return sqliteItemStockStorageLocationDao.setItemStockStorageLocation(_itemStockId, _storageLocId, qty);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to create new item stock record.", e);
        }
    }

    @Override
    public int updateItemBrand(int brandId, int itemId) throws InventoryException {
        try {
            return sqliteItemStockDao.updateItemBrandById(brandId, itemId);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to update item brand for item id: " + itemId, e);
        }
    }

    @Override
    public int updateItemStockStorageLocationQtyById(int itemStockId, int storageLocId, int qty)
            throws InventoryException {
        try {
            return sqliteItemStockStorageLocationDao.updateItemStockQuantity(qty, itemStockId, storageLocId);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to create new item stock record.", e);
        }
    }

    @Override
    public int updateRestockQtyAdded(int qtyAdded, int id) throws InventoryException {
        try {
            return sqliteItemRestockDao.updateRestockQtyAddedById(qtyAdded, id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to update quantity added.", e);
        }
    }

    @Override
    public int updateRestockQtyAfter(int qtyAfter, int id) throws InventoryException {
        try {
            return sqliteItemRestockDao.updateRestockQtyAfterById(qtyAfter, id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to update quantity after.", e);
        }
    }

    @Override
    public int updateRestockQtyBefore(int qtyBefore, int id) throws InventoryException {
        try {
            return sqliteItemRestockDao.updateRestockQtyBeforeById(qtyBefore, id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to update quantity before.", e);
        }
    }
}
