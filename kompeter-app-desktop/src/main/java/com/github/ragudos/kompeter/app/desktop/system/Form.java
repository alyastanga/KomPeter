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

    public void formInit() {
    }

    public void formOpen() {
    }

    public void formRefresh() {
    }
}
