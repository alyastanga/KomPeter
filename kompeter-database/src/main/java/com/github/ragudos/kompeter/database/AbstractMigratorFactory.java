/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database;

import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.seeder.Seeder;
import com.github.ragudos.kompeter.database.sqlite.migrations.SqliteMigratorFactory;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.logging.Logger;

public abstract class AbstractMigratorFactory {
    protected static final Logger LOGGER = KompeterLogger.getLogger(AbstractMigratorFactory.class);
    public static final int SQLITE = 1;

    public static AbstractMigratorFactory getMigrator(int type) {
        return switch (type) {
            case SQLITE -> new SqliteMigratorFactory();
            default -> throw new IllegalArgumentException("Invalid database type: " + type);
        };
    }

    public abstract Migrator getMigrator();

    public abstract Seeder getSeeder();
}
