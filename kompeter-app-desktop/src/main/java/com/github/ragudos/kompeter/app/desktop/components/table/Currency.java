/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.table;

import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.github.ragudos.kompeter.utilities.StringUtils;

public class Currency extends DefaultTableCellRenderer {
    private BigDecimal value;

    public Currency() {
        setHorizontalAlignment(CENTER);

        setPreferredSize(new Dimension(100, -1));
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        if (value == null) {
            this.value = new BigDecimal("0.00");
            return super.getTableCellRendererComponent(table, StringUtils.formatBigDecimal(this.value), isSelected,
                    hasFocus, row, column);
        }

        this.value = value instanceof BigDecimal ? (BigDecimal) value : new BigDecimal((String) value);

        return super.getTableCellRendererComponent(table, StringUtils.formatBigDecimal(this.value), isSelected,
                hasFocus, row, column);
    }

    public BigDecimal value() {
        return value;
    }
}
