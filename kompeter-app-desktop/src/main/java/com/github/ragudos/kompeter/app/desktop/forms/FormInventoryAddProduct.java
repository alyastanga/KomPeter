package com.github.ragudos.kompeter.app.desktop.forms;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.inventory.Inventory;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.platform.SystemInfo;

import net.miginfocom.swing.MigLayout;

@SystemForm(name = "Add Product", description = "Add product to inventory", tags = { "Inventory" })
public class FormInventoryAddProduct extends Form {
    private JLabel title;
    private JLabel subtitle;

    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel nameError;

    private JLabel descriptionLabel;
    private JTextArea descriptionTextField;
    private JLabel descriptionError;

    private JLabel brandLabel;
    private JComboBox<ItemBrandDto> brands;

    private CategoryPanel categoryPanel;

    private ProductImageChooser productImageChooser;

    private JLabel priceLabel;
    private JSpinner priceSpinner;

    private JLabel minQtyLabel;
    private JSpinner minQtySpinner;
    private JLabel minQtyError;

    private QuantityPanel qtyPanel;

    private JLabel statusLabel;
    private JComboBox<ItemStatus> statusBox;

    private void createForm() {
        title = new JLabel("Add Product");
        subtitle = new JLabel("Add a new product to inventory.");

        nameLabel = new JLabel("Product Name");
        nameTextField = new JTextField();
        nameError = new JLabel();

        descriptionLabel = new JLabel("Product Description");
        descriptionTextField = new JTextArea();
        descriptionError = new JLabel();

        productImageChooser = new ProductImageChooser();

        brandLabel = new JLabel("Brand");
        brands = new JComboBox<>();

        categoryPanel = new CategoryPanel();

        add(productImageChooser.imageLabel);
    }

    private class ProductImageChooser {
        JLabel imageLabel;
        JFileChooser imageChooser;
        ProductImageChooserMouseListener productImageChooserMouseListener;

        private class ProductImageChooserMouseListener extends MouseAdapter {

        }

        public ProductImageChooser() {
            productImageChooserMouseListener = new ProductImageChooserMouseListener();
            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(
                    "PNG Images", "png");
            imageLabel = new JLabel(HtmlUtils.wrapInHtml("Drag & Drop product image"));
            imageChooser = new JFileChooser(String.format("%s%sPictures", SystemInfo.USER_HOME, File.separator));

            imageLabel.setPreferredSize(new Dimension(150, 150));

            float[] dash = { 5f, 5f }; // 5 pixels line, 5 pixels gap
            Border dashedBorder = BorderFactory.createStrokeBorder(
                    new BasicStroke(
                            2f, // line width
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10f,
                            dash,
                            0f),
                    Color.BLACK);

            imageLabel.setBorder(dashedBorder);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

            imageChooser.setFileFilter(pngFilter);
            imageChooser.setAcceptAllFileFilterUsed(false);
        }

    }

    private void populateBrands() {
        try {
            for (ItemBrandDto brand : Inventory.getInstance().getAllItemBrandDtos()) {
                brands.addItem(brand);
            }
        } catch (InventoryException err) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                    "Failed to get categories", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class CategoryPanel extends JPanel implements ActionListener {
        private JPanel rowsPanel;
        private JButton addCategoryBtn;

        private AtomicReference<String[]> allCategories;

        @Override
        public void actionPerformed(ActionEvent e) {

        }

        public void populate() {
            try {
                allCategories.set(Inventory.getInstance().getAllItemCategories());
            } catch (InventoryException err) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                        "Failed to get categories", JOptionPane.ERROR_MESSAGE);
            }
        }

        public CategoryPanel() {
            allCategories = new AtomicReference<>();

            setBorder(BorderFactory.createTitledBorder("Categories"));
            setLayout(new BorderLayout(0, 9));

            rowsPanel = new JPanel(new MigLayout("insets 4, gap 4, wrap 1", "[grow, fill]"));
            JScrollPane scrollPane = new JScrollPane(rowsPanel);
            addCategoryBtn = new JButton("+ Add Category");

            add(scrollPane, BorderLayout.CENTER);
            add(addCategoryBtn, BorderLayout.SOUTH);

            addCategoryBtn.addActionListener(this);
        }

        public void addCategoryRow(String preselected) {
            List<String> available = getAvailableCategories();

            if (available.isEmpty() && preselected == null) {
                return;
            }

            JComboBox<String> combo = new JComboBox<>(available.toArray(new String[0]));

            if (preselected != null) {
                combo.setSelectedItem(preselected);
            }

            JButton removeBtn = new JButton("✖");
            JPanel row = new JPanel(new MigLayout("insets 0, gap 6", "[grow, fill][30!]"));

            row.add(combo);
            row.add(removeBtn);
            rowsPanel.add(row, "growx, wrap");
            rowsPanel.revalidate();

            removeBtn.addActionListener(this);
            combo.addActionListener(this);
        }

        private List<String> getAvailableCategories() {
            Set<String> chosen = getChosenCategories();
            List<String> available = new ArrayList<String>();

            for (String cat : allCategories.getAcquire()) {
                if (!chosen.contains(cat)) {
                    available.add(cat);
                }
            }

            return available;
        }

        private Set<String> getChosenCategories() {
            Set<String> chosen = new HashSet<String>();

            for (Component comp : rowsPanel.getComponents()) {
                if (comp instanceof JPanel row) {
                    for (Component c : row.getComponents()) {
                        if (c instanceof JComboBox<?> combo) {
                            Object val = combo.getSelectedItem();

                            if (val != null) {
                                chosen.add(val.toString());
                            }
                        }
                    }
                }
            }

            return chosen;
        }
    }

    private class QuantityPanel extends JPanel {
        public QuantityPanel() {
            setBorder(BorderFactory.createTitledBorder("Quantities per location"));
        }
    }

    private void clearFields() {
    }

    @Override
    public void formInit() {
        init();
        formRefresh();
    }

    @Override
    public void formRefresh() {
        new Thread(() -> {
            categoryPanel.populate();
            populateBrands();
        }, "Load data for FormInventoryAddProduct").start();
    }

    @Override
    public boolean formBeforeClose() {
        return super.formBeforeClose();
    }

    @Override
    public boolean formBeforeLogout() {
        return super.formBeforeLogout();
    }

    private void init() {
        createForm();
    }
}
