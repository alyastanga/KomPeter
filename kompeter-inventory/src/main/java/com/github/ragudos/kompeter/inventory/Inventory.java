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
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.inventory.items.InventoryItemWithStockLocations;
import com.github.ragudos.kompeter.inventory.items.InventoryItemWithTotalQuantity;
import com.github.ragudos.kompeter.inventory.items.InventoryStockLocation;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public final class Inventory {
    private static final Logger LOGGER = KompeterLogger.getLogger(Inventory.class);

    public final int DEFAULT_ROWS_PER_PAGE = 10;
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
        return getInventoryItemsWithTotalQuantities("", null, null, null);
    }

    public InventoryItemWithTotalQuantity[] getInventoryItemsWithTotalQuantities(ItemStatus filterStatus)
            throws InventoryException {
        return getInventoryItemsWithTotalQuantities("", null, null, filterStatus);
    }

    public InventoryItemWithTotalQuantity[] getInventoryItemsWithTotalQuantities(String nameFilter,
            String[] categoryFilters, String[] brandFilters, ItemStatus filterStatus) throws InventoryException {
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
            boolean statusFilter = filterStatus == null ? true : item.status() == filterStatus;

            return similarity >= SEARCH_SIMILARITY_THRESHOLD && isInBrandScope && isInCategoryScope && statusFilter;
        }).map((item) -> {
            return new InventoryItemWithTotalQuantity.InventoryItemWithTotalQuantityBuilder()
                    .setTotalQuantity(Arrays.stream(item.itemStockLocations()).mapToInt((itemLocation) -> {
                        return itemLocation.quantity();
                    }).sum()).setUnitPricePhp(item.unitPricePhp()).setItemName(item.itemName())
                    .setItemStockId(item._itemStockId()).setBrand(item.brand()).setCategories(item.categories())
                    .setDisplayImage(item.displayImage()).setItemStockId(item._itemStockId()).build();
        }).toArray(InventoryItemWithTotalQuantity[]::new);
    }

    public InventoryProductListData getProductList(int rowsPerPage, String nameFilter, String[] categoryFilters,
            String[] brandFilters) throws InventoryException {
        return getProductList(rowsPerPage, nameFilter, categoryFilters, brandFilters, null);
    }

    public InventoryProductListData getProductList(int rowsPerPage, String nameFilter, String[] categoryFilters,
            String[] brandFilters, ItemStatus filterStatus) throws InventoryException {
        if (items == null) {
            refreshItems();

            if (items == null) {
                return null;
            }
        }

        InventoryItemWithStockLocations[] itemsWithStockLocations = Arrays.stream(items).filter((item) -> {
            double similarity = nameFilter.isEmpty()
                    ? SEARCH_SIMILARITY_THRESHOLD
                    : fuzzySimilarity.apply(item.itemName(), nameFilter);
            boolean isInBrandScope = brandFilters != null
                    ? (brandFilters.length == 0 || item.isBrandOf(brandFilters))
                    : true;
            boolean isInCategoryScope = categoryFilters != null
                    ? (categoryFilters.length == 0 || item.isCategoryOf(categoryFilters))
                    : true;
            boolean statusFilter = filterStatus == null ? true : item.status() == filterStatus;

            return similarity >= SEARCH_SIMILARITY_THRESHOLD && isInBrandScope && isInCategoryScope && statusFilter;
        }).map((item) -> {
            return new InventoryItemWithStockLocations.InventoryItemWithStockLocationsBuilder()
                    .setLocations(Arrays.stream(item.itemStockLocations())
                            .map((location) -> new InventoryStockLocation(location._storageLocationId(),
                                    location.name(), location.quantity()))
                            .toArray(InventoryStockLocation[]::new))
                    .setStatus(item.status()).setUnitPricePhp(item.unitPricePhp()).setItemName(item.itemName())
                    .setItemStockId(item._itemStockId()).setBrand(item.brand()).setCategories(item.categories())
                    .setDisplayImage(item.displayImage()).setItemStockId(item._itemStockId()).build();
        }).toArray(InventoryItemWithStockLocations[]::new);

        return new InventoryProductListData(rowsPerPage, itemsWithStockLocations);
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

    public class InventoryProductListData {
        private int currentPage;
        private InventoryItemWithStockLocations[] flatItems; // keep a flat version for repagination
        private InventoryItemWithStockLocations[][] items;
        private int rowsPerPage;

        public InventoryProductListData(int rowsPerPage, InventoryItemWithStockLocations[] allItems) {
            if (rowsPerPage <= 0) {
                throw new IllegalArgumentException("rowsPerPage must be > 0");
            }

            if (allItems == null) {
                throw new IllegalArgumentException("allItems cannot be null");
            }

            this.flatItems = allItems;
            this.rowsPerPage = rowsPerPage;
            this.currentPage = 1;

            paginate();
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public InventoryItemWithStockLocations[] getItemsAtCurrentPage() {
            return items[currentPage - 1];
        }

        public int getRowsPerPage() {
            return rowsPerPage;
        }

        public int getTotalPages() {
            return items.length;
        }

        public void setCurrentPage(int currentPage) {
            if (currentPage <= 0 || currentPage > items.length) {
                throw new IllegalArgumentException(
                        String.format("currentPage argument must be > %d and <= %d", 0, items.length));
            }

            this.currentPage = currentPage;
        }

        public void setRowsPerPage(int rowsPerPage) {
            if (rowsPerPage <= 0 || rowsPerPage > flatItems.length) {
                throw new IllegalArgumentException("rowsPerPage must be > 0");
            }

            this.rowsPerPage = rowsPerPage;

            paginate();
        }

        private void paginate() {
            int pageCount = (int) Math.ceil((double) flatItems.length / rowsPerPage);
            items = new InventoryItemWithStockLocations[pageCount][];

            for (int i = 0; i < pageCount; i++) {
                int start = i * rowsPerPage;
                int end = Math.min(start + rowsPerPage, flatItems.length);
                items[i] = Arrays.copyOfRange(flatItems, start, end);
            }

            if (currentPage > items.length) {
                currentPage = items.length;
            }
        }
    }
}
