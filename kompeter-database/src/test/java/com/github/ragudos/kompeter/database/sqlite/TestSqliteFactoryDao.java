package com.github.ragudos.kompeter.database.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestSqliteFactoryDao {
    @Test
    @DisplayName("Test migration")
    void testMigration() {
        try {
            AbstractMigratorFactory factory =
                    AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
            Migrator migrator = factory.getMigrator();

            migrator.migrate();
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Migration failed.";
        }
    }

    @AfterAll
    static void cleanup() {
        try {
            Files.delete(Paths.get(SqliteFactoryDao.MAIN_DB_FILE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test connection pooling")
    void testConnectionPooling() {
        AbstractSqlFactoryDao factory =
                AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

        Connection c1 = factory.getConnection();
        Connection c2 = factory.getConnection();
        Connection c3 = factory.getConnection();
        Connection c4 = factory.getConnection();
        Connection c5 = factory.getConnection();

        assertNotNull(c1);
        assertNotNull(c2);
        assertNotNull(c3);
        assertNotNull(c4);
        assertNotNull(c5);

        assertThrows(
                IllegalStateException.class,
                () -> {
                    factory.getConnection();
                },
                "Expect getConnection to throw error if all pre-existing connections have been used");

        try {
            factory.shutdown();

            assertTrue(c1.isClosed(), "Connection is closed after shutdown of pool");
            assertTrue(c2.isClosed(), "Connection is closed after shutdown of pool");
            assertTrue(c3.isClosed(), "Connection is closed after shutdown of pool");
            assertTrue(c4.isClosed(), "Connection is closed after shutdown of pool");
            assertTrue(c5.isClosed(), "Connection is closed after shutdown of pool");

            assertThrows(
                    IllegalStateException.class,
                    () -> {
                        factory.getConnection();
                    },
                    "Expect getConnection to throw error if shut down");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
