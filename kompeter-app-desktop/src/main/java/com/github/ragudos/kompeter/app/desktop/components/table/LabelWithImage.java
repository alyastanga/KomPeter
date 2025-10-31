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
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import net.miginfocom.swing.MigLayout;

public class LabelWithImage extends JPanel implements TableCellRenderer {
    private static final Logger LOGGER = KompeterLogger.getLogger(LabelWithImage.class);

    private final ImagePanel imagePanel;
    private final JLabel label;

    public LabelWithImage() {
        this.imagePanel = new ImagePanel(null);
        this.label = new JLabel("");

        putClientProperty(FlatClientProperties.STYLE, "background:null;");

        setLayout(new MigLayout("insets 2, flowx", "[center][grow, fill, left]"));

        setOpaque(false);

        add(imagePanel);
        add(label, "growx");

        imagePanel.setPreferredSize(new Dimension(28, 28));
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
            imagePanel.setImage(AssetLoader.loadImage(data.imagePath, true));
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
