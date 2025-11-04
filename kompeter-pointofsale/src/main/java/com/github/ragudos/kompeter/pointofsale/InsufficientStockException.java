/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

public class InsufficientStockException extends Exception {
    public InsufficientStockException() {
        this("Insufficient stock");
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
