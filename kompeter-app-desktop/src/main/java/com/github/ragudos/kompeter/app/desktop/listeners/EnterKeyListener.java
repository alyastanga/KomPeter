/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class EnterKeyListener extends KeyAdapter {
    private EnterKeyCallback fn;

    public EnterKeyListener(EnterKeyCallback fn) {
        this.fn = fn;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            fn.onPress(e);
        }
    }

    @FunctionalInterface
    public interface EnterKeyCallback {
        void onPress(KeyEvent e);
    }
}
