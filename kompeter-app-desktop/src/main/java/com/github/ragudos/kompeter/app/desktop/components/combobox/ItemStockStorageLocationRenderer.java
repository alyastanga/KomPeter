/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.combobox;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockStorageLocationDto;

public class ItemStockStorageLocationRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value == null) {
            label.setText("Select a Location");
            label.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
        } else if (value instanceof final ItemStockStorageLocationDto loc) {
            label.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.foreground;");

            final String endText = loc.isInitialized() ? "" : " (to-be-created)";

            label.setText(String.format("""
                    <html>
                    <body>
                        <p><strong>%s</strong> - %s <em>%s</em></p>
                    </body>
                    </html>
                    """, loc.name(), loc.quantity(), endText));
        }

        return label;
    }
}
