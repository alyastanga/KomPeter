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
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemCategoryDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemBrandDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemCategoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    public int deleteItem(int id) throws InventoryException {
        try {
            return sqliteItemDao.deleteItemById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public List<InventoryMetadataDto> searchItem(String search) throws InventoryException {
        try {
            return sqliteInventoryDao.getAllData("%" + search.trim() + "%");
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
        try {
            return sqliteItemDao.updateItemNameById(name, id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public int updateItemCategory(int categoryId, int itemId) throws InventoryException {
        try {
            return sqliteItemCategoryAssignmentDao.updateItemCategoryById(itemId, categoryId);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public int addItem(String name) throws InventoryException {
        try {
            return sqliteItemDao.insertItem(name, null);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public int addBrand(String name) throws InventoryException {
        try {
            return sqliteItemBrandDao.insertItemBrand(name, null);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public int addCategory(String name) throws InventoryException {
        try {
            return sqliteItemCategoryDao.insertItemCategory(name, null);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public int addItem(String name, String description) throws InventoryException {
        try {
            return sqliteItemDao.insertItem(name, description);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public int addBrand(String name, String description) throws InventoryException {
        try {
            return sqliteItemBrandDao.insertItemBrand(name, description);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public int addCategory(String name, String description) throws InventoryException {
        try {
            return sqliteItemCategoryDao.insertItemCategory(name, description);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public List<ItemBrandDto> showAllBrands() throws InventoryException {
        try {
            return sqliteItemBrandDao.getAllBrands();
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public Optional<ItemBrandDto> showBrandsById(int id) throws InventoryException {
        try {
            return sqliteItemBrandDao.getBrandById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public List<ItemCategoryDto> showAllCategories() throws InventoryException {
        try {
            return sqliteItemCategoryDao.getAllCategories();
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public Optional<ItemCategoryDto> showCategoryById(int id) throws InventoryException {
        try {
            return sqliteItemCategoryDao.getCategoryById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public List<ItemDto> showAllItems() throws InventoryException {
        try {
            return sqliteItemDao.getAllItems();
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }

    @Override
    public Optional<ItemDto> showItemById(int id) throws InventoryException {
        try {
            return sqliteItemDao.getItemsById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to retrieve complete inventory listing.", e);
        }
    }
}
