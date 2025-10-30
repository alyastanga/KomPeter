/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemBrandDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryDao;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.inventory.items.InventoryItemWithTotalQuantity;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public final class Inventory {
    private static final Logger LOGGER = KompeterLogger.getLogger(Inventory.class);

    public final double SEARCH_SIMILARITY_THRESHOLD = 0.7;

    private JaroWinklerSimilarity fuzzySimilarity;
    private InventoryMetadataDto[] items;

    public Inventory() {
        fuzzySimilarity = new JaroWinklerSimilarity();
    }

    public String[] getAllItemBrands() throws InventoryException {
        AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        ItemBrandDao brandDao = factoryDao.getItemBrandDao();

        try (Connection conn = factoryDao.getConnection()) {
            return brandDao.getAllBrands().stream().map((item) -> item.name()).toArray(String[]::new);
        } catch (SQLException | IOException err) {
            throw new InventoryException("Failed to get brands", err);
        }
    }

    public String[] getAllItemCategories() throws InventoryException {
        AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        ItemCategoryDao categoryDao = factoryDao.getItemCategoryDao();

        try (Connection conn = factoryDao.getConnection()) {
            return categoryDao.getAllCategories().stream().map((item) -> item.name()).toArray(String[]::new);
        } catch (SQLException | IOException err) {
            throw new InventoryException("Failed to get brands", err);
        }
    }

    public InventoryItemWithTotalQuantity[] getInventoryItemsWithTotalQuantities() throws InventoryException {
        return getInventoryItemsWithTotalQuantities("", null, null);
    }

    public InventoryItemWithTotalQuantity[] getInventoryItemsWithTotalQuantities(String nameFilter,
            String[] categoryFilters, String[] brandFilters) throws InventoryException {
        if (items == null) {
            refreshItems();

            if (items == null) {
                return new InventoryItemWithTotalQuantity[]{};
            }
        }

        return Arrays.stream(items).filter((item) -> {
            double similarity = nameFilter.isEmpty()
                    ? SEARCH_SIMILARITY_THRESHOLD
                    : fuzzySimilarity.apply(item.itemName(), nameFilter);
            boolean isInBrandScope = brandFilters != null
                    ? (brandFilters.length == 0 || item.isBrandOf(brandFilters))
                    : true;
            boolean isInCategoryScope = categoryFilters != null
                    ? (categoryFilters.length == 0 || item.isCategoryOf(categoryFilters))
                    : true;

            return similarity >= SEARCH_SIMILARITY_THRESHOLD && isInBrandScope && isInCategoryScope;
        }).map((item) -> {
            return new InventoryItemWithTotalQuantity.InventoryItemWithTotalQuantityBuilder()
                    .setTotalQuantity(Arrays.stream(item.itemStockLocations()).mapToInt((itemLocation) -> {
                        return itemLocation.quantity();
                    }).sum()).setUnitPricePhp(item.unitPricePhp()).setItemName(item.itemName())
                    .setItemStockId(item._itemStockId()).setBrand(item.brand()).setCategories(item.categories())
                    .setDisplayImage(item.displayImage()).setItemStockId(item._itemStockId()).setItemId(item._itemId())
                    .build();
        }).toArray(InventoryItemWithTotalQuantity[]::new);
    }

    public void refreshItems() throws InventoryException {
        AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        InventoryDao inventoryDao = factoryDao.getInventoryDao();

        try (Connection conn = factoryDao.getConnection()) {
            items = inventoryDao.getAllInventoryItems(conn);

        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Failed to get items", err);
            throw new InventoryException("Failed to get inventory items", err);
        }
    }
}
