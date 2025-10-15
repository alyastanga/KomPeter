/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.utilities.cache.LRU;

public final class SqliteQueryLoader extends AbstractSqlQueryLoader {
    private static SqliteQueryLoader instance;

    public static synchronized SqliteQueryLoader getInstance() {
        if (instance == null) {
            instance = new SqliteQueryLoader();
        }

        return instance;
    }

    private SqliteQueryLoader() {
        super();

        queryCache = new LRU<>(30);
    }

    @Override
    public String getDatabaseName() {
        return "sqlite";
    }
}
