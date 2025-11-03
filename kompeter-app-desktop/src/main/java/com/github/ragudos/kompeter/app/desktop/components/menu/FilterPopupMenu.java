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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.inventory.Inventory;
import com.github.ragudos.kompeter.inventory.InventoryException;

import net.miginfocom.swing.MigLayout;

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

    public static class StatusFilterPopupMenu extends FilterPopupMenu implements ItemListener, PopupMenuListener {
        private final ButtonGroup buttonGroup;
        private final SVGIconUIColor chevronDown;
        private final SVGIconUIColor chevronUp;
        private final AtomicReference<ItemStatus> chosenStatus;

        public StatusFilterPopupMenu(final Runnable listener) {
            super(listener);

            setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, center, fill]"));

            chosenStatus = new AtomicReference<>(null);
            chevronDown = new SVGIconUIColor("chevron-down.svg", 0.75f, "foreground.background");
            chevronUp = new SVGIconUIColor("chevron-up.svg", 0.75f, "foreground.background");
            buttonGroup = new ButtonGroup();

            trigger.setText("Showing:");
            trigger.setIcon(chevronDown);

            addPopupMenuListener(this);
        }

        public ItemStatus chosenStatus() {
            return chosenStatus.get();
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            final JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getItemSelectable();

            if (menuItem.getActionCommand().equals("ALL")) {
                chosenStatus.setRelease(null);
            } else if (menuItem.getActionCommand().equals(ItemStatus.ARCHIVED.toString())) {
                chosenStatus.setRelease(ItemStatus.ARCHIVED);
            } else if (menuItem.getActionCommand().equals(ItemStatus.ACTIVE.toString())) {
                chosenStatus.setRelease(ItemStatus.ACTIVE);
            } else if (menuItem.getActionCommand().equals(ItemStatus.INACTIVE.toString())) {
                chosenStatus.setRelease(ItemStatus.INACTIVE);
            }

            listener.run();

            SwingUtilities.invokeLater(() -> {
                trigger.setText(String.format("Showing: %s", buttonGroup.getSelection().getActionCommand()));
            });
        }

        @Override
        public void populate() {
            removeAllItemListeners(this);
            removeAll();

            final JCheckBoxMenuItem allButton = new JCheckBoxMenuItem("ALL");

            allButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            allButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            allButton.setToolTipText("Filter table to show items: ALL");
            allButton.setActionCommand("ALL");

            if (chosenStatus.get() == null) {
                allButton.setSelected(true);
            }

            allButton.addItemListener(this);

            buttonGroup.add(allButton);

            add(allButton, "growx");

            for (final ItemStatus status : ItemStatus.values()) {
                final JCheckBoxMenuItem statusButton = new JCheckBoxMenuItem(status.toString());

                statusButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
                statusButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
                statusButton.setToolTipText(String.format("Filter table to show all: %s", status));
                statusButton.setActionCommand(status.toString());

                if (status == chosenStatus.get()) {
                    statusButton.setSelected(true);
                }

                statusButton.addItemListener(this);

                buttonGroup.add(statusButton);

                add(statusButton, "growx");
            }

            trigger.setText(String.format("Showing: %s", buttonGroup.getSelection().getActionCommand()));
        }

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
            trigger.setIcon(chevronDown);
        }

        @Override
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            trigger.setIcon(chevronDown);
        }

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            trigger.setIcon(chevronUp);
        }
    }
}
