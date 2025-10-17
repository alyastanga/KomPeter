/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.database.sqlite.dao.sales.SqliteSaleDao;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        SqliteSaleDao saledao = new SqliteSaleDao();
        Cart cart = new Cart(saledao);

        cart.addItem(new CartItem(1, "SSD 1TB", 2, 3500.00));

        cart.checkOut();
    }
}
