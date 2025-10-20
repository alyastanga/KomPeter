/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale.observers;

import com.github.ragudos.kompeter.pointofsale.Cart;

/**
 * @author jeric
 */
public class CartNotif implements CartObserver<Cart> {

    @Override
    public void onUpdate(String eventType, Cart cart) {
        switch (eventType) {
            case "ITEM_ADDED" -> System.out.println("Item added!");
            case "ITEM_REMOVED" -> System.out.println("Item removed!");
            case "ITEM_UPDATED" -> System.out.println("Item quantity changed!");
            case "CART_CLEARED" -> System.out.println("Cart has been cleared.");
            default -> System.out.println("Cart updated: " + eventType);
        }
    }
}
