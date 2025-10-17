/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.migrations;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.migrations.SqlMigration;
import com.github.ragudos.kompeter.database.migrations.SqlMigration.ParsedSqlMigration;
import com.github.ragudos.kompeter.database.seeder.Seeder;
import com.github.ragudos.kompeter.database.sqlite.seeder.SqliteSeeder;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class SqliteMigratorFactory extends AbstractMigratorFactory {
    private static final Logger LOGGER = KompeterLogger.getLogger(SqliteMigratorFactory.class);

    /**
     * Can be refactored but it doesn't really matter.
     *
     * @return A list of migration sql queries arranged by the order of file versions.
     */
    public static @NotNull ArrayList<ParsedSqlMigration> getMigrationQueries() {
        LOGGER.info("Loading sql migrations...");

        try (ScanResult scanResult =
                new ClassGraph()
                        .acceptPackages(SqliteMigrator.class.getPackageName())
                        .acceptPaths("META-INF/sqlite/migrations")
                        .scan()) {
            var queries = new ArrayList<SqlMigration>();
            var pattern = Pattern.compile("^V(\\d+)__(.+)\\.(\\w+)$");

            scanResult
                    .getResourcesWithExtension(".sql")
                    .forEach(
                            (res) -> {
                                try {
                                    queries.add(
                                            new SqlMigration(
                                                    Paths.get(res.getPathRelativeToClasspathElement())
                                                            .getFileName()
                                                            .toString(),
                                                    res.getContentAsString()));
                                } catch (IOException e) {
                                    LOGGER.log(Level.SEVERE, "Failed to read sql file: {0}", e);
                                }
                            });

            var validQueries = new ArrayList<ParsedSqlMigration>();

            for (var sqlMigration : queries) {
                var fileName = sqlMigration.fileName();
                var matcher = pattern.matcher(fileName);

                if (matcher.matches()) {
                    var versionNumber = Integer.parseInt(matcher.group(1));
                    var queryName = matcher.group(2);
                    var extension = matcher.group(3);

                    validQueries.add(
                            new ParsedSqlMigration(
                                    versionNumber, queryName + "." + extension, sqlMigration.query()));
                } else {
                    LOGGER.warning("⚠️ Warning: Invalid migration format: " + fileName + ". skipping...");
                }
            }

            validQueries.sort(Comparator.comparingInt((sqlM) -> sqlM.versionNumber()));

            return validQueries;
        }
    }

    public static String getSeederQuery() {
        try (InputStream stream = SqliteSeeder.class.getResourceAsStream("seeder.sql")) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load seeder query", e);
            return null;
        }
    }

    @Override
    public Migrator getMigrator() {
        return new SqliteMigrator();
    }

    @Override
    public Seeder getSeeder() {
        return new SqliteSeeder();
    }
}
