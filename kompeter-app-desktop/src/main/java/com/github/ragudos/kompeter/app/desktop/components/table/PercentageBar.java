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
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.OverlayLayout;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class PercentageBar extends JPanel implements TableCellRenderer {
    private static final Logger LOGGER = KompeterLogger.getLogger(PercentageBar.class);

    private final JProgressBar bar;
    private int currentValue;
    private final JLabel label;

    public PercentageBar() {
        setOpaque(false);

        setLayout(new OverlayLayout(this));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        putClientProperty(FlatClientProperties.STYLE, "background: null;");

        label = new JLabel("");

        label.putClientProperty(FlatClientProperties.STYLE, "font: 9 bold;");
        bar = new JProgressBar(0, 100);

        label.setAlignmentX(0.5f);
        label.setAlignmentY(0.5f);

        add(label);

        final JPanel container = new JPanel(new BorderLayout());

        container.setOpaque(false);
        container.add(bar, BorderLayout.CENTER);

        add(container);
    }

    public int currentValue() {
        return currentValue;
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

        currentValue = percentageBarData.currentValue;
        bar.setToolTipText(String.format("Minimum: %s", percentageBarData.minimumThreshold));
        bar.setMaximum(percentageBarData.maxThreshold);

        final float percentage = ((float) percentageBarData.currentValue - (float) percentageBarData.minimumThreshold)
                / (Math.max(percentageBarData.maxThreshold, percentageBarData.minimumThreshold)
                        - percentageBarData.minimumThreshold);
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

        label.setText(text);
        bar.setValue(Math.round(percentage * 100f));

        return this;
    }

    public static class PercentageBarData {
        public static boolean isPercentageBarData(final Object obj) {
            return (obj != null && obj instanceof final PercentageBarData data) && data.currentValue >= 0
                    && data.minimumThreshold >= 0 && data.maxThreshold > data.minimumThreshold;
        }

        int currentValue;
        int id;
        int maxThreshold;
        String measure;

        int minimumThreshold;

        public PercentageBarData(final int id, final int currentValue, final int minimumThreshold,
                final String measure) {
            this(id, currentValue, minimumThreshold, 100, measure);
        }

        public PercentageBarData(final int id, final int currentValue, final int minimumThreshold,
                final int maxThreshold, final String measure) {
            this.id = id;
            this.currentValue = currentValue;
            this.minimumThreshold = minimumThreshold;
            this.maxThreshold = maxThreshold;
            this.measure = measure;
        }

        public int currentValue() {
            return currentValue;
        }
    }
}
