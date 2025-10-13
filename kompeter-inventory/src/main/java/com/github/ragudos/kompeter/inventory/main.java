package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteItemDao;

public class main {
    public static void main(String[] args) {

        SqliteItemDao sqliteItemDao = new SqliteItemDao(SqliteFactoryDao.getInstance().getConnection());
        SqliteInventoryDao sqliteInventoryDao =
                new SqliteInventoryDao(SqliteFactoryDao.getInstance().getConnection());
        InventoryService is = new InventoryService(sqliteItemDao, sqliteInventoryDao);

        System.out.println(is.showInventoryItems());
    }
}
