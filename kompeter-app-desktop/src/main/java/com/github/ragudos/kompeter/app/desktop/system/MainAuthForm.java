/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.system;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class MainAuthForm extends JPanel {
    public MainAuthForm() {
        init();
    }

    public void setForm(Form form) {
        removeAll();
        add(form, BorderLayout.CENTER);
        repaint();
        revalidate();
    }

    private void init() {
        setLayout(new BorderLayout());
    }
}
