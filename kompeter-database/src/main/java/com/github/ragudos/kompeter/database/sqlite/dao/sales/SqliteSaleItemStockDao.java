/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.sales;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.sales.SaleItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteSaleItemStockDao implements SaleItemStockDao {
    @Override
    public void createSaleItemStock(@NotNull final Connection conn, @Range(from = 0, to = 2147483647) final int _saleId,
            @Range(from = 0, to = 2147483647) final int _itemStockId,
            @Range(from = 0, to = 2147483647) final int quantity, @NotNull final BigDecimal unitPricePhp)
            throws IOException, SQLException {
        try (NamedPreparedStatement stmnt = new NamedPreparedStatement(conn, SqliteQueryLoader.getInstance()
                .get("create_sale_item_stock", "sale_item_stocks", SqlQueryType.INSERT))) {
            stmnt.setInt("_sale_id", _saleId);
            stmnt.setInt("_item_stock_id", _itemStockId);
            stmnt.setInt("quantity", quantity);
            stmnt.setBigDecimal("unit_price_php", unitPricePhp);

            stmnt.executeUpdate();
        }
    }
}
