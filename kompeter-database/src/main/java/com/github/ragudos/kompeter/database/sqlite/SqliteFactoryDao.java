package com.github.ragudos.kompeter.database.sqlite;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.AccountDao;
import com.github.ragudos.kompeter.database.dao.InventoryDao;
import com.github.ragudos.kompeter.database.dao.ItemBrandDao;
import com.github.ragudos.kompeter.database.dao.ItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.dao.ItemDao;
import com.github.ragudos.kompeter.database.dao.ItemRestockDao;
import com.github.ragudos.kompeter.database.dao.ItemStockDao;
import com.github.ragudos.kompeter.database.dao.PurchaseDao;
import com.github.ragudos.kompeter.database.dao.PurchaseItemStockDao;
import com.github.ragudos.kompeter.database.dao.PurchasePaymentDao;
import com.github.ragudos.kompeter.database.dao.RoleDao;
import com.github.ragudos.kompeter.database.dao.SaleDao;
import com.github.ragudos.kompeter.database.dao.SaleItemStockDao;
import com.github.ragudos.kompeter.database.dao.SalePaymentDao;
import com.github.ragudos.kompeter.database.dao.SessionDao;
import com.github.ragudos.kompeter.database.dao.SupplierDao;
import com.github.ragudos.kompeter.database.dao.UserDao;
import com.github.ragudos.kompeter.database.dao.UserRoleDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteAccountDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteItemBrandDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteItemDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteItemRestockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqlitePurchaseDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqlitePurchaseItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqlitePurchasePaymentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteRoleDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteSaleDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteSaleItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteSalePaymentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteSessionDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteSupplierDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteUserDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteUserRoleDao;
import com.github.ragudos.kompeter.utilities.constants.Directories;
import com.github.ragudos.kompeter.utilities.io.FileUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public final class SqliteFactoryDao extends AbstractSqlFactoryDao {
    private static final Logger LOGGER = KompeterLogger.getLogger(SqliteFactoryDao.class);
    private static SqliteFactoryDao instance = null;

    public static final String MAIN_DB_FILE_NAME =
            Directories.SQLITE_DIRECTORY + File.separator + "main.db";
    public static final String DB_URL = "jdbc:sqlite:/" + MAIN_DB_FILE_NAME;

    private SqliteFactoryDao() {
        super();

        FileUtils.createDirectoryIfNotExists(Directories.SQLITE_DIRECTORY);
        FileUtils.createFileIfNotExists(MAIN_DB_FILE_NAME);

        try {
            for (int i = 0; i < POOLED_CONNECTION_COUNT; ++i) {
                pooledConnections.add(createProxy(createConnection()));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to create connections", e);
            throw new RuntimeException("Failed to initialize FactoryDao");
        }
    }

    public static synchronized @NotNull SqliteFactoryDao getInstance() {
        if (instance == null) {
            instance = new SqliteFactoryDao();
        }

        return instance;
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

    @Override
    public void shutdown() throws SQLException {
        super.shutdown();

        writeLock.lock();

        try {
            instance = null;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public @NotNull AccountDao getAccountDao() {
        return new SqliteAccountDao();
    }

    @Override
    public @NotNull UserDao getUserDao() {
        return new SqliteUserDao();
    }

    @Override
    public @NotNull UserRoleDao getUserRoleDao() {
        return new SqliteUserRoleDao();
    }

    @Override
    public @NotNull SessionDao getSessionDao() {
        return new SqliteSessionDao();
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
    public @NotNull ItemDao getItemDao() {
        return new SqliteItemDao(SqliteFactoryDao.getInstance().getConnection());
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
    public @NotNull SupplierDao getSupplierDao() {
        return new SqliteSupplierDao();
    }
    
    @Override
    public @NotNull InventoryDao getInventoryDao(){
        return new SqliteInventoryDao(SqliteFactoryDao.getInstance().getConnection());
    }
    
}
