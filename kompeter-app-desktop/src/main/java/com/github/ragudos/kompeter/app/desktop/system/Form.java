/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.system;

import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Form extends JPanel {
    private LookAndFeel oldTheme = UIManager.getLookAndFeel();

    public void formAfterOpen() {
    }

    public final void formCheck() {
        if (oldTheme != UIManager.getLookAndFeel()) {
            oldTheme = UIManager.getLookAndFeel();
            SwingUtilities.updateComponentTreeUI(this);
        }
    }

    /**
     * For operations with asynchronous operations.
     * 
     * @param cb - a cb that accepts a predicate put by the caller
     * 
     */
    public void formBeforeClose(FormBeforeCloseCallback cb) {
        cb.beforeClose(true);
    }

    public void formInit() {
    }

    public void formOpen() {
    }

    public void formRefresh() {
    }

    @FunctionalInterface
    public interface FormBeforeCloseCallback {
        void beforeClose(boolean predicate);
    }
}
