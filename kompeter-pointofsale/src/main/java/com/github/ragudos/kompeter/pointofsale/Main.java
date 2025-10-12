package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.database.dao.SaleDao;
import com.github.ragudos.kompeter.database.dto.SaleDto;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteSaleDao;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
///test daw later

public class Main {
    public static void main(String[] args) throws SQLException {

        // Use the real SaleDao implementation
        SaleDao saleDao = new SqliteSaleDao();

        Cart cart = new Cart(saleDao);

        // Add items
        cart.addItem(new CartItem(1, "Apple", 2, 10.0));
        cart.addItem(new CartItem(2, "Banana", 3, 5.0));

        // Display cart
        cart.displayCart();

        // Checkout and save to DB
        float total = cart.checkOut();
        System.out.println("Checked out total: " + total);

        // Verify the sale is saved
        SaleDto lastSale = saleDao.get((int) total); // Actually, you should use the transaction ID
        if (lastSale != null) {
            System.out.println("Sale saved in DB: " + lastSale.saleCode());
        } else {
            System.out.println("Sale not found in DB");
        }
    }
}
