package com.github.ragudos.kompeter.database.sqlite.migrations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.SqlScriptParser;
import com.github.ragudos.kompeter.database.SqlScriptParser.SqlStatement;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.migrations.SqlMigration.ParsedSqlMigration;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class SqliteMigrator implements Migrator {
    private static final Logger LOGGER = KompeterLogger.getLogger(SqliteMigrator.class);

    private static final String QUERY_CREATE_MIGRATION_TABLE_IF_NOT_EXISTS = """
            CREATE TABLE IF NOT EXISTS migrations (
            	_migration_id INTEGER PRIMARY KEY AUTOINCREMENT,
            	version_number INTEGER NOT NULL CHECK(version_number > 0),
            	name TEXT NOT NULL
            );
            """;
    private static final String QUERY_CHECK_MIGRATION_EXISTS = """
            SELECT EXISTS (
            	SELECT 1 FROM migrations
            	WHERE version_number = ? AND name = ?
            );
            """;
    private static final String QUERY_INSERT_MIGRATION = """
            INSERT INTO migrations (version_number, name)
            VALUES (?, ?);
            """;

    private void createMigrationTableIfNotExists() throws SQLException {
        LOGGER.info("Ensuring migration table exists...");

        try (Connection conn = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE).getConnection();
                PreparedStatement statement = conn.prepareStatement(QUERY_CREATE_MIGRATION_TABLE_IF_NOT_EXISTS);) {
            statement.executeUpdate();
        }
    }

    @Override
    public String getMigrationsAsAWhole() {
        return null;
    }

    @Override
    public void migrate() throws SQLException {
        final List<ParsedSqlMigration> migrations = SqliteMigratorFactory.getMigrationQueries();

        LOGGER.info("Starting database migration...");

        createMigrationTableIfNotExists();

        try (Connection conn = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE).getConnection();) {
            conn.setAutoCommit(false);

            for (final ParsedSqlMigration queryMigration : migrations) {
                final PreparedStatement checkIfMigrationExistsStatement = conn
                        .prepareStatement(QUERY_CHECK_MIGRATION_EXISTS);

                checkIfMigrationExistsStatement.setInt(1, queryMigration.versionNumber());
                checkIfMigrationExistsStatement.setString(2, queryMigration.name());

                final ResultSet rs = checkIfMigrationExistsStatement.executeQuery();

                if (rs.next() && rs.getBoolean(1)) {
                    continue;
                }

                try (PreparedStatement insertMigrationStatement = conn.prepareStatement(QUERY_INSERT_MIGRATION);) {
                    for (final String rawSqlStatement : splitStatements(queryMigration.query())) {
                        final String trimmed = rawSqlStatement.trim();

                        if (trimmed.isEmpty()) {
                            continue;
                        }

                        try (PreparedStatement rawSqlQueryStatement = conn.prepareStatement(trimmed)) {
                            final SqlStatement parsedSqlStatement = SqlScriptParser.parseSqlStatement(trimmed);

                            LOGGER.log(Level.INFO, "Type: {0} | Name: {1}\n\n-----\n {2} \n-----\n\n",
                                    new Object[] { parsedSqlStatement.type(), queryMigration.name(), trimmed });

                            rawSqlQueryStatement.execute();
                        }
                    }

                    insertMigrationStatement.setInt(1, queryMigration.versionNumber());
                    insertMigrationStatement.setString(2, queryMigration.name());
                    insertMigrationStatement.executeUpdate();

                    conn.commit();
                } catch (final SQLException err) {
                    conn.rollback();

                    LOGGER.log(Level.SEVERE, "Failed to execute migration {0} - {1}: {2}",
                            new Object[] { queryMigration.versionNumber(), queryMigration.name(), err });
                    throw err;
                }
            }
        }
    }

    private List<String> splitStatements(final String sqlScript) {
        final List<String> statements = new ArrayList<>();
        final StringBuilder current = new StringBuilder();
        final String currentDelimiter = ";";
        boolean isInTrigger = false;

        for (final String rawLine : sqlScript.split("\\R")) {
            final String line = rawLine.trim();

            // Change delimiter if directive is found

            if (line.toUpperCase().startsWith("CREATE TRIGGER")) {
                isInTrigger = true;
            }

            current.append(rawLine).append("\n");

            if (isInTrigger) {
                if (line.toUpperCase().startsWith("END")) {
                    isInTrigger = false;
                } else {
                    continue;
                }
            }

            final String trimmed = current.toString().replaceAll("\\s+$", "");

            if (trimmed.endsWith(currentDelimiter) && !isInTrigger) {
                final String statement = trimmed.substring(0, trimmed.length() - currentDelimiter.length()).trim();

                if (!statement.isEmpty()) {
                    statements.add(statement);
                }

                current.setLength(0); // Reset for next statement
            }
        }

        if (!current.toString().trim().isEmpty()) {
            statements.add(current.toString().trim());
        }

        return statements;
    }

}
