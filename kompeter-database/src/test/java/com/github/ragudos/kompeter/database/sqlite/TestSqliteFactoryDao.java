/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.seeder.Seeder;
import com.github.ragudos.kompeter.utilities.constants.Metadata;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestSqliteFactoryDao {
    @BeforeAll
    static void testMigration() {
        try {
            AbstractMigratorFactory factory =
                    AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
            Migrator migrator = factory.getMigrator();
            Seeder seeder = factory.getSeeder();

            migrator.migrate();
            seeder.seed();
        } catch (Exception e) {
            e.printStackTrace();

            if (Metadata.APP_ENV.equals("development")) {
                assert false : "Migration failed";
            }
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
        assertNotNull(c1);

        assertThrows(
                IllegalStateException.class,
                () -> {
                    factory.getConnection();
                },
                "Expect getConnection to throw error if all pre-existing connections have been used");

        try {
            factory.shutdown();

            assertTrue(c1.isClosed(), "Connection is closed after shutdown of pool");

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
