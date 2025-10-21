/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.sales;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.dao.sales.SaleItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class SqliteSaleItemStockDao implements SaleItemStockDao {
    @Override
    public void createSaleItemStock(@NotNull Connection conn, @Range(from = 0, to = 2147483647) int _saleId,
            @Range(from = 0, to = 2147483647) int _itemStockId, @Range(from = 0, to = 2147483647) int quantity,
            @NotNull BigDecimal unitPricePhp) throws IOException, SQLException {
        try (PreparedStatement stmnt = conn.prepareStatement(SqliteQueryLoader.getInstance()
                .get("create_sale_item_stock", "sale_item_stocks", SqlQueryType.INSERT))) {
            stmnt.setInt(1, _saleId);
            stmnt.setInt(2, _itemStockId);
            stmnt.setInt(3, quantity);
            stmnt.setBigDecimal(4, unitPricePhp);

            stmnt.executeUpdate();
        }
    }
}
