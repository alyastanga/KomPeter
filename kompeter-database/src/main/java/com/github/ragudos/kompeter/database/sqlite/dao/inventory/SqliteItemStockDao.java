/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public final class SqliteItemStockDao implements ItemStockDao {
    @Override
    public int insertItemStock(final Connection conn, final int itemId, final int itemBrandId,
            final BigDecimal unit_price,
            final int min_qty)
            throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("insert_item_stock", "item_stocks",
                        AbstractSqlQueryLoader.SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt("_item_id", itemId);
            stmt.setInt("_item_brand_id", itemBrandId);
            stmt.setBigDecimal("unit_price_php", unit_price);
            stmt.setInt("minimum_quantity", min_qty);
            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public void setItemStocksStatusByName(final Connection conn, final String name, final ItemStatus status)
            throws SQLException, IOException {
        try (var stmnt = new NamedPreparedStatement(conn, SqliteQueryLoader.getInstance()
                .get("set_item_stock_status_by_name", "item_stocks", SqlQueryType.UPDATE));) {
            stmnt.setString("name", name);
            stmnt.setString("status", status == null ? null : status.toString().toLowerCase(Locale.ENGLISH));
            stmnt.executeUpdate();
        }
    }

    @Override
    public int updateItemMinimumQtyById(final Connection conn, final int id, final int qty)
            throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("update_item_stock_minQty", "items", SqlQueryType.UPDATE))) {
            stmt.setInt("minimum_quantity", qty);
            stmt.setInt("_item_stock_id", id);

            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateItemUnitPriceById(final Connection conn, final int id, final BigDecimal unitPricePhp)
            throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("update_item_stock_price", "items", SqlQueryType.UPDATE))) {
            stmt.setBigDecimal("unit_price_php", unitPricePhp);
            stmt.setInt("_item_stock_id", id);
            return stmt.executeUpdate();
        }
    }
}
