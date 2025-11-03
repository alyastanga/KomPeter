package com.github.ragudos.kompeter.app.desktop.forms;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.formdev.flatlaf.util.UIScale;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.app.desktop.components.ImageChooser;
import com.github.ragudos.kompeter.app.desktop.components.combobox.ItemBrandRenderer;
import com.github.ragudos.kompeter.app.desktop.components.combobox.StorageLocationRenderer;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.components.jspinner.CurrencySpinner;
import com.github.ragudos.kompeter.app.desktop.components.scroller.ScrollerFactory;
import com.github.ragudos.kompeter.app.desktop.components.textpanes.TextAreaWithLimit;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout.JustifyContent;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.app.desktop.utilities.Validator;
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.dto.inventory.StorageLocationDto;
import com.github.ragudos.kompeter.inventory.Inventory;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.inventory.QuantityMetadata;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.StringUtils;
import com.github.ragudos.kompeter.utilities.constants.StringLimits;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import net.miginfocom.swing.MigLayout;
import raven.extras.SlidePane;
import raven.extras.SlidePaneTransition;
import raven.modal.slider.PanelSlider.SliderCallback;

@SystemForm(name = "Add Product", description = "Add product to inventory", tags = { "Inventory" })
public class FormInventoryAddProduct extends Form {
    private static final Logger LOGGER = KompeterLogger.getLogger(FormInventoryAddProduct.class);

    private JPanel headerPanel;
    private JSplitPane bodyPane;

    private SlidePane slidePane;
    private AtomicBoolean isBusy;

    private RightPanel rightPanel;
    private JPanel leftPanel;
    private FirstStepForm firstStepForm;
    private SecondStepForm secondStepForm;
    private ThirdStepForm thirdStepForm;
    private FourthStepForm fourthStepForm;

    private StepProgressBar progressBar;

    private JPanel leftPanelButtonsContainer;
    private JButton nextConfirmButton;
    private JButton backButton;
    private SVGIconUIColor submitIcon;
    private SVGIconUIColor nextIcon;
    private SVGIconUIColor backIcon;

    private class RightPanel extends JPanel {
        public RightPanel() {
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

            final String markString = loadMarkdown();

            final Parser parser = Parser.builder().build();
            final HtmlRenderer renderer = HtmlRenderer.builder().build();
            final Node node = parser.parse(markString);
            final String htmlString = renderer.render(node);

            final JTextPane textPane = new JTextPane();

            textPane.setContentType("text/html");
            textPane.setEditable(false);
            textPane.setCaret(new DefaultCaret() {
                @Override
                public void paint(final Graphics g) {
                }
            });

            textPane.setText(String.format("""
                    <html>
                    <head>
                        <style>
                            body {
                                font-family: "Montserrat", sans-serif;
                                padding: 12px;
                            }
                        </style>
                    </head>
                    <body>
                        %s
                    </body>
                    </html>
                    """, htmlString));

            final JScrollPane scroller = ScrollerFactory.createScrollPane(textPane);

            setLayout(new MigLayout("insets 0", "[grow, center, fill]", "[grow, center, fill]"));
            add(scroller, "grow");
        }
    }

    private class FourthStepForm extends JPanel {
        ImageChooser productImageChooser;
        JButton clearBtn;

        public void reset() {
            productImageChooser.clear();
        }

        public void populate() {
            if (productImageChooser.initialized()) {
                return;
            }

            productImageChooser.initialize();
        }

        public FourthStepForm() {
            setLayout(new MigLayout("insets 0, flowx", "[grow, fill, left]"));

            clearBtn = new JButton("Clear", new SVGIconUIColor("circle-x.svg", 0.75f, "foreground.background"));
            final JLabel imageLabel = new JLabel("Product Image ");
            final JLabel resName = new JLabel(new SVGIconUIColor("image-file.svg", 0.5f, "Label.disabledForeground"));
            productImageChooser = new ImageChooser(() -> {
                SwingUtilities.invokeLater(() -> {
                    resName.setText(productImageChooser.chosenImageFile() == null ? ""
                            : productImageChooser.chosenImageFile().getAbsolutePath());

                    if (productImageChooser.chosenImageFile() != null) {
                        add(resName, "gapy 1px, wrap", 1);
                    } else {
                        remove(resName);
                    }

                    leftPanel.repaint();
                    leftPanel.revalidate();
                });
            });

            clearBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);
            clearBtn.putClientProperty(FlatClientProperties.STYLE, "font:10;");

            resName.setHorizontalAlignment(JLabel.LEFT);
            resName.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;font:9;");

            final JPanel buttonContainer = new JPanel(new MigLayout("insets 0"));
            buttonContainer.add(clearBtn);

            add(imageLabel, "growx, wrap, gapy 12px");
            add(productImageChooser.imageLabel(), "growx, wrap, gapy 2px");
            add(productImageChooser.chosenImage(), "growx, wrap, gapy 4px");
            add(buttonContainer, "gapy 6px");

            clearBtn.addActionListener((e) -> {
                productImageChooser.clear();
            });
        }
    }

    private class ThirdStepForm extends JPanel {
        CurrencySpinner priceSpinner;
        JSpinner minQtySpinner;
        QuantityPanel qtyPanel;

        public void reset() {
            priceSpinner.setValue(new BigDecimal("0.00"));
            minQtySpinner.setValue(0);
            qtyPanel.reset();
        }

        public ThirdStepForm() {
            setLayout(new MigLayout("insets 0, flowx, wrap", "[grow, fill, left]"));

            priceSpinner = new CurrencySpinner();
            minQtySpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            qtyPanel = new QuantityPanel();

            final JLabel priceLabel = new JLabel("Product Price (Php)");
            final JLabel minQtyLabel = new JLabel("Minimum Quantity");
            final JLabel qtyLabel = new JLabel("Initial Quantity");

            priceSpinner.setToolTipText("Set the price of the product");
            minQtySpinner.setToolTipText("Set the min quantity of the product");
            qtyPanel.setToolTipText("Set the initial quantities of the product");

            add(priceLabel, "growx, gapy 12px");
            add(priceSpinner, "growx, gapy 2px");
            add(minQtyLabel, "growx, gapy 4px");
            add(minQtySpinner, "growx, gapy 2px");
            add(qtyLabel, "growx");
            add(qtyPanel, "growx");
        }

        public class QuantityPanel extends JPanel implements ActionListener {
            AtomicReference<StorageLocationDto[]> storageLocations;
            AtomicReference<ArrayList<StorageLocationDto>> selectedStorageLocations;
            AtomicReference<ArrayList<RowPanel>> rows;
            final JButton addLocationButton;
            JPanel container;
            final JLabel emptyLabel;

            public void reset() {
                for (final RowPanel acquire : rows.getAcquire()) {
                    removeRow(acquire);
                }
            }

            private void updateAllComboBoxes() {
                final ArrayList<StorageLocationDto> selected = new ArrayList<>();

                for (final RowPanel row : rows.getAcquire()) {
                    final StorageLocationDto item = (StorageLocationDto) row.locationComboBox.getSelectedItem();
                    if (item != null) {
                        selected.add(item);
                    }
                }

                selectedStorageLocations.setRelease(selected);

                for (final RowPanel row : rows.getAcquire()) {
                    row.populate();
                }
            }

            private void removeRow(final RowPanel row) {
                rows.getAcquire().remove(row);
                container.remove(row);
                updateAllComboBoxes();

                if (rows.getAcquire().isEmpty()) {
                    container.add(emptyLabel);
                }
                qtyPanel.repaint();
                qtyPanel.revalidate();
            }

            private class RowPanel extends JPanel implements ItemListener, ActionListener {
                JSpinner qtySpinner;
                final JComboBox<StorageLocationDto> locationComboBox;
                private final JButton removeButton;
                private final AtomicBoolean updating;

                public QuantityMetadata getData() {
                    return QuantityMetadata.builder().qty((Integer) qtySpinner.getValue())
                            .storageLocation((StorageLocationDto) locationComboBox.getSelectedItem()).build();
                }

                @Override
                public void actionPerformed(final ActionEvent e) {
                    removeRow(this);
                }

                public RowPanel() {
                    setLayout(new MigLayout("insets 0, flowx, gapx 1", "[grow, fill]", "[grow, top]"));
                    updating = new AtomicBoolean(false);
                    removeButton = new JButton(new SVGIconUIColor("x.svg", 0.5f, "foreground.background"));
                    qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
                    locationComboBox = new JComboBox<>();

                    locationComboBox.putClientProperty(FlatClientProperties.STYLE, "font: 9;");
                    qtySpinner.putClientProperty(FlatClientProperties.STYLE, "font: 9;");
                    removeButton.putClientProperty(FlatClientProperties.STYLE, "font: 9;");
                    removeButton.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);

                    locationComboBox.setRenderer(new StorageLocationRenderer());
                    locationComboBox.addItemListener(this);

                    add(locationComboBox, "growx, wrap");
                    add(qtySpinner, "growx, growy, gapy 1px, split 2");
                    add(removeButton, "growy, gapx 1px");

                    removeButton.addActionListener(this);
                }

                @Override
                public void itemStateChanged(final ItemEvent e) {
                    if (updating.getAcquire()) {
                        return;
                    }

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateAllComboBoxes();
                    }
                }

                public void populate() {
                    final StorageLocationDto curr = (StorageLocationDto) locationComboBox.getSelectedItem();

                    updating.setRelease(true);
                    locationComboBox.removeAllItems();

                    for (final StorageLocationDto dto : storageLocations.getAcquire()) {
                        if (!selectedStorageLocations.getAcquire().contains(dto) || dto.equals(curr)) {
                            locationComboBox.addItem(dto);
                        }
                    }

                    if (curr != null) {
                        locationComboBox.setSelectedItem(curr);
                    }

                    updating.setRelease(false);
                }
            }

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (selectedStorageLocations.getAcquire().size() == storageLocations.getAcquire().length) {
                    JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                            "All available storage locations have already been selected.", "",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                addRow();
            }

            void addRow() {
                final RowPanel row = new RowPanel();

                rows.getAcquire().add(row);
                container.add(row);
                row.populate();

                updateAllComboBoxes();
                container.remove(emptyLabel);
                qtyPanel.repaint();
                qtyPanel.revalidate();
            }

            public void populate() {
                try {
                    storageLocations.setRelease(Inventory.getInstance().getAllStorageLocations());
                } catch (final InventoryException e) {
                    JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(), e.getMessage(),
                            "Failed to get storage locations", JOptionPane.ERROR_MESSAGE);
                    LOGGER.severe(e.getMessage());
                }
            }

            public QuantityPanel() {
                setLayout(new MigLayout("insets 0, flowx, wrap", "[grow, left, fill]"));

                rows = new AtomicReference<>(new ArrayList<>());
                storageLocations = new AtomicReference<>();
                selectedStorageLocations = new AtomicReference<>(new ArrayList<>());
                emptyLabel = new JLabel("No location selected");
                addLocationButton = new JButton("Location",
                        new SVGIconUIColor("plus.svg", 0.75f, "foreground.background"));
                container = new JPanel();
                final JScrollPane scroller = ScrollerFactory.createScrollPane(container);

                scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                addLocationButton.setToolTipText("Add initial product quantity based on storage location.");

                emptyLabel.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
                addLocationButton.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);
                addLocationButton.putClientProperty(FlatClientProperties.STYLE, "font:10;");
                addLocationButton.setIconTextGap(16);

                container.setLayout(new ResponsiveLayout(JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 9, 9));
                container.add(emptyLabel);

                final JPanel buttonContainer = new JPanel(new MigLayout("insets 0"));

                buttonContainer.add(addLocationButton);

                add(scroller, "growx, gapy 2px");
                add(buttonContainer, "align left, gapy 4px");

                addLocationButton.addActionListener(this);
            }
        }
    }

    private void removeListenersRecursively(final Container container) {
        for (final Component comp : container.getComponents()) {
            if (comp instanceof final Container nested) {
                removeListenersRecursively(nested);
            }

            switch (comp) {
                case final JCheckBox checkBox -> {
                    for (final ItemListener l : checkBox.getItemListeners()) {
                        checkBox.removeItemListener(l);
                    }
                }
                case final JButton button -> {
                    for (final ActionListener l : button.getActionListeners()) {
                        button.removeActionListener(l);
                    }
                }
                case final JComboBox<?> cb -> {
                    for (final ItemListener l : cb.getItemListeners()) {
                        cb.removeItemListener(l);
                    }
                }
                case final ImageChooser ic -> ic.destroy();
                case null, default -> {
                }
            }
        }
    }

    private class SecondStepForm extends JPanel {
        JComboBox<ItemBrandDto> brands;
        JLabel brandError;
        CategoryPanel categoryPanel;
        JLabel categoryError;

        public void reset() {
            clearErrors();
            brands.setSelectedIndex(-1);
            categoryPanel.reset();
        }

        public void clearErrors() {
            brandError.setText("");
            brands.putClientProperty("JComponent.outline", null);
            categoryPanel.setBorder(null);
            categoryError.setText("");
        }

        public boolean validateFields() {
            boolean ok = true;

            if (brands.getSelectedIndex() == -1) {
                brands.putClientProperty("JComponent.outline", "error");
                brandError.setText("A brand is required");

                ok = false;
            }

            if (categoryPanel.chosenCategories.getAcquire().isEmpty()) {
                categoryError.setText("Please select at least one category.");

                final FlatRoundBorder b = new FlatRoundBorder();

                b.applyStyleProperty("outline", "error");

                categoryPanel.setBorder(b);

                ok = false;
            }

            return ok;
        }

        public SecondStepForm() {
            setLayout(new MigLayout("insets 0, wrap, flowx", "[grow, left]"));

            final JLabel brandLabel = new JLabel("Product Brand*");
            brandError = new JLabel();
            brands = new JComboBox<>();
            categoryPanel = new CategoryPanel();

            final JLabel categoryLabel = new JLabel(HtmlUtils.wrapInHtml("Product Categories (at least one)*"));
            categoryError = new JLabel();

            brands.setRenderer(new ItemBrandRenderer());
            brandError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
            brandError.putClientProperty(FlatClientProperties.STYLE, "font:9;");
            categoryError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
            categoryError.putClientProperty(FlatClientProperties.STYLE, "font:9;");

            brands.setToolTipText("Select a brand for this product.");

            add(brandLabel, "growx,gapy 12px");
            add(brands, "growx, gapy 2px");
            add(brandError, "growx, gapy");

            add(categoryLabel, "growx, gapy 4px");
            add(categoryPanel, "growx, gapy 2px");
            add(categoryError, "growx, gapy");
        }

        void populateBrands() {
            try {
                final ItemBrandDto[] itemBrands = Inventory.getInstance().getAllItemBrandDtos();

                SwingUtilities.invokeLater(() -> {
                    final ItemBrandDto selected = (ItemBrandDto) brands.getSelectedItem();

                    brands.removeAllItems();

                    for (final ItemBrandDto brand : itemBrands) {
                        brands.addItem(brand);

                        if (selected != null && selected.get_itemBrandId() == brand.get_itemBrandId()) {
                            brands.setSelectedItem(selected);
                        }
                    }

                    if (selected == null) {
                        brands.setSelectedIndex(-1);
                    }

                });
            } catch (final InventoryException err) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                            "Failed to get categories", JOptionPane.ERROR_MESSAGE);
                });
            }
        }

        class CategoryPanel extends JPanel implements ItemListener {
            private final AtomicReference<HashSet<String>> chosenCategories;

            @Override
            public void itemStateChanged(final ItemEvent e) {
                final JCheckBox checkBox = (JCheckBox) e.getItemSelectable();

                switch (e.getStateChange()) {
                    case ItemEvent.DESELECTED -> {
                        chosenCategories.getAcquire().remove(checkBox.getName());
                    }
                    case ItemEvent.SELECTED -> {
                        chosenCategories.getAcquire().add(checkBox.getName());
                    }
                }
            }

            public void reset() {
                for (final Component c : getComponents()) {
                    if (c instanceof final JCheckBox jcb) {
                        jcb.setSelected(false);
                    }
                }
            }

            void populate() {
                try {
                    final String[] categories = Inventory.getInstance().getAllItemCategories();

                    SwingUtilities.invokeLater(() -> {
                        removeListenersRecursively(this);
                        removeAll();

                        final HashSet<String> chosenSet = chosenCategories.getAcquire();

                        for (final String category : categories) {
                            final JCheckBox checkBox = new JCheckBox(category);

                            if (chosenSet.contains(category)) {
                                checkBox.setSelected(true);
                            }

                            checkBox.setToolTipText(String.format(
                                    "<html><body>This product will be in the category <strong>%s</strong></body></html>",
                                    category));
                            checkBox.setName(category);
                            checkBox.addItemListener(this);

                            add(checkBox);
                        }

                        repaint();
                        revalidate();
                    });
                } catch (final InventoryException err) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                                "Failed to get categories", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }

            public CategoryPanel() {
                chosenCategories = new AtomicReference<>(new HashSet<>());

                setLayout(new MigLayout("insets 0, flowx, wrap", "[grow, left]"));
            }
        }
    }

    private void createHeader() {
        headerPanel = new JPanel(new MigLayout("insets 0, flowx"));

        final JLabel title = new JLabel("Add Product");
        final JLabel subtitle = new JLabel("Add a new product to inventory.");

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

        public void reset() {
            nameTextField.setText("");
            nameError.setText("");
            descriptionTextField.setText("");
            descriptionError.setText("");
            nameTextField.putClientProperty("JComponent.outline", null);
            descriptionTextField.putClientProperty("JComponent.outline", null);
        }

        public boolean validateFields() {
            boolean ok = true;

            ok &= Validator.validateField(nameTextField, nameError, (n) -> {
                if (n.isEmpty()) {
                    return "Product name cannot be empty";
                }

                if (n.length() < StringLimits.PRODUCT_NAME.min()) {
                    return String.format("Too short (min: %s)", StringLimits.PRODUCT_NAME.min());
                }

                if (n.length() > StringLimits.PRODUCT_NAME.max()) {
                    return String.format("Too long (max: %s)", StringLimits.PRODUCT_NAME.max());
                }

                try {
                    if (Inventory.getInstance().itemExists(n)) {
                        return "Product already exists. Maybe go to View Product and add a brand for that product?";
                    }
                } catch (final InventoryException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(), String
                                .format("We failed to verify whether the product %s already exists. We apologize", n),
                                "Failed to Verify Product Existence", JOptionPane.ERROR_MESSAGE);
                    });

                    return "System failure.";
                }

                return null;
            });

            ok &= Validator.validateAreaField(descriptionTextField, descriptionError, (d) -> {
                if (d.length() > StringLimits.PRODUCT_DESCRIPTION.max()) {
                    return String.format("Too long (max: %s)", StringLimits.PRODUCT_DESCRIPTION.max());
                }

                return null;
            });

            return ok;
        }

        public void clearErrors() {
            nameTextField.putClientProperty("JComponent.outline", null);
            descriptionTextField.putClientProperty("JComponent.outline", null);
            nameError.setText("");
            descriptionError.setText("");
        }

        public FirstStepForm() {
            setLayout(new MigLayout("insets 0, wrap, flowx", "[grow, center, fill]"));
            final JLabel nameLabel = new JLabel("Product Name*");
            nameTextField = new JTextField();
            nameError = new JLabel("");

            final JLabel descriptionLabel = new JLabel("Product Description");
            descriptionTextField = new TextAreaWithLimit(StringLimits.PRODUCT_DESCRIPTION.max());
            descriptionError = new JLabel();

            nameTextField.setToolTipText("The name of the product. Must be unique.");
            descriptionTextField.setToolTipText("The description of the product.");

            descriptionTextField.setRows(5);
            descriptionTextField.setBorder(new FlatRoundBorder());
            descriptionTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                    "Enter product description...");

            nameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter product name...");

            nameError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
            descriptionError.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");

            nameError.putClientProperty(FlatClientProperties.STYLE, "font:9;");
            descriptionError.putClientProperty(FlatClientProperties.STYLE, "font:9;");

            add(nameLabel, "growx, wrap, gapy 12px");
            add(nameTextField, "growx, wrap, gapy 2px");
            add(nameError, "gapy 1px, wrap");

            add(descriptionLabel, "wrap, gapy 4px");
            add(descriptionTextField, "growx, wrap, gapy 2px");
            add(descriptionError, "gapy 1px");
        }
    }

    @Override
    public void formInit() {
        init();
        formRefresh();
    }

    public FormInventoryAddProduct() {
    }

    @Override
    public void formRefresh() {
        new Thread(() -> {
            secondStepForm.populateBrands();
            secondStepForm.categoryPanel.populate();
            thirdStepForm.qtyPanel.populate();
            fourthStepForm.populate();
        }, "Load data for FormInventoryAddProduct").start();
    }

    @Override
    public boolean formBeforeClose() {
        if (!isBusy.get()) {
            return true;
        }

        JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                "The current page is busy. Please wait until the page buttons are enabled again.", "Page Busy",
                JOptionPane.ERROR_MESSAGE);

        return false;
    }

    @Override
    public boolean formBeforeLogout() {
        if (progressBar.currentStep() == 1 && firstStepForm.nameTextField.getText().isEmpty()
                && firstStepForm.descriptionTextField.getText().isEmpty()) {
            return true;
        }

        final int res = JOptionPane.showConfirmDialog(KompeterDesktopApp.getRootFrame(),
                "Adding a product in progress. Are you sure you want to sign out?", "Sign Out",
                JOptionPane.YES_NO_OPTION);

        switch (res) {
            case JOptionPane.YES_OPTION: {
                removeListenersRecursively(this);
                firstStepForm.reset();
                secondStepForm.reset();
                thirdStepForm.reset();
                fourthStepForm.reset();
                return true;
            }
            case JOptionPane.NO_OPTION: {
                return false;
            }
        }

        return true;
    }

    private void init() {
        isBusy = new AtomicBoolean(false);
        firstStepForm = new FirstStepForm();
        secondStepForm = new SecondStepForm();
        thirdStepForm = new ThirdStepForm();
        fourthStepForm = new FourthStepForm();
        bodyPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPanelButtonsContainer = new JPanel(new MigLayout("insets 0", "[left]push[right]"));
        leftPanel = new JPanel(new MigLayout("insets 0 0 0 12", "[grow, fill, left]", "[top][top]"));
        final JPanel leftPanelWrapper = new JPanel(
                new MigLayout("insets 0", "[grow, left, fill]", "[grow, top, fill]"));
        slidePane = new SlidePane();
        rightPanel = new RightPanel();
        nextIcon = new SVGIconUIColor("move-right.svg", 0.75f, "foreground.primary");
        backIcon = new SVGIconUIColor("move-left.svg", 0.75f, "foreground.muted");
        submitIcon = new SVGIconUIColor("circle-check.svg", 0.75f, "foreground.primary");
        nextConfirmButton = new JButton("Continue", nextIcon);
        backButton = new JButton("Back", backIcon);
        progressBar = new StepProgressBar(4);
        final JScrollPane scroller = ScrollerFactory.createScrollPane(leftPanelWrapper);

        setLayout(new MigLayout("insets 0 4 0 4, flowx, wrap", "[grow, center, fill]", "[top][grow, top, fill]"));
        createHeader();

        nextConfirmButton.setToolTipText("Go to next step or finalize creating this product.");
        backButton.setToolTipText("Go back to previous step");

        nextConfirmButton.setIconTextGap(16);
        nextConfirmButton.setHorizontalTextPosition(SwingConstants.LEFT);
        nextConfirmButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        backButton.setIconTextGap(16);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        backButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

        slidePane.addSlide(firstStepForm);

        leftPanelButtonsContainer.add(nextConfirmButton, "cell 1 0");

        leftPanel.add(progressBar, "growx, wrap");
        leftPanel.add(slidePane, "grow, wrap, gapy 8px");
        leftPanel.add(leftPanelButtonsContainer, "growx, gapy 24px");
        leftPanelWrapper.add(leftPanel, "grow");

        bodyPane.add(scroller);
        bodyPane.add(rightPanel);

        add(headerPanel, "growx");
        add(bodyPane, "gapy 16px, grow");

        bodyPane.setResizeWeight(0.35);
        bodyPane.setContinuousLayout(true);
        bodyPane.setOneTouchExpandable(true);

        backButton.addActionListener(new BackButtonActionListener());
        nextConfirmButton.addActionListener(new NextConfirmButtonActionListener());
    }

    private class BackButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isBusy.get()) {
                return;
            }

            switch (progressBar.currentStep()) {
                case 2:
                    secondStep();
                    break;
                case 3:
                    thirdStep();
                    break;
                case 4:
                    fourthStep();
                    break;
            }
        }

        private void secondStep() {
            isBusy.set(true);

            leftPanelButtonsContainer.remove(backButton);
            progressBar.setCurrentStep(1);

            slidePane.addSlide(firstStepForm,
                    SlidePaneTransition.create(SlidePaneTransition.Type.BACK),
                    new SliderCallback() {

                        @Override
                        public void complete() {
                            isBusy.set(false);
                        }
                    });
        }

        private void thirdStep() {
            isBusy.set(true);

            progressBar.setCurrentStep(2);
            secondStepForm.repaint();
            secondStepForm.revalidate();

            slidePane.addSlide(secondStepForm,
                    SlidePaneTransition.create(SlidePaneTransition.Type.BACK),
                    new SliderCallback() {

                        @Override
                        public void complete() {
                            isBusy.set(false);
                        }
                    });
        }

        private void fourthStep() {
            isBusy.set(true);
            progressBar.setCurrentStep(3);

            slidePane.addSlide(thirdStepForm,
                    SlidePaneTransition.create(SlidePaneTransition.Type.BACK),
                    new SliderCallback() {

                        @Override
                        public void complete() {
                            isBusy.set(false);
                            nextConfirmButton.setText("Continue");
                            nextConfirmButton.setIcon(nextIcon);
                        }
                    });
        }
    }

    private class NextConfirmButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isBusy.get()) {
                return;
            }

            switch (progressBar.currentStep()) {
                case 1:
                    firstStep();
                    break;
                case 2:
                    secondStep();
                    break;
                case 3:
                    thirdStep();
                    break;
                case 4:
                    fourthStep();
                    break;
            }
        }

        private void firstStep() {
            firstStepForm.clearErrors();

            if (!firstStepForm.validateFields()) {
                return;
            }

            isBusy.set(true);

            leftPanelButtonsContainer.add(backButton, 0);

            progressBar.setCurrentStep(2);

            slidePane.addSlide(secondStepForm,
                    SlidePaneTransition.create(SlidePaneTransition.Type.FORWARD),
                    new SliderCallback() {

                        @Override
                        public void complete() {
                            isBusy.set(false);
                        }
                    });
        }

        private void secondStep() {
            secondStepForm.clearErrors();

            if (!secondStepForm.validateFields()) {
                return;
            }

            isBusy.set(true);
            progressBar.setCurrentStep(3);

            slidePane.addSlide(thirdStepForm,
                    SlidePaneTransition.create(SlidePaneTransition.Type.FORWARD),
                    new SliderCallback() {

                        @Override
                        public void complete() {
                            isBusy.set(false);
                        }
                    });
        }

        private void thirdStep() {
            isBusy.set(true);
            progressBar.setCurrentStep(4);

            slidePane.addSlide(fourthStepForm,
                    SlidePaneTransition.create(SlidePaneTransition.Type.FORWARD),
                    new SliderCallback() {

                        @Override
                        public void complete() {
                            isBusy.set(false);
                            nextConfirmButton.setText("Add Product");
                            nextConfirmButton.setIcon(submitIcon);
                        }
                    });
        }

        private void fourthStep() {
            isBusy.set(true);

            final String name = firstStepForm.nameTextField.getText();
            final String description = firstStepForm.descriptionTextField.getText();
            final ItemBrandDto chosenBrand = (ItemBrandDto) secondStepForm.brands.getSelectedItem();
            final String[] chosenCategories = secondStepForm.categoryPanel.chosenCategories.getAcquire()
                    .toArray(String[]::new);
            final BigDecimal price = (BigDecimal) thirdStepForm.priceSpinner.getValue();
            final Integer minQty = (Integer) thirdStepForm.minQtySpinner.getValue();
            final QuantityMetadata[] qty = thirdStepForm.qtyPanel.rows.getAcquire().stream().map((row) -> {
                return row.getData();
            }).toArray(QuantityMetadata[]::new);

            new ConfirmProductAdditionDialog(name, description, chosenBrand, chosenCategories, price, minQty, qty,
                    fourthStepForm.productImageChooser.chosenImageFile())
                    .setVisible(true);
        }
    }

    private class ConfirmProductAdditionDialog extends JDialog implements ActionListener {
        JButton confirmBtn;
        JButton cancelBtn;

        private void beforeDispose() {
            confirmBtn.removeActionListener(this);
            cancelBtn.removeActionListener(this);
        }

        public ConfirmProductAdditionDialog(
                final String name,
                final String description,
                final ItemBrandDto chosenBrand,
                final String[] chosenCategories,
                final BigDecimal price,
                final Integer minQty,
                final QuantityMetadata[] qty,
                final File chosenImage) {
            super(KompeterDesktopApp.getRootFrame(), "Confirm Product Addition", Dialog.ModalityType.APPLICATION_MODAL);

            setLayout(new MigLayout("insets 12, flowx, wrap", "[grow, fill, left]"));

            final JLabel title = new JLabel(HtmlUtils.wrapInHtml("Adding a new item"));
            final JLabel subtitle = new JLabel(
                    HtmlUtils.wrapInHtml("<p>Please make sure all information below are correct</p>"));

            title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
            subtitle.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");

            final JPanel container = new JPanel(new MigLayout("insets 0, flowx, wrap", "[grow, left]"));
            final JScrollPane scroller = ScrollerFactory.createScrollPane(container);

            final JLabel n = new JLabel(HtmlUtils.wrapInHtml(String.format("<p><b>Name: </b><br>%s</p>", name)));
            final JLabel dL = new JLabel(HtmlUtils.wrapInHtml("<b>Description: </b>"));
            final JTextArea d = new JTextArea();

            d.setWrapStyleWord(true);
            d.setLineWrap(true);
            d.setCaret(new DefaultCaret() {
                @Override
                public void paint(final Graphics g) {
                }
            });
            d.putClientProperty(FlatClientProperties.STYLE, "background:null;");
            d.setText(description);

            final JLabel b = new JLabel(
                    HtmlUtils.wrapInHtml(String.format("<p><b>Brand: </b><br>%s</p>", chosenBrand.getName())));
            final JLabel cL = new JLabel(HtmlUtils.wrapInHtml("<b>Categories: </b>"));
            final JTextPane c = new JTextPane();
            c.setContentType("text/html");
            c.setText(String.format("""
                    <html>
                    <head>
                        <style>
                            ul {
                                margin-left: 4px;
                                padding: 0;
                            }
                        </style>
                    </head>
                    <body>
                        <ul>%s</ul>
                    </body>
                    </html>
                    """, Arrays.stream(chosenCategories).map((cc) -> String.format("<li>%s</li>", cc))
                    .collect(Collectors.joining("\n"))));
            c.putClientProperty(FlatClientProperties.STYLE, "background:null;");

            final JLabel p = new JLabel(
                    HtmlUtils.wrapInHtml(String.format("<b>Price: </b><br>%s", StringUtils.formatBigDecimal(price))));
            final JLabel mq = new JLabel(
                    HtmlUtils.wrapInHtml(String.format("<b>Minimum Quantity: </b><br>%s", minQty)));
            final JLabel qL = new JLabel(HtmlUtils.wrapInHtml("<b>Quantities: </b>"));
            final JTextPane q = new JTextPane();
            q.setContentType("text/html");
            q.setText(String.format("""
                    <html>
                    <head>
                        <style>
                            ul {
                                margin-left: 4px;
                                padding: 0;
                            }
                        </style>
                    </head>
                    <body>
                        <ul>%s</ul>
                    </body>
                    </html>
                    """,
                    Arrays.stream(qty)
                            .map((qq) -> String.format("<li>%s - %s item/s</li>", qq.getStorageLocation().getName(),
                                    qq.getQty()))
                            .collect(Collectors.joining("\n"))));
            q.putClientProperty(FlatClientProperties.STYLE, "background:null;");
            final JLabel ci = new JLabel(
                    HtmlUtils.wrapInHtml(String.format("<b>Chosen Image: </b><br>%s",
                            chosenImage == null ? "No Image" : chosenImage.getAbsolutePath())));

            confirmBtn = new JButton("Add Product",
                    new SVGIconUIColor("circle-check.svg", 0.75f, "foreground.primary"));
            cancelBtn = new JButton("Cancel", new SVGIconUIColor("circle-x.svg", 0.75f, "foreground.muted"));

            confirmBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
            cancelBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

            add(title, "growx, wrap");
            add(subtitle, "growx, wrap");
            add(scroller, "growx, wrap, gapy 8px");

            container.add(n, "wrap, gapy 8px");
            container.add(dL, "wrap, gapy 2px");
            container.add(d, "wrap, gapy 1px");
            container.add(b, "wrap, gapy 2px");
            container.add(cL, "wrap, gapy 2px");
            container.add(c, "wrap, gapy 1px");
            container.add(p, "wrap, gapy 2px");
            container.add(mq, "wrap, gapy 2px");
            container.add(qL, "wrap, gapy 2px");
            container.add(q, "wrap, gapy 1px");
            container.add(ci, "wrap, gapy 2px");

            add(cancelBtn, "split 2, gapy 16px");
            add(confirmBtn, "gapx 4px");

            pack();
            setLocationRelativeTo(KompeterDesktopApp.getRootFrame());

            confirmBtn.setActionCommand("confirm");
            cancelBtn.setActionCommand("cancel");

            confirmBtn.addActionListener(this);
            cancelBtn.addActionListener(this);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("confirm")) {

                beforeDispose();
                dispose();
            } else if (e.getActionCommand().equals("cancel")) {
                beforeDispose();
                dispose();
            }
        }
    }

    private String loadMarkdown() {
        final StringBuilder sb = new StringBuilder();

        try (InputStream st = FormInventoryAddProduct.class.getResourceAsStream("AddProductFormGuide.md")) {
            if (st == null) {
                LOGGER.severe("Cannot find AddProductFormGuide.md in resources.");

                return "";
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(st))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to read AddProductFormGuide.md", e);

            return "";
        }

        return sb.toString();
    }

    private class StepProgressBar extends JComponent {
        private final int totalSteps;
        private final AtomicInteger currentStep;
        private final int animationDelay = 10;
        private final float animationSpeed = 0.1f;
        private Timer animationTimer;
        private float displayedStep;

        public StepProgressBar(final int totalSteps) {
            this.totalSteps = totalSteps;
            currentStep = new AtomicInteger(1);
            displayedStep = 1f;

            setPreferredSize(new Dimension(getPreferredSize().width, 36));

            setupTimer();
        }

        private void setupTimer() {
            animationTimer = new Timer(animationDelay, (final ActionEvent e) -> {
                final float target = currentStep.getAcquire();

                if (Math.abs(displayedStep - target) < 0.01f) {
                    displayedStep = (float) target;
                    animationTimer.stop();
                } else {
                    displayedStep += (target - displayedStep) * animationSpeed;
                }

                repaint();
            });
        }

        public void setCurrentStep(final int step) {
            if (step < 1 || step > totalSteps) {
                return;
            }

            currentStep.set(step);

            if (!animationTimer.isRunning()) {
                animationTimer.start();
            }
        }

        public int currentStep() {
            return currentStep.get();
        }

        @Override
        protected void paintComponent(final Graphics g) {
            final Graphics2D g2 = (Graphics2D) g.create();
            final int w = getWidth();
            final int h = getHeight();
            final int barY = h / 2;
            final int margin = UIScale.scale(10);
            final int stepSpacing = (w - margin * 2) / (totalSteps - 1);
            final int circleSize = UIScale.scale(18);
            final int stroke = UIScale.scale(6);
            final Color lineColor = UIManager.getColor("color.stepProgressRemaining");
            final Color progressColor = UIManager.getColor("color.stepProgressCompleted");
            final Color progressCircleColor = UIManager.getColor("color.stepProgressCurrentCircle");
            final Color progressForeground = UIManager.getColor("foreground.stepProgressCurrentCircle");
            final Color lineForeground = UIManager.getColor("foreground.stepProgressCircle");
            final int filledX = margin + (int) ((displayedStep - 1) * stepSpacing);
            final Font font = getFont().deriveFont(Font.BOLD, UIScale.scale(12f));

            if (progressColor == null) {
            }

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(lineColor);
            g2.drawLine(margin, barY, w - margin, barY);

            g2.setColor(progressColor);
            g2.drawLine(margin, barY, filledX, barY);

            g2.setFont(font);

            final FontMetrics fm = g2.getFontMetrics();

            for (int i = 1; i <= totalSteps; ++i) {
                final int cx = margin + (i - 1) * stepSpacing;
                final int x = cx - circleSize / 2;
                final int y = barY - circleSize / 2;

                final boolean active = i <= displayedStep;

                g2.setColor(active ? progressCircleColor : lineColor);
                g2.fillOval(x, y, circleSize, circleSize);

                final String txt = String.valueOf(i);
                final int tx = cx - fm.stringWidth(txt) / 2;
                final int ty = y + (circleSize + fm.getAscent()) / 2 - 3;

                g2.setColor(active ? progressForeground : lineForeground);
                g2.drawString(txt, tx, ty);
            }

            g2.dispose();
        }
    }
}
