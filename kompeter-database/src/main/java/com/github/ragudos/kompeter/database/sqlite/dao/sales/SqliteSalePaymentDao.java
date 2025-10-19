/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.sales;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.sales.SalePaymentDao;
import com.github.ragudos.kompeter.database.dto.sales.SaleItemStockDto;
import com.github.ragudos.kompeter.database.dto.sales.SalePaymentDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class SqliteSalePaymentDao implements SalePaymentDao {

    @Override
    public int savePayment(SalePaymentDto salePayment) throws SQLException {
        int result;
        String sqlFileName = "insert_payment";
        String query;

        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(sqlFileName, "transaction", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        } catch (IOException e) {
            throw new SQLException("Error!");
        }

        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, salePayment._saleId());
            ps.setTimestamp(2, salePayment._createdAt());
            ps.setTimestamp(3, salePayment.paymentDate());
            ps.setString(4, salePayment.referenceNumber());
            ps.setString(5, salePayment.paymentMethod().name().toUpperCase());
            ps.setBigDecimal(6, salePayment.amountPhp());

            result = ps.executeUpdate();
        }
        return result;
    }

    @Override
    public List<SaleItemStockDto> getRevenue() {
        return null;
    }

    @Override
    public List<SaleItemStockDto> getRevenue(Timestamp from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SaleItemStockDto> getRevenue(Timestamp from, Timestamp to) {
        // TODO Auto-generated method stub
        return null;
    }
}
