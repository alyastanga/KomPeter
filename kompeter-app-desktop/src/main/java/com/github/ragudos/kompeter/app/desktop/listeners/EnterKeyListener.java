package com.github.ragudos.kompeter.app.desktop.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class EnterKeyListener extends KeyAdapter {
    @FunctionalInterface
    public interface EnterKeyCallback {
        public void onPress(KeyEvent e);
    }

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
}
