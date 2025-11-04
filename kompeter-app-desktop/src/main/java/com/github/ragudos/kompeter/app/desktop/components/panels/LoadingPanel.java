/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;

public class LoadingPanel extends JPanel {
    public LoadingPanel() {
        setLayout(new MigLayout("insets 0, flowx, wrap"));

        putClientProperty(FlatClientProperties.STYLE, "background: null;");

        final JProgressBar loader = new JProgressBar();

        loader.setIndeterminate(true);
        loader.setString("Loading...");
        loader.setStringPainted(true);

        add(loader, BorderLayout.CENTER);
    }
}
