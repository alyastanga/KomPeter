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
import java.awt.Dimension;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.OverlayLayout;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockStorageLocationDto;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import lombok.ToString;

public class PercentageBar extends JPanel implements TableCellRenderer {
    private static final Logger LOGGER = KompeterLogger.getLogger(PercentageBar.class);

    final JProgressBar bar;

    final JLabel label;

    public PercentageBar() {
        setOpaque(false);

        setLayout(new OverlayLayout(this));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setMinimumSize(new Dimension(100, getMinimumSize().height));

        putClientProperty(FlatClientProperties.STYLE, "background: null;");

        label = new JLabel("");

        label.putClientProperty(FlatClientProperties.STYLE, "font: -2 bold;");
        bar = new JProgressBar(0, 100);

        label.setAlignmentX(0.5f);
        label.setAlignmentY(0.5f);

        add(label);

        final JPanel container = new JPanel(new BorderLayout());

        container.setOpaque(false);
        container.add(bar, BorderLayout.CENTER);

        add(container);
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        if (isSelected) {
            setOpaque(true);
            setBackground(table.getSelectionBackground());
        } else {
            setOpaque(false);
            setBackground(table.getBackground());
        }

        if (!PercentageBarData.isPercentageBarData(value)) {
            LOGGER.severe("Invalid percentage bar data! \n" + value);

            return this;
        }

        final PercentageBarData percentageBarData = (PercentageBarData) value;
        final float c = (float) percentageBarData.currentValue;
        final float m = (float) percentageBarData.minimumThreshold;

        final float percentage = (c - m) / (m * 10f - m);
        String text = percentageBarData.currentValue + " " + percentageBarData.measure;

        if (percentage <= 0.1f) {
            text += " (Critically Low)";
            bar.putClientProperty(FlatClientProperties.STYLE, "foreground:$color.error;");
        } else if (percentage <= 0.49f) {
            text += " (Low)";
            bar.putClientProperty(FlatClientProperties.STYLE, "foreground:$color.warning;");
        } else if (percentage <= 0.89f) {
            text += " (Mid)";
            bar.putClientProperty(FlatClientProperties.STYLE, "foreground:$color.info;");
        } else {
            text += " (High)";
            bar.putClientProperty(FlatClientProperties.STYLE, "foreground:$color.success;");
        }

        LOGGER.info(percentageBarData.toString());

        label.setText(text);
        bar.setValue(percentageBarData.currentValue);
        bar.setMaximum(percentageBarData.minimumThreshold * 10);

        repaint();
        revalidate();

        return this;
    }

    public static class ItemStockQtyPercentageBar extends PercentageBar {
        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                final boolean hasFocus, final int row, final int column) {
            final Component res = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return res;
        }
    }

    public static class ItemStockQtyPercentageBarData extends PercentageBarData {
        ItemStockStorageLocationDto[] locations;

        public ItemStockQtyPercentageBarData(final int id, final int currentValue, final int minimumThreshold,
                final String measure, final ItemStockStorageLocationDto[] locations) {
            super(id, currentValue, minimumThreshold, measure);

            this.locations = locations;
        }

        /**
         * @return array of string like <location name> - <quantity>
         */
        public String[] getLocationQtyData() {
            return Arrays.stream(locations).map((loc) -> {
                final String end = loc.isInitialized() ? "" : " (to-be-created)";

                return String.join(" - ", loc.name(), loc.quantity() + " item/s" + end);
            }).toArray(String[]::new);
        }

        public ItemStockStorageLocationDto[] locations() {
            return locations;
        }
    }

    @ToString
    public static class PercentageBarData {
        public static boolean isPercentageBarData(final Object obj) {
            return (obj != null && obj instanceof final PercentageBarData data) && data.currentValue >= 0
                    && data.minimumThreshold >= 0;
        }

        int currentValue;
        int id;
        String measure;

        int minimumThreshold;

        public PercentageBarData(final int id, final int currentValue, final int minimumThreshold,
                final String measure) {
            this.id = id;
            this.currentValue = currentValue;
            this.minimumThreshold = minimumThreshold;
            this.measure = measure;
        }

        public int currentValue() {
            return currentValue;
        }

        public String measure() {
            return measure;
        }

        public int minimumThreshold() {
            return minimumThreshold;
        }
    }
}
