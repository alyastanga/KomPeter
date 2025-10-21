/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale.observers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeric
 */
public class Observable<T> {
    private final List<CartObserver<T>> observers = new ArrayList<>();

    public void addObserver(CartObserver<T> ob) {
        observers.add(ob);
    }

    public void notifyObserver(String eventType, T data) {
        for (CartObserver<T> ob : observers) {
            ob.onUpdate(eventType, data);
        }
    }

    public void removeObserver(CartObserver<T> ob) {
        observers.remove(ob);
    }
}
