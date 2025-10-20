/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SqliteItemStockDao implements ItemStockDao {

    @Override
    public int insertItemStock(int itemId, int itemBrandId, BigDecimal unit_price, int min_qty)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_item_stock", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmt.setInt("_item_id", itemId);
            stmt.setInt("_item_brand_id", itemBrandId);
            stmt.setBigDecimal("unit_price_php", unit_price);
            stmt.setInt("minimum_quantity", min_qty);

            stmt.executeUpdate();
            var rs = stmt.getPreparedStatement().getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public List<ItemStockDto> getAllData() throws SQLException, IOException {
        List<ItemStockDto> listItemStock = new ArrayList<>();
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_all_item_stocks", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query)) {

            var rs = stmt.executeQuery();
            while (rs.next()) {
                ItemStockDto itemStock =
                        new ItemStockDto(
                                rs.getInt("_item_stock_id"),
                                rs.getInt("_item_id"),
                                rs.getInt("_item_brand_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getBigDecimal("unit_price_php"),
                                rs.getInt("quantity"),
                                rs.getInt("minimum_quantity"));
                listItemStock.add(itemStock);
            }
        }
        return listItemStock;
    }

    @Override
    public Optional<ItemStockDto> getItemStockById(int id) throws SQLException, IOException {
        Optional<ItemStockDto> optionalItemStock = Optional.empty();
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_item_stock_by_id", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            var rs = stmt.executeQuery();
            while (rs.next()) {
                ItemStockDto itemStock =
                        new ItemStockDto(
                                rs.getInt("_item_stock_id"),
                                rs.getInt("_item_id"),
                                rs.getInt("_item_brand_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getBigDecimal("unit_price_php"),
                                rs.getInt("quantity"),
                                rs.getInt("minimum_quantity"));
                optionalItemStock = Optional.of(itemStock);
            }
        }
        return optionalItemStock;
    }

    @Override
    public int deleteItemStockById(int id) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("delete_item_stock_by_id", "items", AbstractSqlQueryLoader.SqlQueryType.DELETE);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            var rs = stmt.executeUpdate();
            return rs;
        }
    }

    @Override
    public int updateItemBrandById(int brandID, int id) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("update_item_brand_by_id", "items", AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt =
                new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setInt("_item_brand_id", brandID);
            stmt.setInt("_item_stock_id", id);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateItemMinimumQtyById(int minimumQty, int id) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("update_item_stock_minQty", "items", AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt =
                new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setInt("minimum_quantity", minimumQty);
            stmt.setInt("_item_stock_id", id);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateItemUnitPriceById(BigDecimal unitPrice, int id)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("update_item_stock_price", "items", AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt =
                new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setBigDecimal("unit_price_php", unitPrice);
            stmt.setInt("_item_stock_id", id);
            return stmt.executeUpdate();
        }
    }
}
