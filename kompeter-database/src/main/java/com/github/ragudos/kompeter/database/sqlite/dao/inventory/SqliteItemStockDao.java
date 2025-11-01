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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public final class SqliteItemStockDao implements ItemStockDao {

    @Override
    public List<ItemStockDto> getAllData() throws SQLException, IOException {
        final List<ItemStockDto> listItemStock = new ArrayList<>();
        final var query = SqliteQueryLoader.getInstance().get("select_all_item_stocks", "items",
                AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection(); var stmt = conn.prepareStatement(query)) {

            final var rs = stmt.executeQuery();
            while (rs.next()) {
                final ItemStockDto itemStock = new ItemStockDto(rs.getInt("_item_stock_id"), rs.getInt("_item_id"),
                        rs.getInt("_item_brand_id"), rs.getTimestamp("_created_at"), rs.getBigDecimal("unit_price_php"),
                        rs.getInt("quantity"), rs.getInt("minimum_quantity"));
                listItemStock.add(itemStock);
            }
        }
        return listItemStock;
    }

    @Override
    public Optional<ItemStockDto> getItemStockById(final int id) throws SQLException, IOException {
        Optional<ItemStockDto> optionalItemStock = Optional.empty();
        final var query = SqliteQueryLoader.getInstance().get("select_item_stock_by_id", "items",
                AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection(); var stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            final var rs = stmt.executeQuery();
            while (rs.next()) {
                final ItemStockDto itemStock = new ItemStockDto(rs.getInt("_item_stock_id"), rs.getInt("_item_id"),
                        rs.getInt("_item_brand_id"), rs.getTimestamp("_created_at"), rs.getBigDecimal("unit_price_php"),
                        rs.getInt("quantity"), rs.getInt("minimum_quantity"));
                optionalItemStock = Optional.of(itemStock);
            }
        }
        return optionalItemStock;
    }

    @Override
    public int insertItemStock(final int itemId, final int itemBrandId, final BigDecimal unit_price, final int min_qty)
            throws SQLException, IOException {
        final var query = SqliteQueryLoader.getInstance().get("insert_item_stock", "items",
                AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt = new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query,
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt("_item_id", itemId);
            stmt.setInt("_item_brand_id", itemBrandId);
            stmt.setBigDecimal("unit_price_php", unit_price);
            stmt.setInt("minimum_quantity", min_qty);

            stmt.executeUpdate();
            final var rs = stmt.getPreparedStatement().getGeneratedKeys();
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
    public int updateItemMinimumQtyById(Connection conn, int id, int qty) throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("update_item_stock_minQty", "items", SqlQueryType.UPDATE))) {
            stmt.setInt("minimum_quantity", qty);
            stmt.setInt("_item_stock_id", id);

            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateItemUnitPriceById(Connection conn, int id, BigDecimal unitPricePhp)
            throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("update_item_stock_price", "items", SqlQueryType.UPDATE))) {
            stmt.setBigDecimal("unit_price_php", unitPricePhp);
            stmt.setInt("_item_stock_id", id);
            return stmt.executeUpdate();
        }
    }
}
