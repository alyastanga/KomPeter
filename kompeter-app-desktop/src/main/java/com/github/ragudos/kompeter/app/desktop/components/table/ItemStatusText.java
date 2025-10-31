/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;

import net.miginfocom.swing.MigLayout;

public class ItemStatusText extends JPanel implements TableCellRenderer {
    JLabel label;
    private Color bg;
    private Color fg;

    public ItemStatusText() {
        label = new JLabel();
        bg = getBackground();
        fg = getForeground();

        setMinimumSize(new Dimension(68, getMinimumSize().height));

        setAlignmentX(0.5f);
        setAlignmentY(0.5f);

        setOpaque(false);

        label.putClientProperty(FlatClientProperties.STYLE, "arc: 999;border: 6,12,6,12; font: 11 bold;");

        setLayout(new MigLayout("insets 6, al center center"));

        add(label, "");
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        final ItemStatus itemStatus = (ItemStatus) value;

        label.setText(itemStatus == null ? "" : itemStatus.toString().toLowerCase());

        if (isSelected) {
            setOpaque(true);
            bg = getBackground();
            fg = getForeground();
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setOpaque(false);
            setBackground(bg);
            setForeground(fg);
        }

        // for some reason putClientProperties doesnt work
        switch (itemStatus) {
            case ACTIVE :
                label.setBackground(Color.decode("#80EF80"));
                label.setForeground(Color.decode("#084108"));
                break;
            case INACTIVE :
                label.setBackground(Color.decode("#c2bdb9"));
                label.setForeground(Color.decode("#282623"));
                break;
            case ARCHIVED :
                label.setBackground(Color.decode("#FF6961"));
                label.setForeground(Color.decode("#ffccca"));
                break;
        }

        return this;
    }
}
