package com.github.ragudos.kompeter.database;

import com.github.ragudos.kompeter.database.dao.AccountDao;
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
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * This class is responsible for creating the appropriate SQL factory DAO based on the database
 * type. It uses a factory method pattern to create the appropriate instance. <br>
 * <br>
 * Example usage of the AbstractSqlFactoryDao to retrieve a platform-specific DAO implementation.
 *
 * <p>This snippet demonstrates how to obtain a MySQL-specific factory instance using the factory
 * method {@link AbstractSqlFactoryDao#getSqlFactoryDao(int)}, and then retrieve the {@link
 * SessionDao} to perform a data access operation. This promotes loose coupling between
 * database-specific implementations and the business logic, making the codebase easier to maintain
 * and extend for other database platforms.
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
 * <p>This design allows easy substitution of different database backends by changing only the
 * factory input type, without modifying the business logic that relies on the DAO interfaces.
 *
 * @see <a href= "https://www.oracle.com/java/technologies/dataaccessobject.html">Article about DAO
 *     Pattern by Oracle</a>
 */
public abstract class AbstractSqlFactoryDao {
    protected class PooledConnectionHandler implements InvocationHandler {
        final @NotNull Connection conn;

        public PooledConnectionHandler(final @NotNull Connection conn) {
            this.conn = conn;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final String methodName = method.getName();

            if (methodName.equals("close")) {
                returnConnection((Connection) proxy);

                return null;
            }

            try {
                return method.invoke(conn, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        public void reallyClose() throws SQLException {
            conn.close();
        }
    }

    protected static final Logger LOGGER = KompeterLogger.getLogger(AbstractSqlFactoryDao.class);
    public static final int SQLITE = 1;

    public static final int POOLED_CONNECTION_COUNT = 1;

    // We use LinkedList since we are only going to pop and push onto this
    protected final BlockingQueue<Connection> pooledConnections = new ArrayBlockingQueue<>(1);

    public static @NotNull AbstractSqlFactoryDao getSqlFactoryDao(int databaseType) {
        return switch (databaseType) {
            case SQLITE -> SqliteFactoryDao.getInstance();
            default -> throw new IllegalArgumentException("Unsupported database type: " + databaseType);
        };
    }

    protected void returnConnection(@NotNull Connection conn) {
        pooledConnections.offer(conn);
    }

    /**
     * Used to override default behavior of {@link Connection} for specific methods like {@link
     * Connection#close}
     */
    protected @NotNull Connection createProxy(@NotNull Connection realConn) {
        return (Connection)
                Proxy.newProxyInstance(
                        realConn.getClass().getClassLoader(),
                        new Class<?>[] {Connection.class},
                        new PooledConnectionHandler(realConn));
    }

    /**
     * Get a {@link Connection} from the connection pool
     *
     * <p>Useful for reusing connections for fast connection to the database.
     *
     * @return A {@link Connection} wrapped around the real connection.
     */
    public @NotNull Connection getConnection() {
        try {
            return pooledConnections.take();
        } catch (InterruptedException err) {
            LOGGER.log(Level.SEVERE, "Cannot take connection", err);
            return null;
        }
    }

    public void shutdown() throws SQLException {
        try {
            for (Connection conn : pooledConnections) {
                InvocationHandler handler = Proxy.getInvocationHandler(conn);

                if (handler instanceof PooledConnectionHandler p) {
                    p.reallyClose();
                }
            }

            pooledConnections.clear();
        } finally {
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

    public @NotNull abstract PurchaseDao getPurchaseDao();

    public @NotNull abstract PurchaseItemStockDao getPurchaseItemStockDao();

    public @NotNull abstract PurchasePaymentDao getPurchasePaymentDao();

    public @NotNull abstract RoleDao getRoleDao();

    public @NotNull abstract SaleDao getSaleDao();

    public @NotNull abstract SaleItemStockDao getSaleItemStockDao();

    public @NotNull abstract SalePaymentDao getSalePaymentDao();

    public @NotNull abstract SupplierDao getSupplierDao();
}
