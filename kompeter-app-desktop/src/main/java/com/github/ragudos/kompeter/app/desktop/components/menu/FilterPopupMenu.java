/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.inventory.Inventory;
import com.github.ragudos.kompeter.inventory.InventoryException;

public abstract class FilterPopupMenu extends JPopupMenu implements ItemListener, ActionListener {

    private static void removeAllItemListeners(final FilterPopupMenu menu) {
        for (final Component component : menu.getComponents()) {
            if (component instanceof final JMenuItem item) {
                for (final ItemListener l : item.getItemListeners()) {
                    item.removeItemListener(l);
                }
            }
        }
    }

    protected Runnable listener;

    protected JButton trigger;

    public FilterPopupMenu(final Runnable listener) {
        this.listener = listener;
        trigger = new JButton(new SVGIconUIColor("filter.svg", 0.5f, "foreground.background"));

        trigger.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);
        trigger.putClientProperty(FlatClientProperties.STYLE, "font:11;");

        trigger.addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        show(trigger, 0, trigger.getHeight());
    }

    public abstract void populate();

    public JButton trigger() {
        return trigger;
    }

    public static class CategoryBrandFilterPopupMenu extends FilterPopupMenu {
        public final AtomicReference<ArrayList<String>> categoryFilters;
        public final AtomicReference<ArrayList<String>> brandFilters;

        public CategoryBrandFilterPopupMenu(final Runnable listener) {
            super(listener);

            categoryFilters = new AtomicReference<>(new ArrayList<>());
            brandFilters = new AtomicReference<>(new ArrayList<>());
        }

        @Override
        public void populate() {
            final Inventory inventory = Inventory.getInstance();

            try {
                final String[] itemBrands = inventory.getAllItemBrands();
                final String[] itemCategories = inventory.getAllItemCategories();
                final ArrayList<String> categoryFilters = this.categoryFilters.getAcquire();
                final ArrayList<String> brandFilters = this.brandFilters.getAcquire();

                removeAllItemListeners(this);
                removeAll();

                add(new JLabel("Categories"));

                for (final String category : itemCategories) {
                    final JCheckBox c = new JCheckBox(category);

                    if (categoryFilters.contains(category)) {
                        c.setSelected(true);
                    }

                    c.setName("category");
                    c.addItemListener(this);
                    add(c);
                }

                addSeparator();
                add(new JLabel("Brands"));

                for (final String brand : itemBrands) {
                    final JCheckBox c = new JCheckBox(brand);

                    if (brandFilters.contains(brand)) {
                        c.setSelected(true);
                    }

                    c.setName("brand");
                    c.addItemListener(this);
                    add(c);
                }
            } catch (final InventoryException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), e.getMessage(),
                            "Failed to load categories and brands.", JOptionPane.ERROR_MESSAGE);
                });
            }
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            final JCheckBox c = (JCheckBox) e.getItemSelectable();
            final String text = c.getText();
            final String name = c.getName();

            final ArrayList<String> brandFilters = this.brandFilters.getAcquire();
            final ArrayList<String> categoryFilters = this.categoryFilters.getAcquire();

            switch (e.getStateChange()) {
                case ItemEvent.DESELECTED -> {
                    if (name.equals("brand")) {
                        brandFilters.remove(text);
                    } else if (name.equals("category")) {
                        categoryFilters.remove(text);
                    }
                }
                case ItemEvent.SELECTED -> {
                    if (name.equals("brand")) {
                        brandFilters.add(text);
                    } else if (name.equals("category")) {
                        categoryFilters.add(text);
                    }
                }
            }

            listener.run();
        }
    }
}
