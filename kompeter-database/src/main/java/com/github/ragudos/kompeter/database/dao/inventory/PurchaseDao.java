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
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface PurchaseDao {
    //CREATE
    void insertPurchase(
            int suppID, 
            Timestamp purchase_date, 
            String purch_code,
            Timestamp deliverydate, 
            float vat_percentage, 
            float disc_val, 
            DiscountType discountType) throws SQLException, IOException;
    //READ
    List<PurchaseDto> getAllPurchase() throws SQLException, IOException;
    //UPDATE
    //DELETE
}
    