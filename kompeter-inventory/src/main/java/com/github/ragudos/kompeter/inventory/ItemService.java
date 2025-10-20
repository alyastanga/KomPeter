/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemBrandDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemDao;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemService implements Items {
    private final ItemDao sqliteItemDao;
    private final InventoryDao sqliteInventoryDao;
    private final ItemBrandDao sqliteItemBrandDao;
    private final ItemCategoryDao sqliteItemCategoryDao;
    private final ItemCategoryAssignmentDao sqliteItemCategoryAssignmentDao;

    public ItemService() {
        AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

        this.sqliteItemDao = factoryDao.getItemDao();
        this.sqliteInventoryDao = factoryDao.getInventoryDao();
        this.sqliteItemBrandDao = factoryDao.getItemBrandDao();
        this.sqliteItemCategoryDao = factoryDao.getItemCategoryDao();
        this.sqliteItemCategoryAssignmentDao = factoryDao.getItemCategoryAssignmentDao();
    }

    @Override
    public List<InventoryMetadataDto> showInventoryItems() throws InventoryException {
        try {
            return sqliteInventoryDao.getAllData();
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public void deleteItem(int id) throws InventoryException {
        try {
            sqliteItemDao.deleteItemById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public List<InventoryMetadataDto> searchItem(String search) throws InventoryException {
        try {
            return sqliteInventoryDao.getAllData(search);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public void setItemCategory(int _itemId, int _categoryId) throws InventoryException {
        try {
            sqliteItemCategoryAssignmentDao.setItemCategory(_itemId, _categoryId);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public int updateItemName(String name, int id) throws InventoryException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int updateItemBrand(int brandId, int itemId) throws InventoryException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int updateItemCategory(int categoryId, int itemId) throws InventoryException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int addItem(String name) throws InventoryException {
        int x = 0;
        try {
            x = sqliteItemDao.insertItem(name, null);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
        return x;
    }

    @Override
    public int addBrand(String name) throws InventoryException {
        int x = 0;
        try {
            x = sqliteItemBrandDao.insertItemBrand(name, null);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
        return x;
    }

    @Override
    public int addCategory(String name) throws InventoryException {
        int x = 0;
        try {
            x = sqliteItemCategoryDao.insertItemCategory(name, null);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }

        return x;
    }

    @Override
    public int addItem(String name, String description) throws InventoryException {
        int x = 0;
        try {
            x = sqliteItemDao.insertItem(name, description);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
        return x;
    }

    @Override
    public int addBrand(String name, String description) throws InventoryException {
        int x = 0;
        try {
            x = sqliteItemBrandDao.insertItemBrand(name, description);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
        return x;
    }

    @Override
    public int addCategory(String name, String description) throws InventoryException {
        int x = 0;
        try {
            x = sqliteItemCategoryDao.insertItemCategory(name, description);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }

        return x;
    }
}
