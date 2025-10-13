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

        // 1️⃣ Create a fake SaleDao (or mock)
        SaleDao saleDao = new SqliteSaleDao(); // or null if you only want to test printReceipt()
        // 2️⃣ Create a new cart and add items
        Cart cart = new Cart(saleDao);
        cart.addItem(new CartItem(101, "Mouse", 2, 350f));
        cart.addItem(new CartItem(102, "Keyboard", 1, 850f));
        cart.addItem(new CartItem(103, "Monitor 24\"", 1, 5250f));
        // 3️⃣ Perform checkout (this creates the transaction)
        Transaction transaction = new Transaction(cart, saleDao);
        // 4️⃣ Print the receipt to console
        transaction.printReciept();
        
        // 5️⃣ Optional: Save to DB
        // transaction.saveToDatabase();    
    }
}
