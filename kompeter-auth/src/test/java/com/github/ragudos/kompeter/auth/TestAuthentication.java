package com.github.ragudos.kompeter.auth;

import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import java.sql.SQLException;
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

    @Test
    @DisplayName("Test sign up process")
    void testSignUp() {

        try {
            Authentication.signUp(
                    "johndoe@example.com", "Aaron", "123456".toCharArray(), "123456".toCharArray());
        } catch (AuthenticationException e) {
            System.err.println(e.getMessage() + " " + e.errorType.toString());
        }
    }
}
