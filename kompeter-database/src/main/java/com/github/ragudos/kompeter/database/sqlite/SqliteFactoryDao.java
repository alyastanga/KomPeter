/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemBrandDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemRestockDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockStorageLocationDao;
import com.github.ragudos.kompeter.database.dao.inventory.PurchaseDao;
import com.github.ragudos.kompeter.database.dao.inventory.PurchaseItemStockDao;
import com.github.ragudos.kompeter.database.dao.inventory.PurchasePaymentDao;
import com.github.ragudos.kompeter.database.dao.inventory.StorageLocationDao;
import com.github.ragudos.kompeter.database.dao.inventory.SupplierDao;
import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dao.sales.SaleItemStockDao;
import com.github.ragudos.kompeter.database.dao.sales.SalePaymentDao;
import com.github.ragudos.kompeter.database.dao.user.AccountDao;
import com.github.ragudos.kompeter.database.dao.user.RoleDao;
import com.github.ragudos.kompeter.database.dao.user.SessionDao;
import com.github.ragudos.kompeter.database.dao.user.UserDao;
import com.github.ragudos.kompeter.database.dao.user.UserMetadataDao;
import com.github.ragudos.kompeter.database.dao.user.UserRoleDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemBrandDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemCategoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemRestockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemStockStorageLocationDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchasePaymentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteStorageLocationDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteSupplierDao;
import com.github.ragudos.kompeter.database.sqlite.dao.sales.SqliteSaleDao;
import com.github.ragudos.kompeter.database.sqlite.dao.sales.SqliteSaleItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.sales.SqliteSalePaymentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.user.SqliteAccountDao;
import com.github.ragudos.kompeter.database.sqlite.dao.user.SqliteRoleDao;
import com.github.ragudos.kompeter.database.sqlite.dao.user.SqliteSessionDao;
import com.github.ragudos.kompeter.database.sqlite.dao.user.SqliteUserDao;
import com.github.ragudos.kompeter.database.sqlite.dao.user.SqliteUserMetadataDao;
import com.github.ragudos.kompeter.database.sqlite.dao.user.SqliteUserRoleDao;
import com.github.ragudos.kompeter.utilities.constants.Directories;
import com.github.ragudos.kompeter.utilities.constants.Metadata;
import com.github.ragudos.kompeter.utilities.io.FileUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public final class SqliteFactoryDao extends AbstractSqlFactoryDao {
    public static final String DB_URL = "jdbc:sqlite:/" + MAIN_DB_FILE_NAME;
    public static final String MAIN_DB_FILE_NAME = Directories.SQLITE_DIRECTORY + File.separator + "main-"
            + Metadata.APP_ENV + ".db";

    private static final Logger LOGGER = KompeterLogger.getLogger(SqliteFactoryDao.class);

    private static SqliteFactoryDao instance = null;

    public static synchronized @NotNull SqliteFactoryDao getInstance() {
        if (instance == null) {
            instance = new SqliteFactoryDao();
        }

        return instance;
    }

    private SqliteFactoryDao() {
        super();

        FileUtils.createDirectoryIfNotExists(Directories.SQLITE_DIRECTORY);
        FileUtils.createFileIfNotExists(MAIN_DB_FILE_NAME);
    }

    @Override
    public @NotNull AccountDao getAccountDao() {
        return new SqliteAccountDao();
    }

    @Override
    public @NotNull InventoryDao getInventoryDao() {
        return new SqliteInventoryDao();
    }

    @Override
    public @NotNull ItemBrandDao getItemBrandDao() {
        return new SqliteItemBrandDao();
    }

    @Override
    public @NotNull ItemCategoryAssignmentDao getItemCategoryAssignmentDao() {
        return new SqliteItemCategoryAssignmentDao();
    }

    @Override
    public @NotNull ItemCategoryDao getItemCategoryDao() {
        return new SqliteItemCategoryDao();
    }

    @Override
    public @NotNull ItemDao getItemDao() {
        return new SqliteItemDao();
    }

    @Override
    public @NotNull ItemRestockDao getItemRestockDao() {
        return new SqliteItemRestockDao();
    }

    @Override
    public @NotNull ItemStockDao getItemStockDao() {
        return new SqliteItemStockDao();
    }

    @Override
    public ItemStockStorageLocationDao getItemStockStorageLocationDao() {
        return new SqliteItemStockStorageLocationDao();
    }

    @Override
    public @NotNull PurchaseDao getPurchaseDao() {
        return new SqlitePurchaseDao();
    }

    @Override
    public @NotNull PurchaseItemStockDao getPurchaseItemStockDao() {
        return new SqlitePurchaseItemStockDao();
    }

    @Override
    public @NotNull PurchasePaymentDao getPurchasePaymentDao() {
        return new SqlitePurchasePaymentDao();
    }

    @Override
    public @NotNull RoleDao getRoleDao() {
        return new SqliteRoleDao();
    }

    @Override
    public @NotNull SaleDao getSaleDao() {
        return new SqliteSaleDao();
    }

    @Override
    public @NotNull SaleItemStockDao getSaleItemStockDao() {
        return new SqliteSaleItemStockDao();
    }

    @Override
    public @NotNull SalePaymentDao getSalePaymentDao() {
        return new SqliteSalePaymentDao();
    }

    @Override
    public @NotNull SessionDao getSessionDao() {
        return new SqliteSessionDao();
    }

    @Override
    public StorageLocationDao getStorageLocationDao() {
        return new SqliteStorageLocationDao();
    }

    @Override
    public @NotNull SupplierDao getSupplierDao() {
        return new SqliteSupplierDao();
    }

    @Override
    public @NotNull UserDao getUserDao() {
        return new SqliteUserDao();
    }

    @Override
    public @NotNull UserMetadataDao getUserMetadataDao() {
        return new SqliteUserMetadataDao();
    }

    @Override
    public @NotNull UserRoleDao getUserRoleDao() {
        return new SqliteUserRoleDao();
    }

    @Override
    protected @NotNull Connection createConnection() throws SQLException {
        try {
            Class.forName("java.sql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC Driver not found", e);
        }

        return DriverManager.getConnection(DB_URL);
    }
}
