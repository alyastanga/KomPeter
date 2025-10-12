package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.database.dao.SaleDao;
import com.github.ragudos.kompeter.database.dto.SaleDto;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

class Transaction {

    static int transCounter = 0;
    ArrayList<CartItem> cartItems = new ArrayList<>();
    int TransID;
    LocalDateTime TimeStamp;
    float Total;
    SaleDao saleDao;

    Transaction(Cart cart, SaleDao saleDao) {
        this.saleDao = saleDao;
        this.TransID = ++transCounter;
        this.TimeStamp = LocalDateTime.now();
        for (CartItem item : cart.cI) {
            CartItem copy = new CartItem(item.productID(), item.productName(), item.qty(), item.price());
            cartItems.add(copy);
        } /// Copies all the transaction and stores in a differnt array
        this.Total = cart.getCartTotal();
    }
    
    void saveToDatabase() throws SQLException {
        saleDao.save(toSaleDto());
    }
    
    SaleDto toSaleDto(){
        return new SaleDto(
                TransID,
                Timestamp.valueOf(TimeStamp),
                Timestamp.valueOf(TimeStamp),
                "SALE-" + TransID,
                "Walk-in Customer",
                BigDecimal.valueOf(0.12),
                BigDecimal.ZERO,
                DiscountType.FIXED
        );
    }
}
