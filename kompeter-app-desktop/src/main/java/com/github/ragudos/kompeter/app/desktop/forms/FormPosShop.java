/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetLoader;
import com.github.ragudos.kompeter.app.desktop.components.ImagePanel;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.components.menu.FilterPopupMenu.CategoryBrandFilterPopupMenu;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout.JustifyContent;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.inventory.Inventory;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.pointofsale.Cart;
import com.github.ragudos.kompeter.pointofsale.Cart.CartEvent;
import com.github.ragudos.kompeter.pointofsale.CartItem;
import com.github.ragudos.kompeter.pointofsale.InsufficientStockException;
import com.github.ragudos.kompeter.pointofsale.NegativeQuantityException;
import com.github.ragudos.kompeter.utilities.Debouncer;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.StringUtils;

import net.miginfocom.swing.MigLayout;
import raven.modal.component.DropShadowBorder;

@SystemForm(name = "Point of Sale Shop", description = "The point of sale shop", tags = { "sales", "shop" })
public class FormPosShop extends Form {
    private AtomicReference<Cart> cart;
    private JPanel cartButtonsContainer;
    private JPanel cartContentContainer;
    private JPanel cartMetadataContainer;
    private JPanel cartPanel;
    private JPanel cartTotalPriceContainer;
    private JLabel cartTotalPriceLabel;
    private JPanel cartTotalQuantityContainer;
    private JLabel cartTotalQuantityLabel;
    // No need atomic reference since we'll always access these in EDT
    private JButton checkoutButton;
    private JButton clearCartButton;
    private JSplitPane containerSplitPane;
    private Debouncer debouncer;
    private CategoryBrandFilterPopupMenu filterPopupMenu;
    private Inventory inventory;
    private AtomicBoolean isFetching;
    private AtomicReferenceArray<InventoryMetadataDto> items;
    private JPanel leftPanel;
    private JPanel leftPanelContentContainer;
    private JPanel leftPanelHeader;
    private JPanel rightPanel;

    private JTextField searchTextField;

    @Override
    public boolean formBeforeClose() {
        if (cart.getAcquire().isEmpty()) {
            return true;
        }

        final int chosenOption = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),
                "Cart is not empty. Would you like to save the current cart's state?", "Save or Remove",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        boolean returnVal = false;

        switch (chosenOption) {
            case JOptionPane.CANCEL_OPTION:
                returnVal = false;
                break;
            case JOptionPane.YES_OPTION:
                returnVal = true;
                break;
            case JOptionPane.NO_OPTION:
                SwingUtilities.invokeLater(() -> {
                    cart.getAcquire().clearCart();
                    buildRightPanelContent();
                });
                returnVal = true;
                break;
        }

        return returnVal;
    }

    @Override
    public boolean formBeforeLogout() {
        if (cart.getAcquire().isEmpty()) {
            return true;
        }

        final int chosenOption = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),
                "Cart is not empty. Would you like to continue logging out?", "Log out?", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        return chosenOption == JOptionPane.YES_OPTION;
    }

    @Override
    public void formInit() {
        init();
        formRefresh();
    }

    @Override
    public void formOpen() {
    }

    @Override
    public void formRefresh() {
        if (isFetching.get()) {
            return;
        }

        new Thread(this::loadData, "Load Point of Sale Shop Data").start();
    }

    private void applyShadowBorder(final JPanel panel) {
        if (panel != null) {
            panel.setBorder(new DropShadowBorder(new Insets(2, 4, 8, 4), 1, 16));
        }
    }

    private void buildLeftPanelContent() {
        leftPanelContentContainer.removeAll();

        final ResponsiveLayout layout = (ResponsiveLayout) leftPanelContentContainer.getLayout();

        final int itemLen = items.length();

        if (itemLen == 0) {
            layout.setJustifyContent(JustifyContent.CENTER);
            leftPanelContentContainer.add(new NoResultsPanel());

            leftPanelContentContainer.repaint();
            leftPanelContentContainer.revalidate();

            return;
        }

        layout.setJustifyContent(JustifyContent.START);

        for (int i = 0; i < itemLen; ++i) {
            final InventoryMetadataDto item = items.getAcquire(i);

            final JPanel itemPanel = new JPanel(new BorderLayout()) {
                @Override
                public void updateUI() {
                    super.updateUI();
                    applyShadowBorder(this);
                }
            };

            final JPanel itemContentContainer = new JPanel(
                    new MigLayout("flowx, wrap, insets 0", "[grow, fill, center]"));

            final String imagePath = item.displayImage() == null || item.displayImage().isEmpty()
                    ? "Acer Nitro 5 Laptop.png"
                    : item.displayImage();

            final ImagePanel imagePanel = new ImagePanel(AssetLoader.loadImage(imagePath, true));
            final JLabel itemName = new JLabel(
                    HtmlUtils.wrapInHtml(String.format("<p align='center'>%s", item.itemName())));
            final JLabel itemPrice = new JLabel(String.format(HtmlUtils.wrapInHtml("<p align='center'> %s"),
                    StringUtils.formatBigDecimal(item.unitPricePhp())));

            itemName.putClientProperty(FlatClientProperties.STYLE, "font: bold;");

            itemName.setHorizontalAlignment(JLabel.CENTER);
            itemPrice.setHorizontalAlignment(JLabel.CENTER);

            itemContentContainer.add(imagePanel, "grow");
            itemContentContainer.add(itemName, "growx, gaptop 6px");
            itemContentContainer.add(itemPrice, "growx, gaptop 2px");

            itemPanel.add(itemContentContainer);

            leftPanelContentContainer.add(itemPanel, BorderLayout.CENTER);

            itemPanel.addMouseListener(new ItemPanelMouseListener(item._itemStockId(), item.itemName(),
                    item.totalQuantity(), item.unitPricePhp()));
        }

        leftPanelContentContainer.repaint();
        leftPanelContentContainer.revalidate();
    }

    private void buildRightPanelContent() {
        final Cart cart = this.cart.getAcquire();

        removeActionListeners(cartPanel);
        cartPanel.removeAll();

        if (cart.isEmpty()) {
            rightPanel.remove(cartButtonsContainer);
            cartContentContainer.remove(cartMetadataContainer);

            cartPanel.add(new NoItemsInCartPanel());

            rightPanel.repaint();
            rightPanel.revalidate();

            return;
        }

        createRightPanelContents();
    }

    private void createContainers() {
        containerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPanel = new JPanel(new MigLayout("insets 0 2 0 0, flowy, al center center", "[grow, fill, center]",
                "[top]16px[top]4px[top]8px[grow,fill, center]"));
        final JPanel rightPanelWrapper = new JPanel(
                new MigLayout("insets 0, al center center", "[grow, fill, center]", "[grow, fill, center]"));
        rightPanel = new JPanel(new MigLayout("insets 0, wrap", "[grow, fill]", "[grow, fill, top][bottom]"));
        leftPanelHeader = new JPanel(new MigLayout("flowx, insets 0 0 0 12", "[grow,fill]16px[]2px[]push[]"));
        leftPanelContentContainer = new JPanel(
                new ResponsiveLayout(JustifyContent.CENTER, new Dimension(190, -1), 1, 1));
        final JLabel title = new JLabel("Products");
        final JLabel subtitle = new JLabel("Click a product card to add them to cart.");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

        final JScrollPane scroller = new JScrollPane(leftPanelContentContainer);

        leftPanel.putClientProperty(FlatClientProperties.STYLE, "background:tint($Panel.background, 20%);");

        scroller.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        scroller.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        scroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        containerSplitPane.setResizeWeight(0.7);
        containerSplitPane.setContinuousLayout(true);
        containerSplitPane.setOneTouchExpandable(true);

        scroller.getVerticalScrollBar().setUnitIncrement(16);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        leftPanel.add(leftPanelHeader, "growx");
        leftPanel.add(title, "growx");
        leftPanel.add(subtitle, "growx");
        leftPanel.add(scroller, "grow");

        rightPanelWrapper.add(rightPanel, "grow, width ::450px");

        containerSplitPane.add(leftPanel);
        containerSplitPane.add(rightPanelWrapper);

        add(containerSplitPane);
    }

    private Component createItemCard(final CartItem item) {
        final JPanel cartItemPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][right]", "[]"));
        cartItemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        cartItemPanel.setName(String.format("_itemStockId:%s", item._itemStockId()));

        final JLabel productName = new JLabel(item.name());
        final JLabel productPrice = new JLabel(String.format("%s", StringUtils.formatBigDecimal(item.price())));
        final JLabel productQty = new JLabel(String.format("x%s", item.qty()));

        final String decIcon = item.qty() == 1 ? "trash.svg" : "minus.svg";

        final JPanel qtyPanel = new JPanel(new MigLayout("insets 0, wrap 3, al center center", "[]5[]5[]", "[]"));
        final JButton decBtn = new JButton(new SVGIconUIColor(decIcon, 0.5f, "foreground.background"));
        final JButton addBtn = new JButton(new SVGIconUIColor("plus.svg", 0.5f, "foreground.background"));

        decBtn.setName("decrement");
        addBtn.setName("increment");
        productPrice.setName("price");
        productQty.setName("quantity");

        decBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
        addBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
        decBtn.putClientProperty(FlatClientProperties.STYLE, "arc:999;");
        addBtn.putClientProperty(FlatClientProperties.STYLE, "arc:999;");

        addBtn.addActionListener(new IncrementItemQuantityCartActionListener(item._itemStockId()));
        decBtn.addActionListener(new DecrementItemQuantityCartActionListener(item._itemStockId(), item.qty()));

        qtyPanel.add(decBtn, "center");
        qtyPanel.add(productQty, "center");
        qtyPanel.add(addBtn, "center");

        cartItemPanel.add(productName, "growx");
        cartItemPanel.add(productPrice, "gapx 10");
        cartItemPanel.add(qtyPanel, "gapx 10");

        return cartItemPanel;
    }

    private void createLeftPanel() {
        searchTextField = new JTextField();

        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new SVGIconUIColor("search.svg", 0.5f, "TextField.placeholderForeground"));
        searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search products...");

        searchTextField.getDocument().addDocumentListener(new SearchTextFieldDocumentListener());

        buildLeftPanelContent();

        leftPanelHeader.add(searchTextField);
        leftPanelHeader.add(filterPopupMenu.trigger());
    }

    private void createRightPanel() {
        final JPanel container = new JPanel(new BorderLayout()) {
            @Override
            public void updateUI() {
                super.updateUI();
                applyShadowBorder(this);
            }
        };

        cartContentContainer = new JPanel(new MigLayout("insets 0, flowx, wrap", "[grow, fill]"));
        final JLabel title = new JLabel("Cart", new SVGIconUIColor("shopping-cart.svg", 0.75f, "color.primary"),
                JLabel.RIGHT);
        cartPanel = new JPanel(new MigLayout("insets 0, flowx, wrap, al center top", "[grow, fill, center]"));
        final JScrollPane scroller = new JScrollPane(cartPanel);
        checkoutButton = new JButton("Checkout");
        clearCartButton = new JButton("Clear Cart");
        cartMetadataContainer = new JPanel(
                new MigLayout("insets 0 6 6 6, wrap, flowx, gapy 3px", "[grow, fill, center]"));
        cartButtonsContainer = new JPanel(new MigLayout("insets 0 4 4 4, fillx, gapx 9px", "[grow, fill, center]"));
        cartTotalPriceContainer = new JPanel(new MigLayout("insets 0, flowx"));
        cartTotalQuantityContainer = new JPanel(new MigLayout("insets 0, flowx"));
        cartTotalPriceLabel = new JLabel("0.00");
        cartTotalQuantityLabel = new JLabel("");
        final JLabel totalPriceTitle = new JLabel("Total: ");
        final JLabel totalQuantityTitle = new JLabel("Quantity: ");

        title.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
        title.setIconTextGap(16);
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h3");
        title.setHorizontalAlignment(JLabel.LEFT);

        rightPanel.putClientProperty(FlatClientProperties.STYLE, "background:tint($Panel.background, 25%);");

        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        scroller.getHorizontalScrollBar().setUnitIncrement(16);
        scroller.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        scroller.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        scroller.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        cartPanel.putClientProperty(FlatClientProperties.STYLE, "background: null;");
        cartButtonsContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");
        cartTotalPriceContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");
        cartTotalQuantityContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");
        cartMetadataContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");

        cartMetadataContainer.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY),
                        BorderFactory.createEmptyBorder(5, 0, 0, 0)));

        totalPriceTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold;");
        totalQuantityTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold;");

        clearCartButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        checkoutButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        cartTotalPriceContainer.add(totalPriceTitle, "pushx");
        cartTotalPriceContainer.add(cartTotalPriceLabel);

        cartTotalQuantityContainer.add(totalQuantityTitle, "pushx");
        cartTotalQuantityContainer.add(cartTotalQuantityLabel);

        cartMetadataContainer.add(cartTotalQuantityContainer, "growx");
        cartMetadataContainer.add(cartTotalPriceContainer, "growx");

        cartButtonsContainer.add(clearCartButton, "growx");
        cartButtonsContainer.add(checkoutButton, "growx");

        cartContentContainer.add(title);
        cartContentContainer.add(scroller, "grow, pushy");

        buildRightPanelContent();

        container.add(cartContentContainer, BorderLayout.CENTER);

        rightPanel.add(container, "grow");

        checkoutButton.addActionListener(new CheckoutButtonActionListener(this));
        clearCartButton.addActionListener(new ClearCartActionListener());
    }

    private void createRightPanelContents() {
        final Cart cart = this.cart.getAcquire();

        final JPanel headerPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][right][center]", "[]"));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

        final JLabel nameHeader = new JLabel("Product");
        final JLabel priceHeader = new JLabel("Price");
        final JLabel quantityHeader = new JLabel("Quantity");

        nameHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        priceHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        quantityHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

        headerPanel.add(nameHeader, "growx");
        headerPanel.add(priceHeader, "gapright 20");
        headerPanel.add(quantityHeader, "gapright 6");

        cartPanel.add(headerPanel, "growx");

        for (final CartItem item : cart.getAllItems()) {
            cartPanel.add(createItemCard(item), "growx, wrap");
        }

        cartTotalPriceLabel.setText(String.format("%s", cart.totalPrice()));
        cartTotalQuantityLabel.setText(String.format("%s", cart.totalQuantity()));

        cartContentContainer.add(cartMetadataContainer);

        rightPanel.add(cartButtonsContainer, "growx");
        rightPanel.repaint();
        rightPanel.revalidate();
    }

    private Component getComponent(final JComponent parent, final String name) {
        if (parent == null) {
            return null;
        }

        for (final Component c : parent.getComponents()) {
            if (c.getName() != null && c.getName().equals(name)) {
                return c;
            }

            if (c instanceof final JComponent jc) {
                final Component val = getComponent(jc, name);

                if (val != null) {
                    return val;
                }
            }
        }

        return null;
    }

    private void init() {
        filterPopupMenu = new CategoryBrandFilterPopupMenu(this::search);
        inventory = Inventory.getInstance();
        isFetching = new AtomicBoolean(false);
        cart = new AtomicReference<>(new Cart());
        items = new AtomicReferenceArray<>(new InventoryMetadataDto[0]);
        debouncer = new Debouncer(250);
        cart.getAcquire().subscribe(new CartConsumer());

        setLayout(new BorderLayout());

        createContainers();
        createLeftPanel();
        createRightPanel();
    }

    private void loadData() {
        isFetching.set(true);

        SwingUtilities.invokeLater(() -> {
            leftPanelContentContainer.removeAll();

            ((ResponsiveLayout) leftPanelContentContainer.getLayout()).setJustifyContent(JustifyContent.CENTER);

            leftPanelContentContainer.add(new LoadingPanel());
            leftPanelContentContainer.repaint();
            leftPanelContentContainer.revalidate();
        });

        try {
            final InventoryMetadataDto[] items = inventory.getInventoryItemsWithTotalQuantities(
                    searchTextField.getText(), filterPopupMenu.categoryFilters.getAcquire().toArray(String[]::new),
                    filterPopupMenu.brandFilters.getAcquire().toArray(String[]::new), ItemStatus.ACTIVE);

            this.items = new AtomicReferenceArray<>(items);

            filterPopupMenu.populate();

            SwingUtilities.invokeLater(() -> {
                buildLeftPanelContent();

                isFetching.set(false);
            });
        } catch (final InventoryException err) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                    "Failed to load data :(", JOptionPane.ERROR_MESSAGE);

            SwingUtilities.invokeLater(() -> {
                leftPanelContentContainer.removeAll();

                ((ResponsiveLayout) leftPanelContentContainer.getLayout()).setJustifyContent(JustifyContent.CENTER);

                leftPanelContentContainer.add(new ErrorPanel());
                leftPanelContentContainer.repaint();
                leftPanelContentContainer.revalidate();
            });

            isFetching.set(false);
        }
    }

    private void removeActionListeners(final JComponent component) {
        for (final Component c : component.getComponents()) {
            switch (c) {
                case final JButton button:
                    Arrays.stream(button.getActionListeners()).forEach(button::removeActionListener);
                    break;
                case final JComponent co:
                    removeActionListeners(co);
                    break;
                default:
            }
        }
    }

    private void search() {
        debouncer.call(() -> {
            if (isFetching.get()) {
                return;
            }

            SwingUtilities.invokeLater(() -> {
                leftPanelContentContainer.removeAll();

                ((ResponsiveLayout) leftPanelContentContainer.getLayout()).setJustifyContent(JustifyContent.CENTER);

                leftPanelContentContainer.add(new LoadingPanel());
                leftPanelContentContainer.repaint();
                leftPanelContentContainer.revalidate();

                try {
                    items = new AtomicReferenceArray<>(inventory.getInventoryItemsWithTotalQuantities(
                            searchTextField.getText(),
                            filterPopupMenu.categoryFilters.getAcquire().toArray(String[]::new),
                            filterPopupMenu.brandFilters.getAcquire().toArray(String[]::new), ItemStatus.ACTIVE));

                    SwingUtilities.invokeLater(() -> {
                        buildLeftPanelContent();
                    });
                } catch (final InventoryException err) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                            "Failed to load data :(", JOptionPane.ERROR_MESSAGE);

                    SwingUtilities.invokeLater(() -> {
                        leftPanelContentContainer.removeAll();

                        ((ResponsiveLayout) leftPanelContentContainer.getLayout())
                                .setJustifyContent(JustifyContent.CENTER);

                        leftPanelContentContainer.add(new ErrorPanel());
                        leftPanelContentContainer.repaint();
                        leftPanelContentContainer.revalidate();
                    });
                }
            });
        });
    }

    private void updateCartTotals() {
        final Cart acquiredCart = cart.getAcquire();

        cartTotalPriceLabel.setText(String.format("%s", StringUtils.formatBigDecimal(acquiredCart.totalPrice())));
        cartTotalQuantityLabel.setText(String.format("%s", acquiredCart.totalQuantity()));
    }

    private class AddToCartDialog extends JDialog {
        public AddToCartDialog(final int id, final String itemName, final int totalQuantity,
                final BigDecimal unitPricePhp, final Window owner) {
            super(owner, "Add to Cart", Dialog.ModalityType.APPLICATION_MODAL);

            putClientProperty(FlatClientProperties.STYLE, "background:tint($Panel.background,20%);");
            setLayout(new MigLayout("insets 9, flowx, wrap", "[grow, fill, center]"));

            final JLabel title = new JLabel("Add to Cart");
            final JLabel subtitle = new JLabel(
                    HtmlUtils.wrapInHtml(String.format("<p align='center'>This will add %s to the cart.", itemName)));

            title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3 primary");
            subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            subtitle.putClientProperty(FlatClientProperties.STYLE, "font:11;");

            final JLabel quantityLabel = new JLabel(String.format("Quantity (Max %s)", totalQuantity));
            final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, totalQuantity, 1));

            final JButton cancelButton = new JButton("Cancel");
            final JButton confirmButton = new JButton("Confirm");

            cancelButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            confirmButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

            cancelButton.addActionListener(new CancelButtonActionListener());
            confirmButton.addActionListener(
                    new ConfirmButtonActionListener(this, id, itemName, totalQuantity, unitPricePhp, quantitySpinner));

            add(title, "wrap");
            add(subtitle, "wrap, gapy 2px");

            add(quantityLabel, "gapy 4px");
            add(quantitySpinner, "push, gapy 2px");

            add(cancelButton, "split 2, gapy 16px");
            add(confirmButton, "gapx 4px");

            pack();
            setLocationRelativeTo(owner);
        }

        private class CancelButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        }

        private class ConfirmButtonActionListener implements ActionListener {
            private final JDialog addToCartDialog;
            private final int id;
            private final String itemName;
            private final JSpinner numberSpinner;
            private final int totalQuantity;
            private final BigDecimal unitPricePhp;

            public ConfirmButtonActionListener(final JDialog addToCartDialog, final int id, final String itemName,
                    final int totalQuantity, final BigDecimal unitPricePhp, final JSpinner numberSpinner) {
                this.id = id;
                this.itemName = itemName;
                this.totalQuantity = totalQuantity;
                this.unitPricePhp = unitPricePhp;
                this.numberSpinner = numberSpinner;
                this.addToCartDialog = addToCartDialog;
            }

            @Override
            public void actionPerformed(final ActionEvent e) {
                final Integer val = (Integer) numberSpinner.getValue();

                final Cart cartVal = cart.getAcquire();

                try {
                    if (cartVal.exists(id)) {
                        cartVal.increaseItemQty(id, val);
                    } else {
                        cartVal.addItem(new CartItem(id, itemName, totalQuantity, val, unitPricePhp));
                    }
                } catch (InsufficientStockException | NegativeQuantityException err) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(addToCartDialog, err.getMessage(), err.getClass().getName(),
                                JOptionPane.ERROR_MESSAGE);
                    });

                    return;
                }

                dispose();
            }
        }
    }

    private class CartConsumer implements Consumer<CartEvent> {
        @Override
        public void accept(final CartEvent cartEvent) {
            final Cart acquiredCart = cart.getAcquire();
            final CartItem cartItem = cartEvent.payload();
            final CartItem previousCartItem = cartEvent.previousPayload();

            SwingUtilities.invokeLater(() -> {
                switch (cartEvent.eventType()) {
                    case INCREASE_ITEM_QTY:
                    case INCREMENT_ITEM:
                    case DECREASE_ITEM_QTY:
                    case DECREMENT_ITEM: {
                        final JComponent parent = (JComponent) getComponent(cartPanel,
                                String.format("_itemStockId:%s", cartItem._itemStockId()));
                        final JButton decrementButton = (JButton) getComponent(parent, "decrement");
                        final JLabel priceLabel = (JLabel) getComponent(parent, "price");
                        final JLabel qtyLabel = (JLabel) getComponent(parent, "quantity");

                        switch (cartEvent.eventType()) {
                            case INCREASE_ITEM_QTY:
                            case INCREMENT_ITEM: {
                                if (previousCartItem.qty() == 1) {
                                    decrementButton
                                            .setIcon(new SVGIconUIColor("minus.svg", 0.5f, "foregorund.background"));
                                }

                                priceLabel.setText(
                                        String.format("%s", StringUtils.formatBigDecimal(cartItem.getTotalPrice())));
                                qtyLabel.setText(String.format("x%s", cartItem.qty()));

                                updateCartTotals();
                            }
                                break;
                            case DECREASE_ITEM_QTY:
                            case DECREMENT_ITEM: {
                                if (cartItem.qty() == 1) {
                                    decrementButton
                                            .setIcon(new SVGIconUIColor("trash.svg", 0.5f, "foregorund.background"));
                                }

                                priceLabel.setText(
                                        String.format("%s", StringUtils.formatBigDecimal(cartItem.getTotalPrice())));
                                qtyLabel.setText(String.format("x%s", cartItem.qty()));

                                updateCartTotals();
                            }
                                break;
                            default:
                        }
                    }
                        break;
                    case REMOVE_ITEM: {
                        if (acquiredCart.isEmpty()) {
                            buildRightPanelContent();
                        } else {
                            cartPanel.remove((JComponent) getComponent(cartPanel,
                                    String.format("_itemStockId:%s", cartItem._itemStockId())));
                            cartPanel.repaint();
                            cartPanel.revalidate();

                            updateCartTotals();
                        }
                    }
                        break;
                    case CLEAR: {
                        buildRightPanelContent();
                    }
                        break;
                    case ADD_ITEM: {
                        if (acquiredCart.getAllItems().size() == 1) {
                            buildRightPanelContent();
                        } else {
                            cartPanel.add(createItemCard(cartItem));
                            cartPanel.repaint();
                            cartPanel.revalidate();
                        }

                        updateCartTotals();
                    }
                        break;
                }
            });
        }
    }

    private class CheckoutButtonActionListener implements ActionListener {
        private final FormPosShop formPosShop;

        public CheckoutButtonActionListener(final FormPosShop formPosShop) {
            this.formPosShop = formPosShop;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
        }
    }

    private class ClearCartActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {

            final int chosenOption = JOptionPane.showConfirmDialog(
                    SwingUtilities.getWindowAncestor(cartButtonsContainer), "Are you sure you want to clear your cart?",
                    "Clear Cart", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            switch (chosenOption) {
                case JOptionPane.YES_OPTION:
                    cart.getAcquire().clearCart();
                    break;
            }
        }
    }

    private class DecrementItemQuantityCartActionListener implements ActionListener {
        private final int id;
        private final int qty;

        public DecrementItemQuantityCartActionListener(final int id, final int qty) {
            this.id = id;
            this.qty = qty;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            final Cart acquiredCart = cart.getAcquire();

            if (qty == 1) {
                acquiredCart.removeItem(id);
            } else {
                try {
                    acquiredCart.decrementItem(id);
                } catch (final NegativeQuantityException err) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(cartPanel), err.getMessage(),
                            err.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class ErrorPanel extends JPanel {
        public ErrorPanel() {
            final JLabel text = new JLabel(HtmlUtils.wrapInHtml("Something went wrong! :("));

            putClientProperty(FlatClientProperties.STYLE, "background: null;");

            add(text, BorderLayout.WEST);
        }
    }

    private class IncrementItemQuantityCartActionListener implements ActionListener {
        private final int id;

        public IncrementItemQuantityCartActionListener(final int id) {
            this.id = id;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            final Cart acquiredCart = cart.getAcquire();

            try {
                acquiredCart.incrementItem(id);
            } catch (final InsufficientStockException err) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(cartPanel), err.getMessage(),
                        err.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ItemPanelMouseListener extends MouseAdapter {
        private final int id;
        private final String itemName;
        private final int totalQuantity;
        private final BigDecimal unitPricePhp;

        public ItemPanelMouseListener(final int id, final String itemName, final int totalQuantity,
                final BigDecimal unitPricePhp) {
            this.id = id;
            this.itemName = itemName;
            this.totalQuantity = totalQuantity;
            this.unitPricePhp = unitPricePhp;
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
            new AddToCartDialog(id, itemName, totalQuantity, unitPricePhp,
                    SwingUtilities.getWindowAncestor(containerSplitPane)).setVisible(true);
        }
    }

    private class LoadingPanel extends JPanel {
        public LoadingPanel() {
            setLayout(new BorderLayout());

            putClientProperty(FlatClientProperties.STYLE, "background: null;");

            final JLabel loading = new JLabel("Loading...");

            add(loading, BorderLayout.WEST);
        }
    }

    private class NoItemsInCartPanel extends JPanel {
        public NoItemsInCartPanel() {
            setLayout(new BorderLayout());

            putClientProperty(FlatClientProperties.STYLE, "background: null;");

            final JLabel text = new JLabel("No items in cart yet :(");

            add(text, BorderLayout.NORTH);
        }
    }

    private class NoResultsPanel extends JPanel {
        public NoResultsPanel() {
            setLayout(new BorderLayout());

            putClientProperty(FlatClientProperties.STYLE, "background: null;");

            final JLabel noResults = new JLabel(HtmlUtils.wrapInHtml("<p align='center'>No results were found :("));
            add(noResults, BorderLayout.NORTH);
        }
    }

    private class SearchTextFieldDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(final DocumentEvent e) {
        }

        @Override
        public void insertUpdate(final DocumentEvent e) {
            search();
        }

        @Override
        public void removeUpdate(final DocumentEvent e) {
            search();
        }
    }
}
