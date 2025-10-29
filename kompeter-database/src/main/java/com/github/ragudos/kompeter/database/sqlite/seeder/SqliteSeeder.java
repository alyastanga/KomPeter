/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.seeder;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.seeder.Seeder;
import com.github.ragudos.kompeter.database.sqlite.migrations.SqliteMigratorFactory;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.SQLiteException;

public class SqliteSeeder implements Seeder {
    private static final Logger LOGGER = KompeterLogger.getLogger(SqliteSeeder.class);

    @Override
    public void seed() throws SQLException {
        String query = SqliteMigratorFactory.getSeederQuery();
        AbstractSqlFactoryDao factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

        try (Connection conn = factory.getConnection();
                Statement stmnt = conn.createStatement()) {
            conn.setAutoCommit(false);

            String[] queries = Arrays.stream(query.split(";"))
                    .filter((s) -> !s.isBlank())
                    .map((s) -> s.trim())
                    .toArray(String[]::new);

            for (int i = 0; i < queries.length; ++i) {
                LOGGER.info("Seeding... \n\n-----\n" + queries[i] + "\n-----\n\n");

                stmnt.executeUpdate(queries[i]);
                try {
                    conn.commit();
                } catch (SQLException e) {
                    try {
                        conn.rollback();
                    } catch (SQLiteException e2) {
                        e.addSuppressed(e2);
                    }

                    LOGGER.log(Level.SEVERE, "Failed to seed table!", e);
                }
            }
        }
    }
}
