/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseDto;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface PurchaseDao {
    // CREATE
    int insertPurchase(
            int suppID,
            Timestamp purchase_date,
            String purch_code,
            Timestamp deliverydate,
            BigDecimal vat_percentage,
            BigDecimal disc_val,
            DiscountType discountType)
            throws SQLException, IOException;

    // READ
    List<PurchaseDto> getAllPurchase() throws SQLException, IOException;
    
    
    
    // UPDATE
    // DELETE
}
