/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.panels;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

public class NoResultsPanel extends JPanel {
    public NoResultsPanel() {
        setLayout(new BorderLayout());

        putClientProperty(FlatClientProperties.STYLE, "background: null;");

        final JLabel noResults = new JLabel(HtmlUtils.wrapInHtml("No results were found :("));
        add(noResults, BorderLayout.CENTER);
    }
}
