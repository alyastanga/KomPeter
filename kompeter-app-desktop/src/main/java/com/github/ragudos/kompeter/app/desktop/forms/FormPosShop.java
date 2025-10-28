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
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.components.modal.SimpleMessageModal;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout.JustifyContent;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemCategoryDto;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.inventory.ItemService;
import com.github.ragudos.kompeter.pointofsale.Cart;
import com.github.ragudos.kompeter.pointofsale.CartItem;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.ModalDialog;
import raven.modal.component.DropShadowBorder;
import raven.modal.component.SimpleModalBorder;
import raven.modal.listener.ModalCallback;
import raven.modal.listener.ModalController;

@SystemForm(name = "Point of Sale Shop", description = "The point of sale shop", tags = {"sales", "shop"})
public class FormPosShop extends Form {
    private ArrayList<JCheckBoxMenuItem> brandCheckBoxes;
    private AtomicReference<ArrayList<String>> brandFilters;
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
    private ArrayList<JCheckBoxMenuItem> categoryCheckBoxes;
    private AtomicReference<ArrayList<String>> categoryFilters;
    private JButton checkoutButton;
    private JButton clearCartButton;
    private JSplitPane containerSplitPane;
    private JButton filterButtonTrigger;
    private JPopupMenu filterPopupMenu;
    private JaroWinklerSimilarity fuzzySimilarity;
    private AtomicBoolean isFetching;
    private AtomicReference<ArrayList<InventoryMetadataDto>> items;
    private JPanel leftPanel;
    private JPanel leftPanelContentContainer;
    private JPanel leftPanelHeader;
    private JPanel rightPanel;
    private JTextField searchTextField;

    @Override
    public void formBeforeClose(FormBeforeCloseCallback cb) {
        if (cart.getAcquire().isEmpty()) {
            cb.beforeClose(true);

            return;
        }

        ModalDialog.showModal(this,
                new SimpleMessageModal(SimpleMessageModal.Type.WARNING,
                        "Cart is not empty. Would you like to save the current cart's state?", "Save or Remove",
                        SimpleModalBorder.YES_NO_CANCEL_OPTION, new FormBeforeCloseModalActionCallback(cb)));
    }

    @Override
    public void formInit() {
        init();
        formRefresh();
        repaint();
        revalidate();
    }

    @Override
    public void formOpen() {
    }

    @Override
    public void formRefresh() {
        new Thread(this::loadData, "Load Point of Sale Shop Data").start();
    }

    private void applyShadowBorder(JPanel panel) {
        if (panel != null) {
            panel.setBorder(new DropShadowBorder(new Insets(4, 8, 12, 8), 1, 25));
        }
    }

    private void buildFilterPopupMenu() {
        removeAllItemListenersOfPopupMenu(filterPopupMenu);
        filterPopupMenu.removeAll();

        JLabel categoryLabel = new JLabel("Category");
        JLabel brandLabel = new JLabel("Brand");

        filterPopupMenu.add(categoryLabel);

        categoryCheckBoxes.forEach((c -> {
            filterPopupMenu.add(c);

            c.addItemListener(new PopupMenuCheckboxItemListener());
        }));

        filterPopupMenu.addSeparator();

        filterPopupMenu.add(brandLabel);

        brandCheckBoxes.forEach((c -> {
            filterPopupMenu.add(c);

            c.addItemListener(new PopupMenuCheckboxItemListener());
        }));
    }

    private void buildLeftPanelContent() {
        if (isFetching.get()) {
            return;
        }

        leftPanelContentContainer.removeAll();

        ResponsiveLayout layout = (ResponsiveLayout) leftPanelContentContainer.getLayout();

        if (items.getAcquire().isEmpty()) {
            layout.setJustifyContent(JustifyContent.CENTER);
            leftPanelContentContainer.add(new NoResultsPanel());

            leftPanelContentContainer.repaint();
            leftPanelContentContainer.revalidate();

            return;
        }

        layout.setJustifyContent(JustifyContent.CENTER);

        for (InventoryMetadataDto item : filterItems()) {
            JPanel itemPanel = new JPanel(new BorderLayout()) {
                @Override
                public void updateUI() {
                    super.updateUI();
                    applyShadowBorder(this);
                }
            };

            JPanel itemContentContainer = new JPanel(
                    new MigLayout("flowx, wrap, insets 3", "[grow, center, fill, 100]"));

            AvatarIcon itemIcon = new AvatarIcon(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "placeholder.svg"),
                    90, 90, 0f);
            JLabel itemName = new JLabel(HtmlUtils.wrapInHtml(String.format("<p align='center'>%s", item.itemName())));
            JLabel itemPrice = new JLabel(
                    String.format(HtmlUtils.wrapInHtml("<p align='center'>₱ %s"), item.itemPricePhp()));

            itemName.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
            itemPrice.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

            itemName.setHorizontalAlignment(JLabel.CENTER);
            itemPrice.setHorizontalAlignment(JLabel.CENTER);

            itemPanel.setName(String.format("%s;%s;%s", item._itemId(), item._itemStockId(), item._stockLocationId()));
            itemIcon.setType(AvatarIcon.Type.MASK_SQUIRCLE);

            itemContentContainer.add(new JLabel(itemIcon), "grow");
            itemContentContainer.add(itemName, "growx, gapy 14px");
            itemContentContainer.add(itemPrice, "growx, gapy 6px");

            itemPanel.add(itemContentContainer);

            leftPanelContentContainer.add(itemPanel, BorderLayout.CENTER);

            itemPanel.addMouseListener(new ItemPanelMouseListener(item));
        }

        leftPanelContentContainer.repaint();
        leftPanelContentContainer.revalidate();
    }

    private void buildRightPanelContent() {
        Cart cart = this.cart.getAcquire();

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

        JPanel headerPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][right][center]", "[]"));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

        JLabel nameHeader = new JLabel("Product");
        JLabel priceHeader = new JLabel("Price");
        JLabel quantityHeader = new JLabel("Quantity");

        nameHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");
        priceHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");
        quantityHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");

        headerPanel.add(nameHeader, "growx");
        headerPanel.add(priceHeader, "gapright 20");
        headerPanel.add(quantityHeader, "gapright 6");

        cartPanel.add(headerPanel, "growx");

        for (CartItem item : cart.getAllItems()) {
            JPanel cartItemPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][right]", "[]"));
            cartItemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

            JLabel productName = new JLabel(item.productName());
            JLabel productPrice = new JLabel("₱" + item.price());
            JLabel productQty = new JLabel("x" + item.qty());

            JPanel qtyPanel = new JPanel(new MigLayout("insets 0, wrap 3, al center center", "[]5[]5[]", "[]"));
            JButton decBtn = new JButton(new SVGIconUIColor("minus.svg", 0.75f, "foreground.background"));
            JButton addBtn = new JButton(new SVGIconUIColor("plus.svg", 0.75f, "foreground.background"));

            decBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            addBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            decBtn.putClientProperty(FlatClientProperties.STYLE, "arc:999;");
            addBtn.putClientProperty(FlatClientProperties.STYLE, "arc:999;");

            addBtn.addActionListener(new IncrementItemQuantityCartActionListener(item));
            decBtn.addActionListener(new DecrementItemQuantityCartActionListener(item));

            qtyPanel.add(decBtn, "center");
            qtyPanel.add(productQty, "center");
            qtyPanel.add(addBtn, "center");

            cartItemPanel.add(productName, "growx");
            cartItemPanel.add(productPrice, "gapx 10");
            cartItemPanel.add(qtyPanel, "gapx 10");

            cartPanel.add(cartItemPanel, "growx, wrap");
        }

        cartTotalPriceLabel.setText(String.format("₱%s", cart.totalPrice()));
        cartTotalQuantityLabel.setText(String.format("%s", cart.totalQuantity()));

        cartContentContainer.add(cartMetadataContainer);

        rightPanel.add(cartButtonsContainer, "growx");
        rightPanel.repaint();
        rightPanel.revalidate();
    }

    private void createContainers() {
        containerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPanel = new JPanel(new MigLayout("insets 0, flowy, al center center", "[grow, fill]",
                "[top]16px[top]4px[top]8px[grow,fill]"));
        JPanel rightPanelWrapper = new JPanel(
                new MigLayout("insets 0, al center center", "[grow, fill, center]", "[grow, fill, center]"));
        rightPanel = new JPanel(new MigLayout("insets 0, wrap", "[grow, fill]", "[grow, fill, top][bottom]"));
        leftPanelHeader = new JPanel(new MigLayout("flowx, insets 0 0 0 12", "[grow,fill]16px[]2px[]push[]"));
        leftPanelContentContainer = new JPanel(
                new ResponsiveLayout(JustifyContent.CENTER, new Dimension(-1, -1), 9, 9));
        JLabel title = new JLabel("Products");
        JLabel subtitle = new JLabel("Click a product card to add them to cart.");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h2");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted h3");

        JScrollPane scroller = new JScrollPane(leftPanelContentContainer);

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

    private void createLeftPanel() {
        searchTextField = new JTextField();

        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new SVGIconUIColor("search.svg", 0.5f, "TextField.placeholderForeground"));
        searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search products...");

        filterButtonTrigger = new JButton(new SVGIconUIColor("filter.svg", 0.5f, "foreground.muted"));

        filterButtonTrigger.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        filterPopupMenu = new JPopupMenu();

        buildFilterPopupMenu();

        filterButtonTrigger.addActionListener(new FilterButtonTriggerActionListener());
        searchTextField.getDocument().addDocumentListener(new SearchTextFieldDocumentListener());

        buildLeftPanelContent();

        leftPanelHeader.add(searchTextField);
        leftPanelHeader.add(filterButtonTrigger);
    }

    private void createRightPanel() {
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            public void updateUI() {
                super.updateUI();
                applyShadowBorder(this);
            }
        };

        cartContentContainer = new JPanel(new MigLayout("insets 0, flowx, wrap", "[grow, fill]"));
        JLabel title = new JLabel("Cart", new SVGIconUIColor("shopping-cart.svg", 0.75f, "color.primary"),
                JLabel.RIGHT);
        cartPanel = new JPanel(new MigLayout("insets 0, flowx, wrap, al center top", "[grow, fill]", ""));
        JScrollPane scroller = new JScrollPane(cartPanel);
        checkoutButton = new JButton("Checkout");
        clearCartButton = new JButton("Clear Cart");
        cartMetadataContainer = new JPanel(new MigLayout("insets 0 6 6 6, wrap, flowx, gapy 3px"));
        cartButtonsContainer = new JPanel(new MigLayout("insets 0 4 4 4, fillx, gapx 9px"));
        cartTotalPriceContainer = new JPanel(new MigLayout("insets 0, flowx", "[]push[]"));
        cartTotalQuantityContainer = new JPanel(new MigLayout("insets 0, flowx", "[]push[]"));
        cartTotalPriceLabel = new JLabel("₱0.00");
        cartTotalQuantityLabel = new JLabel("");
        JLabel totalPriceTitle = new JLabel("Total: ");
        JLabel totalQuantityTitle = new JLabel("Quantity: ");

        title.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
        title.setIconTextGap(16);
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h1");
        title.setHorizontalAlignment(JLabel.LEFT);

        rightPanel.putClientProperty(FlatClientProperties.STYLE, "background:tint($Panel.background, 25%);");

        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        scroller.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        scroller.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

        cartPanel.putClientProperty(FlatClientProperties.STYLE, "background: null;");
        cartButtonsContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");
        cartTotalPriceContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");
        cartTotalQuantityContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");
        cartMetadataContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");

        cartMetadataContainer.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY),
                        BorderFactory.createEmptyBorder(5, 0, 0, 0)));

        totalPriceTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        totalQuantityTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        cartTotalPriceLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        cartTotalQuantityLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

        clearCartButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted h3");
        checkoutButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h3");

        cartTotalPriceContainer.add(totalPriceTitle);
        cartTotalPriceContainer.add(cartTotalPriceLabel);

        cartTotalQuantityContainer.add(totalQuantityTitle);
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

    private List<InventoryMetadataDto> filterItems() {
        return items.getAcquire();
    }

    private void init() {
        isFetching = new AtomicBoolean(true);
        cart = new AtomicReference<>(new Cart());
        categoryFilters = new AtomicReference<>(new ArrayList<>());
        brandFilters = new AtomicReference<>(new ArrayList<>());
        items = new AtomicReference<>(new ArrayList<>());
        categoryCheckBoxes = new ArrayList<>();
        brandCheckBoxes = new ArrayList<>();

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

        ItemService itemService = new ItemService();

        try {
            List<ItemBrandDto> itemBrands = itemService.showAllBrands();
            List<ItemCategoryDto> itemCategories = itemService.showAllCategories();
            List<InventoryMetadataDto> items = itemService.getAllItems();

            this.items.set((ArrayList<InventoryMetadataDto>) items);

            SwingUtilities.invokeLater(() -> {
                brandCheckBoxes.clear();
                categoryCheckBoxes.clear();

                itemBrands.forEach((brand) -> {
                    JCheckBoxMenuItem c = new JCheckBoxMenuItem(brand.name());

                    brandCheckBoxes.add(c);
                });

                itemCategories.forEach((category) -> {
                    JCheckBoxMenuItem c = new JCheckBoxMenuItem(category.name());

                    categoryCheckBoxes.add(c);
                });

                buildFilterPopupMenu();
                buildLeftPanelContent();
            });
        } catch (InventoryException err) {
            ModalDialog.showModal(this, new SimpleMessageModal(SimpleMessageModal.Type.ERROR, err.getMessage(),
                    "Failed to load data", SimpleModalBorder.CLOSE_OPTION, null));
        } finally {
            isFetching.set(false);
        }
    }

    private void removeActionListeners(JComponent component) {
        for (Component c : component.getComponents()) {
            switch (c) {
                case JButton button -> Arrays.stream(button.getActionListeners()).forEach(button::removeActionListener);
                case JComponent co -> removeActionListeners(co);
                default -> {
                }
            }
        }
    }

    private void removeAllItemListenersOfPopupMenu(JPopupMenu menu) {
        for (Component component : menu.getComponents()) {
            if (component instanceof JMenuItem item) {
                for (ItemListener l : item.getItemListeners()) {
                    item.removeItemListener(l);
                }
            }
        }
    }

    private void search() {
    }

    private class AddToCartDialog extends JDialog {
        final InventoryMetadataDto item;
        final Window owner;

        public AddToCartDialog(InventoryMetadataDto item, Window owner) {
            super(owner, "Add to Cart", Dialog.ModalityType.APPLICATION_MODAL);

            this.item = item;
            this.owner = owner;

            putClientProperty(FlatClientProperties.STYLE, "background:tint($Panel.background,20%);");
            setLayout(new MigLayout("insets 9, flowx, wrap", "[grow, fill, center]"));

            JTextArea subtitle = new JTextArea("This will add " + item.itemName() + " to the cart.");

            subtitle.setCaret(new DefaultCaret() {
                @Override
                public void paint(Graphics g) {
                    // TODO Auto-generated method stub
                }
            });

            subtitle.setWrapStyleWord(true);
            subtitle.setEditable(false);

            subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
            subtitle.putClientProperty(FlatClientProperties.STYLE, "margin:0,0,0,0;foreground:$color.muted;");

            JLabel quantityLabel = new JLabel("Quantity");
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

            JButton cancelButton = new JButton("Cancel");
            JButton confirmButton = new JButton("Confirm");

            quantityLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

            cancelButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            confirmButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

            cancelButton.addActionListener(new CancelButtonActionListener());
            confirmButton.addActionListener(new ConfirmButtonActionListener(quantitySpinner));

            add(subtitle);

            add(quantityLabel, "gapy 9px");
            add(quantitySpinner, "push, gapy 2px");

            add(cancelButton, "split 2, gapy 32px");
            add(confirmButton, "gapx 9px");

            pack();
            setLocationRelativeTo(owner);
        }

        private class CancelButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }

        private class ConfirmButtonActionListener implements ActionListener {
            private final JSpinner numberSpinner;

            public ConfirmButtonActionListener(JSpinner numberSpinner) {
                this.numberSpinner = numberSpinner;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Integer val = (Integer) numberSpinner.getValue();
                Cart cartVal = cart.getAcquire();

                cartVal.addItem(new CartItem(item._itemStockId(), item.itemName(), item.categoryName(),
                        item.brandName(), val, item.itemPricePhp()));
                dispose();

                SwingUtilities.invokeLater(() -> {
                    buildRightPanelContent();
                });
            }
        }
    }

    private class CheckoutButtonActionListener implements ActionListener {
        private FormPosShop formPosShop;

        public CheckoutButtonActionListener(FormPosShop formPosShop) {
            this.formPosShop = formPosShop;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    private class ClearCartActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cart.getAcquire().clearCart();
            buildRightPanelContent();
        }
    }

    private class DecrementItemQuantityCartActionListener implements ActionListener {
        private final CartItem item;

        public DecrementItemQuantityCartActionListener(CartItem item) {
            this.item = item;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cart.getAcquire().replaceItem(item.decrement());
            buildRightPanelContent();
        }
    }

    private class FilterButtonTriggerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            filterPopupMenu.show(filterButtonTrigger, 0, filterButtonTrigger.getHeight());
        }
    }

    private final class FormBeforeCloseModalActionCallback implements ModalCallback {
        private final FormBeforeCloseCallback cb;

        public FormBeforeCloseModalActionCallback(FormBeforeCloseCallback cb) {
            this.cb = cb;
        }

        @Override
        public void action(ModalController controller, int action) {
            switch (action) {
                case SimpleModalBorder.CANCEL_OPTION :
                    cb.beforeClose(false);
                    controller.consume();
                    controller.close();
                    break;
                case SimpleModalBorder.YES_OPTION :
                    cb.beforeClose(true);
                    controller.consume();
                    controller.close();
                    break;
                case SimpleModalBorder.NO_OPTION :
                    cart.getAcquire().clearCart();
                    removeActionListeners(cartPanel);
                    cartPanel.removeAll();
                    rightPanel.remove(cartButtonsContainer);
                    rightPanel.repaint();
                    rightPanel.revalidate();

                    cb.beforeClose(true);
                    controller.consume();
                    controller.close();
                    break;
            }
        }
    }

    private class IncrementItemQuantityCartActionListener implements ActionListener {
        private final CartItem item;

        public IncrementItemQuantityCartActionListener(CartItem item) {
            this.item = item;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cart.getAcquire().replaceItem(item.increment());
            buildRightPanelContent();
        }
    }

    private class ItemPanelMouseListener extends MouseAdapter {
        private InventoryMetadataDto item;

        public ItemPanelMouseListener(InventoryMetadataDto item) {
            this.item = item;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            new AddToCartDialog(item, SwingUtilities.getWindowAncestor(containerSplitPane)).setVisible(true);
        }
    }

    private class LoadingPanel extends JPanel {
        public LoadingPanel() {
            setLayout(new BorderLayout());

            putClientProperty(FlatClientProperties.STYLE, "background: null;");

            JLabel loading = new JLabel("Loading...");

            loading.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");

            add(loading, BorderLayout.CENTER);
        }
    }

    private class NoItemsInCartPanel extends JPanel {
        public NoItemsInCartPanel() {
            setLayout(new BorderLayout());

            add(new JLabel("No items in cart yet :("), BorderLayout.NORTH);
        }
    }

    private class NoResultsPanel extends JPanel {
        public NoResultsPanel() {
            setLayout(new BorderLayout());

            putClientProperty(FlatClientProperties.STYLE, "background: null;");

            JLabel noResults = new JLabel("No results were found :(");

            add(noResults, BorderLayout.NORTH);
        }
    }

    private class PopupMenuCheckboxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            search();
        }
    }

    private class SearchTextFieldDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            search();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            search();
        }
    }
}
