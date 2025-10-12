package com.github.ragudos.kompeter.pointofsale;

public record CartItem(
     int productID,
     String productName,
     int qty, 
     double price
     ) {

    public double getTotalPrice() {
        return qty * price;
    }
}
