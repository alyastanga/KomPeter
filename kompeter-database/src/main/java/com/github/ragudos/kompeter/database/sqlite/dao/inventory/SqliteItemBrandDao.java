/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.ItemBrandDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SqliteItemBrandDao implements ItemBrandDao {
    private final Connection conn;

    public SqliteItemBrandDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insertItemBrand(String name, String description) throws SQLException, IOException {
        var query=
                SqliteQueryLoader.getInstance()
                        .get(
                        "insert_item_brand",
                        "items",
                        AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try(var stmt = conn.prepareStatement(query);){
            stmt.setString(1, name);
            stmt.setString(2, description);
            
            var insert = stmt.executeUpdate();
            
        }
    }
}
