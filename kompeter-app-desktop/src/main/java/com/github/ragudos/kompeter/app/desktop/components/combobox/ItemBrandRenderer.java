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
import javax.swing.UIManager;

import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;

public class ItemBrandRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
            final boolean isSelected, final boolean cellHasFocus) {
        final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (index == -1 && value == null) {
            label.setText("Select a Brand");
            label.setForeground(UIManager.getColor("Label.disabledForeground"));
        } else {
            final ItemBrandDto v = (ItemBrandDto) value;

            label.setText(v.getName());
        }

        return label;
    }
}
