/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

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
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

/**
 * This class is responsible for creating the appropriate SQL factory DAO based
 * on the database type. It uses a factory method pattern to create the
 * appropriate instance. <br>
 * <br>
 * Example usage of the AbstractSqlFactoryDao to retrieve a platform-specific
 * DAO implementation.
 *
 * <p>
 * This snippet demonstrates how to obtain a MySQL-specific factory instance
 * using the factory method {@link AbstractSqlFactoryDao#getSqlFactoryDao(int)},
 * and then retrieve the {@link SessionDao} to perform a data access operation.
 * This promotes loose coupling between database-specific implementations and
 * the business logic, making the codebase easier to maintain and extend for
 * other database platforms.
 *
 * <pre>
 * // Step 1: Get the factory for the specified database type (MySQL in this
 * // case)
 * var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
 *
 * // Step 2: Retrieve the Connection and SessionDao implementation from the
 * // factory
 * var conn = factory.getConnection();
 * var sessionDao = factory.getSessionDao();
 *
 * // Step 3: Use the Dao to perform operations, such as checking if a session
 * // exists
 * var sessionExists = sessionDao.sessionExists(conn, sessionUid);
 * </pre>
 *
 * <p>
 * This design allows easy substitution of different database backends by
 * changing only the factory input type, without modifying the business logic
 * that relies on the DAO interfaces.
 *
 * @see <a href=
 *      "https://www.oracle.com/java/technologies/dataaccessobject.html">Article
 *      about DAO Pattern by Oracle</a>
 */
public abstract class AbstractSqlFactoryDao {
    protected static final Logger LOGGER = KompeterLogger.getLogger(AbstractSqlFactoryDao.class);
    public static final int SQLITE = 1;

    public static @NotNull AbstractSqlFactoryDao getSqlFactoryDao(int databaseType) {
        return switch (databaseType) {
            case SQLITE -> SqliteFactoryDao.getInstance();
            default -> throw new IllegalArgumentException("Unsupported database type: " + databaseType);
        };
    }

    /**
     * Get a {@link Connection} from the connection pool
     *
     * <p>
     * Useful for reusing connections for fast connection to the database.
     *
     * @return A {@link Connection} wrapped around the real connection.
     */
    public @NotNull Connection getConnection() {
        try {
            return createConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "", e);
            return null;
        }
    }

    /** Create a {@link Connection} */
    protected abstract @NotNull Connection createConnection() throws SQLException;

    public @NotNull abstract AccountDao getAccountDao();

    public @NotNull abstract UserDao getUserDao();

    public @NotNull abstract UserRoleDao getUserRoleDao();

    public @NotNull abstract SessionDao getSessionDao();

    public @NotNull abstract ItemBrandDao getItemBrandDao();

    public @NotNull abstract ItemCategoryAssignmentDao getItemCategoryAssignmentDao();

    public @NotNull abstract ItemDao getItemDao();

    public @NotNull abstract ItemRestockDao getItemRestockDao();

    public @NotNull abstract ItemStockDao getItemStockDao();

    public @NotNull abstract ItemCategoryDao getItemCategoryDao();

    public @NotNull abstract PurchaseDao getPurchaseDao();

    public @NotNull abstract UserMetadataDao getUserMetadataDao();

    public @NotNull abstract PurchaseItemStockDao getPurchaseItemStockDao();

    public @NotNull abstract PurchasePaymentDao getPurchasePaymentDao();

    public @NotNull abstract RoleDao getRoleDao();

    public @NotNull abstract SaleDao getSaleDao();

    public @NotNull abstract SaleItemStockDao getSaleItemStockDao();

    public @NotNull abstract SalePaymentDao getSalePaymentDao();

    public @NotNull abstract SupplierDao getSupplierDao();

    public @NotNull abstract InventoryDao getInventoryDao();

    public @NotNull abstract StorageLocationDao getStorageLocationDao();

    public @NotNull abstract ItemStockStorageLocationDao getItemStockStorageLocationDao();
}
