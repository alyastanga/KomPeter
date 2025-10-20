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
import com.github.ragudos.kompeter.database.dao.inventory.PurchaseItemStockDao;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlitePurchaseItemStockDao implements PurchaseItemStockDao {

    @Override
    public int insertPurchaseItemStock(
            int purchaseId, int itemStockId, int qty_ordered, int qty_received, BigDecimal unit_cost_php)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_purchase_item_stock", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmt.setInt("_purchase_id", purchaseId);
            stmt.setInt("_item_stock_id", itemStockId);
            stmt.setInt("quantity_ordered", qty_ordered);
            stmt.setInt("quantity_received", qty_received);
            stmt.setBigDecimal("unit_cost_php", unit_cost_php);

            stmt.executeUpdate();

            var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public List<PurchaseItemStockDto> getAllData() throws SQLException, IOException {
        List<PurchaseItemStockDto> purchaseItemStockList = new ArrayList<>();
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "select_all_purchase_item_stock",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery(); ) {

            while (rs.next()) {
                PurchaseItemStockDto purchaseItemStock =
                        new PurchaseItemStockDto(
                                rs.getInt("_purchase_item_stock_id"),
                                rs.getInt("_purchase_id"),
                                rs.getInt("_item_stock_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getInt("quantity_ordered"),
                                rs.getInt("quantity_received"),
                                rs.getBigDecimal("unit_cost_php"));

                purchaseItemStockList.add(purchaseItemStock);
            }
        }
        return purchaseItemStockList;
    }

    @Override
    public List<PurchaseItemStockDto> getAllDataByPurchaseId(int purchaseId)
            throws SQLException, IOException {
        List<PurchaseItemStockDto> purchaseItemStockList = new ArrayList<>();
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "select_purchase_item_stock_by_purchaseId",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query); ) {
            stmt.setInt(1, purchaseId);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                PurchaseItemStockDto purchaseItemStock =
                        new PurchaseItemStockDto(
                                rs.getInt("_purchase_item_stock_id"),
                                rs.getInt("_purchase_id"),
                                rs.getInt("_item_stock_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getInt("quantity_ordered"),
                                rs.getInt("quantity_received"),
                                rs.getBigDecimal("unit_cost_php"));

                purchaseItemStockList.add(purchaseItemStock);
            }
        }
        return purchaseItemStockList;
    }

    @Override
    public BigDecimal getPurchaseLineCost(int purchaseId, int itemStockId)
            throws SQLException, IOException {
        BigDecimal cost = new BigDecimal("0.00");
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "select_purchase_line_item_cost",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query); ) {

            stmt.setInt(1, purchaseId);
            stmt.setInt(2, itemStockId);

            var rs = stmt.executeQuery();
            while (rs.next()) {
                cost = rs.getBigDecimal("cost");
            }
        }
        return cost;
    }

    @Override
    public BigDecimal getPurchaseTotalCost(int purchaseId) throws SQLException, IOException {
        BigDecimal cost = new BigDecimal("0.00");
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_purchase_total_cost", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query); ) {
            stmt.setInt(1, purchaseId);

            var rs = stmt.executeQuery();
            while (rs.next()) {
                cost = rs.getBigDecimal("total_cost");
            }
        }
        return cost;
    }
}
