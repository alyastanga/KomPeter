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
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockDao;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public final class Inventory {
    private static final Logger LOGGER = KompeterLogger.getLogger(Inventory.class);

    private static Inventory instance;

    public static synchronized Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }

        return instance;
    }

    public final int DEFAULT_ROWS_PER_PAGE = 10;

    public final double SEARCH_SIMILARITY_THRESHOLD = 0.7;

    private final JaroWinklerSimilarity fuzzySimilarity;

    private Inventory() {
        fuzzySimilarity = new JaroWinklerSimilarity();
    }

    public String[] getAllItemBrands() throws InventoryException {
        final AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        final ItemBrandDao brandDao = factoryDao.getItemBrandDao();

        try (Connection conn = factoryDao.getConnection()) {
            return brandDao.getAllBrands().stream().map((item) -> item.name()).toArray(String[]::new);
        } catch (SQLException | IOException err) {
            throw new InventoryException("Failed to get brands", err);
        }
    }

    public String[] getAllItemCategories() throws InventoryException {
        final AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        final ItemCategoryDao categoryDao = factoryDao.getItemCategoryDao();

        try (Connection conn = factoryDao.getConnection()) {
            return categoryDao.getAllCategories().stream().map((item) -> item.name()).toArray(String[]::new);
        } catch (SQLException | IOException err) {
            throw new InventoryException("Failed to get brands", err);
        }
    }

    public InventoryMetadataDto[] getInventoryItemsWithTotalQuantities() throws InventoryException {
        return getInventoryItemsWithTotalQuantities("", null, null, null);
    }

    public InventoryMetadataDto[] getInventoryItemsWithTotalQuantities(final ItemStatus filterStatus)
            throws InventoryException {
        return getInventoryItemsWithTotalQuantities("", null, null, filterStatus);
    }

    public InventoryMetadataDto[] getInventoryItemsWithTotalQuantities(final String nameFilter,
            final String[] categoryFilters, final String[] brandFilters, final ItemStatus filterStatus)
            throws InventoryException {
        final AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        final InventoryDao inventoryDao = factoryDao.getInventoryDao();

        try (Connection conn = factoryDao.getConnection()) {
            final InventoryMetadataDto[] items = inventoryDao.getAllInventoryItems(conn);

            return Arrays.stream(items).filter((item) -> {
                final double similarity = nameFilter.isEmpty()
                        ? SEARCH_SIMILARITY_THRESHOLD
                        : fuzzySimilarity.apply(item.itemName(), nameFilter);
                final boolean isInBrandScope = brandFilters != null
                        ? (brandFilters.length == 0 || item.isBrandOf(brandFilters))
                        : true;
                final boolean isInCategoryScope = categoryFilters != null
                        ? (categoryFilters.length == 0 || item.isCategoryOf(categoryFilters))
                        : true;
                final boolean statusFilter = filterStatus == null ? true : item.status() == filterStatus;

                return similarity >= SEARCH_SIMILARITY_THRESHOLD && isInBrandScope && isInCategoryScope && statusFilter;
            }).toArray(InventoryMetadataDto[]::new);
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Failed to get items", err);
            throw new InventoryException("Failed to get inventory items", err);
        }
    }

    public InventoryProductListData getProductList(final int rowsPerPage, final String nameFilter,
            final String[] categoryFilters, final String[] brandFilters) throws InventoryException {
        return getProductList(rowsPerPage, nameFilter, categoryFilters, brandFilters, null);
    }

    public InventoryProductListData getProductList(final int rowsPerPage, final String nameFilter,
            final String[] categoryFilters, final String[] brandFilters, final ItemStatus filterStatus)
            throws InventoryException {
        final AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        final InventoryDao inventoryDao = factoryDao.getInventoryDao();

        try (Connection conn = factoryDao.getConnection()) {
            final InventoryMetadataDto[] items = inventoryDao.getAllInventoryItems(conn);

            final InventoryMetadataDto[] itemsWithStockLocations = Arrays.stream(items).filter((item) -> {
                final double similarity = nameFilter.isEmpty()
                        ? SEARCH_SIMILARITY_THRESHOLD
                        : fuzzySimilarity.apply(item.itemName(), nameFilter);
                final boolean isInBrandScope = brandFilters != null
                        ? (brandFilters.length == 0 || item.isBrandOf(brandFilters))
                        : true;
                final boolean isInCategoryScope = categoryFilters != null
                        ? (categoryFilters.length == 0 || item.isCategoryOf(categoryFilters))
                        : true;
                final boolean statusFilter = filterStatus == null
                        ? item.status() != ItemStatus.ARCHIVED
                        : item.status() == filterStatus;

                return similarity >= SEARCH_SIMILARITY_THRESHOLD && isInBrandScope && isInCategoryScope && statusFilter;
            }).toArray(InventoryMetadataDto[]::new);

            return new InventoryProductListData(rowsPerPage, itemsWithStockLocations);
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Failed to get items", err);
            throw new InventoryException("Failed to get inventory items", err);
        }
    }

    public void setStatusOfItemsByName(final String[] itemNames, final ItemStatus status) throws InventoryException {
        final AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        final ItemStockDao itemStockDao = factoryDao.getItemStockDao();

        try (Connection conn = factoryDao.getConnection()) {
            conn.setAutoCommit(false);

            for (final String name : itemNames) {
                try {
                    itemStockDao.setItemStocksStatusByName(conn, name, status);
                } catch (SQLException | IOException err1) {
                    try {
                        conn.rollback();
                    } catch (final SQLException err2) {
                        err1.addSuppressed(err2);
                    }

                    throw err1;
                }
            }

            try {
                conn.commit();
            } catch (final SQLException err1) {
                try {
                    conn.rollback();
                } catch (final SQLException err2) {
                    err1.addSuppressed(err2);
                }

                throw err1;
            }
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Failed to archive items", err);
            throw new InventoryException("Failed archive inventory items", err);
        }
    }

    public class InventoryProductListData {
        private int currentPage;
        private final InventoryMetadataDto[] flatItems; // keep a flat version for repagination
        private InventoryMetadataDto[][] items;
        private int rowsPerPage;

        public InventoryProductListData(final int rowsPerPage, final InventoryMetadataDto[] allItems) {
            if (rowsPerPage <= 0) {
                throw new IllegalArgumentException("rowsPerPage must be > 0");
            }

            if (allItems == null) {
                throw new IllegalArgumentException("allItems cannot be null");
            }

            if (allItems.length < rowsPerPage) {
                this.rowsPerPage = allItems.length;
            } else {
                this.rowsPerPage = rowsPerPage;
            }

            this.flatItems = allItems;
            this.currentPage = 1;

            paginate();
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public InventoryMetadataDto[] getItemsAtCurrentPage() {
            return items[currentPage - 1];
        }

        public int getRowsPerPage() {
            return rowsPerPage;
        }

        public int getTotalPages() {
            return items.length;
        }

        public void setCurrentPage(final int currentPage) {
            if (currentPage <= 0 || currentPage > items.length) {
                throw new IllegalArgumentException(
                        String.format("currentPage argument must be > %d and <= %d", 0, items.length));
            }

            this.currentPage = currentPage;
        }

        public void setRowsPerPage(final int rowsPerPage) {
            if (rowsPerPage <= 0 || rowsPerPage > flatItems.length) {
                throw new IllegalArgumentException("rowsPerPage must be > 0");
            }

            this.rowsPerPage = rowsPerPage;

            paginate();
        }

        private void paginate() {
            final int pageCount = (int) Math.ceil((double) flatItems.length / rowsPerPage);
            items = new InventoryMetadataDto[pageCount][];

            for (int i = 0; i < pageCount; i++) {
                final int start = i * rowsPerPage;
                final int end = Math.min(start + rowsPerPage, flatItems.length);
                items[i] = Arrays.copyOfRange(flatItems, start, end);
            }

            if (currentPage > items.length) {
                currentPage = items.length;
            }
        }
    }
}
