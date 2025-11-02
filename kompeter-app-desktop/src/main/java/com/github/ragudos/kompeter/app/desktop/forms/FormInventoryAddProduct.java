package com.github.ragudos.kompeter.app.desktop.forms;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.ImageChooser;
import com.github.ragudos.kompeter.app.desktop.components.ImagePanel;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
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
    private JPanel headerPanel;
    private JSplitPane bodyPane;

    private FirstStepForm firstStepForm;
    private SecondStepForm secondStepForm;
    private ThirdStepForm thirdStepForm;
    private FourthStepForm fourthStepForm;

    private class FourthStepForm extends JPanel {
        ImageChooser productImageChooser;
        JComboBox<ItemStatus> statusBox;

        public FourthStepForm() {
            productImageChooser = new ImageChooser();
            statusBox = new JComboBox<>();
        }
    }

    private class ThirdStepForm extends JPanel {
        JSpinner priceSpinner;
        JSpinner minQtySpinner;
        JLabel minQtyError;
        QuantityPanel qtyPanel;

        public ThirdStepForm() {
            priceSpinner = new JSpinner(new SpinnerNumberModel(1.00, 1.00, Double.MAX_VALUE, 1.00));
        }
    }

    private class SecondStepForm extends JPanel {
        private JComboBox<ItemBrandDto> brands;
        private CategoryPanel categoryPanel;

        public SecondStepForm() {

        }

        void populateBrands() {
            try {
                for (ItemBrandDto brand : Inventory.getInstance().getAllItemBrandDtos()) {
                    brands.addItem(brand);
                }
            } catch (InventoryException err) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                        "Failed to get categories", JOptionPane.ERROR_MESSAGE);
            }
        }

        class CategoryPanel extends JPanel implements ActionListener {
            private JPanel rowsPanel;
            private JButton addCategoryBtn;

            private AtomicReference<String[]> allCategories;

            @Override
            public void actionPerformed(ActionEvent e) {

            }

            void populate() {
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

            void addCategoryRow(String preselected) {
                List<String> available = getAvailableCategories();

                if (available.isEmpty() && preselected == null) {
                    return;
                }

                JComboBox<String> combo = new JComboBox<>(available.toArray(new String[0]));

                if (preselected != null) {
                    combo.setSelectedItem(preselected);
                }

                JButton removeBtn = new JButton("Remove", new SVGIconUIColor("x.svg", 0.5f, "foreground.muted"));
                JPanel row = new JPanel(new MigLayout("insets 0, gap 6", "[grow, fill][30!]"));

                row.add(combo);
                row.add(removeBtn);
                rowsPanel.add(row, "growx, wrap");
                rowsPanel.revalidate();

                removeBtn.addActionListener(this);
                combo.addActionListener(this);
            }

            List<String> getAvailableCategories() {
                Set<String> chosen = getChosenCategories();
                List<String> available = new ArrayList<String>();

                for (String cat : allCategories.getAcquire()) {
                    if (!chosen.contains(cat)) {
                        available.add(cat);
                    }
                }

                return available;
            }

            Set<String> getChosenCategories() {
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

    }

    private void createHeader() {
        headerPanel = new JPanel(new MigLayout("insets 0, flowx"));

        JLabel title = new JLabel("Add Product");
        JLabel subtitle = new JLabel("Add a new product to inventory.");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h4");
        subtitle.putClientProperty(FlatClientProperties.STYLE, "foreground:$color.muted;font:11;");

        headerPanel.add(title, "wrap");
        headerPanel.add(subtitle, "wrap, gapy 4px");
    }

    private class FirstStepForm extends JPanel {
        JTextField nameTextField;
        JLabel nameError;
        JTextArea descriptionTextField;
        JLabel descriptionError;

        public FirstStepForm() {
            JLabel nameLabel = new JLabel("Product Name");
            nameTextField = new JTextField();
            nameError = new JLabel();

            JLabel descriptionLabel = new JLabel("Product Description");
            descriptionTextField = new JTextArea();
            descriptionError = new JLabel();

            add(nameLabel, "wrap");
            add(nameTextField, "growx, wrap, gapy 2px");
            add(nameError, "gapy 1px, wrap");

            add(descriptionLabel, "wrap, gapy 4px");
            add(descriptionTextField, "growx, wrap, gapy 2px");
            add(descriptionError, "gapy 1px");
        }
    }

    private void createForm() {
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
        setLayout(new BorderLayout());
        createForm();
    }
}
