/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.sales;

import com.github.ragudos.kompeter.database.dto.sales.SaleDto;
import java.sql.SQLException;
import java.util.List;

public interface SaleDao {
    int saveTransaction(SaleDto sale) throws SQLException;

    SaleDto getTransaction(int saleId) throws SQLException;

    List<SaleDto> getAllTransaction() throws SQLException;
}
