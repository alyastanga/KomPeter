/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale.observers;

/**
 * @author jeric
 */
public interface CartObserver<T> {
    void onUpdate(String eventType, T data);
}
