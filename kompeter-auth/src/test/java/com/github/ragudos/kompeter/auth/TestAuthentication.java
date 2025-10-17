/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.auth;

import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestAuthentication {
    @BeforeAll
    static void migrateFirst() {
        AbstractMigratorFactory factory =
                AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
        Migrator migrator = factory.getMigrator();

        try {
            migrator.migrate();
        } catch (SQLException e) {
            e.printStackTrace();
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
    @DisplayName("Test sign up process")
    void testSignUp() {

        try {
            Authentication.signUp(
                    "Aaron",
                    "Aaron",
                    "Ragudos",
                    "johndoe@example.com",
                    "123456".toCharArray(),
                    "123456".toCharArray());
        } catch (AuthenticationException e) {
            System.err.println(e.getMessage() + " " + e.errorType);
        }
    }
}
