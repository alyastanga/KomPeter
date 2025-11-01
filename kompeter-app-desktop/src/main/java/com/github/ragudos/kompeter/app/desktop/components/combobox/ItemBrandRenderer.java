package com.github.ragudos.kompeter.app.desktop.components.combobox;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class ItemBrandRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected,
            final boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
