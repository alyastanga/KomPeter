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

        setLayout(new MigLayout("insets 2, flowx", "[center][grow, fill, left]"));

        add(imagePanel);
        add(label, "growx");

        imagePanel.setPreferredSize(new Dimension(28, 28));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        if (value instanceof LabelWithImageData data) {
            imagePanel.setImage(AssetLoader.loadImage(data.imagePath, true));
            label.setText(data.label);
        } else {
            LOGGER.severe("Invalid data for LabelWithImage \n" + value);
        }

        return this;
    }

    public static class LabelWithImageData {
        String imagePath;
        String label;

        public LabelWithImageData(String imagePath, String label) {
            this.imagePath = imagePath;
            this.label = label;
        }
    }
}
