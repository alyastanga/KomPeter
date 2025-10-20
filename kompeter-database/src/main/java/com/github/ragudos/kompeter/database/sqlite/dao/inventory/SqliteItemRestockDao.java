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
import com.github.ragudos.kompeter.database.dao.inventory.ItemRestockDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemRestockDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqliteItemRestockDao implements ItemRestockDao {

    @Override
    public int insertItemRestock(int itemStockId, int qty_before, int qty_after, int qty_added)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_item_restock", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmt.setInt("_item_stock_id", itemStockId);
            stmt.setInt("quantity_before", qty_before);
            stmt.setInt("quantity_after", qty_after);
            stmt.setInt("quantity_added", qty_added);
            stmt.executeUpdate();

            var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public List<ItemRestockDto> getAllData() throws SQLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int updateRestockQtyAfterById(int qtyAfter, int id) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "update_restock_qtyAfter_by_id",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt =
                new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setInt("quantity_after", qtyAfter);
            stmt.setInt("_item_restock_id", id);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateRestockQtyBeforeById(int qtyBefore, int id) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "update_restock_qtyBefore_by_id",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt =
                new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setInt("quantity_before", qtyBefore);
            stmt.setInt("_item_restock_id", id);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateRestockQtyAddedById(int qtyAdded, int id) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "update_restock_qtyAdded_by_id",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt =
                new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setInt("quantity_added", qtyAdded);
            stmt.setInt("_item_restock_id", id);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int deleteRestockById(int id) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("delete_item_restock_by_id", "items", AbstractSqlQueryLoader.SqlQueryType.DELETE);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            var rs = stmt.executeUpdate();
            return rs;
        }
    }
}
