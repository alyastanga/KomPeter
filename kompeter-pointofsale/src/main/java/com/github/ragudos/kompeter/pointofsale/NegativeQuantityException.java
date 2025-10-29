/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

public class NegativeQuantityException extends Exception {
    public NegativeQuantityException() {
        this("Quantity cannot exceed below 0.");
    }

    public NegativeQuantityException(String message) {
        super(message);
    }
}
