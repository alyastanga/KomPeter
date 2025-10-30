/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.table;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;

public class ItemStatusText extends JPanel implements TableCellRenderer {
    private final JPanel container;
    private final JLabel label;

    public ItemStatusText() {
        label = new JLabel("");
        container = new JPanel(new BorderLayout());

        label.setAlignmentX(0.5f);
        label.setAlignmentY(0.5f);

        container.add(label, BorderLayout.CENTER);

        container.putClientProperty(FlatClientProperties.STYLE, "arc:999;margin:2,2,2,2;");

        add(container);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        ItemStatus itemStatus = ItemStatus.fromString((String) value);

        switch (itemStatus) {
            case ACTIVE:
                container.putClientProperty(FlatClientProperties.STYLE, "background:$color.success;");
                break;
            case INACTIVE:
                container.putClientProperty(FlatClientProperties.STYLE, "background:$color.gray;");
                break;
            case ARCHIVED:
                container.putClientProperty(FlatClientProperties.STYLE, "background:$color.error;");
                break;
        }

        return this;
    }
}
