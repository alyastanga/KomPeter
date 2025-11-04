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
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetLoader;
import com.github.ragudos.kompeter.app.desktop.components.ImagePanel;
import com.github.ragudos.kompeter.app.desktop.components.ImagePanel.ScaleMode;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import net.miginfocom.swing.MigLayout;

public class LabelWithImage extends JPanel implements TableCellRenderer {
    private static final Logger LOGGER = KompeterLogger.getLogger(LabelWithImage.class);

    private final ImagePanel imagePanel;
    private final JLabel label;

    public LabelWithImage() {
        this.imagePanel = new ImagePanel(null);
        this.label = new JLabel("");

        imagePanel.setScaleMode(ScaleMode.CONTAIN);

        putClientProperty(FlatClientProperties.STYLE, "background:null;");

        setLayout(new MigLayout("insets 2, flowx", "[center][grow, fill, left]", "[center]"));

        label.setAlignmentY(0.5f);

        setOpaque(false);

        imagePanel.setMinimumSize(new Dimension(36, 36));

        add(imagePanel);
        add(label, "growx");
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column) {
        if (isSelected) {
            setOpaque(true);
            setBackground(table.getSelectionBackground());
            label.setForeground(table.getSelectionForeground());
        } else {
            setOpaque(false);
            setBackground(table.getBackground());
            label.setForeground(table.getForeground());
        }

        if (value instanceof final LabelWithImageData data) {
            AssetLoader.loadImageAsync(data.imagePath, true, (img) -> {
                if (img != null) {
                    imagePanel.setImage(img);
                    table.repaint(table.getCellRect(row, column, false));
                }
            });

            label.setText(data.label);
        } else {
            LOGGER.severe("Invalid data for LabelWithImage \n" + value);
        }

        return this;
    }

    public String getText() {
        return label.getText();
    }

    public static class LabelWithImageData {
        String imagePath;
        String label;

        public LabelWithImageData(final String imagePath, final String label) {
            this.imagePath = imagePath;
            this.label = label;
        }

        public String label() {
            return label;
        }
    }
}
