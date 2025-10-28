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
import java.awt.Dimension;
import java.awt.Insets;
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
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FormPosShop form = new FormPosShop();
            form.setVisible(true);
        });
    }

    private ArrayList<JCheckBoxMenuItem> brandCheckBoxes;
    private AtomicReference<ArrayList<String>> brandFilters;
    private JButton cancelButton;
    private AtomicReference<Cart> cart;
    private JPanel cartButtonsContainer;
    private JPanel cartListPanel;
    private JPanel cartPanel;
    // No need atomic reference since we'll always access these in EDT
    private ArrayList<JCheckBoxMenuItem> categoryCheckBoxes;
    private AtomicReference<ArrayList<String>> categoryFilters;
    private JButton checkoutButton;
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
    private JPanel rightPanelContentContainer;

    private JPanel rightPanelHeader;

    private JTextField searchTextField;

    @Override
    public void formBeforeClose(FormBeforeCloseCallback cb) {
        Cart cart = this.cart.getAcquire();

        if (cart.isEmpty()) {
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

            itemIcon.setType(AvatarIcon.Type.MASK_SQUIRCLE);

            itemContentContainer.add(new JLabel(itemIcon), "grow");
            itemContentContainer.add(itemName, "growx, gapy 14px");
            itemContentContainer.add(itemPrice, "growx, gapy 6px");

            itemContentContainer.setBackground(Color.WHITE);

            itemPanel.add(itemContentContainer);

            leftPanelContentContainer.add(itemPanel, BorderLayout.CENTER);

            itemPanel.addMouseListener(new ItemPanelMouseListener(itemPanel, item));
        }

        leftPanelContentContainer.repaint();
        leftPanelContentContainer.revalidate();
    }

    private void buildRightPanelContent() {
        rightPanelContentContainer = new JPanel(new BorderLayout());

        cartListPanel = new JPanel(new MigLayout("wrap, insets 10", "[grow]", "[]"));
        JScrollPane cartScroll = new JScrollPane(cartListPanel);
        cartScroll.setBorder(BorderFactory.createEmptyBorder());

        /// idk how to use this
        // cartListPanel.putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT,
        // "20");
        cartListPanel.setBackground(Color.white);

        JPanel receiptPanel = new JPanel(new BorderLayout());
        applyShadowBorder(receiptPanel);
        receiptPanel.add(cartScroll, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new MigLayout("insets 10, fillx, wrap 2", "[grow][right]", "[]10[]"));
        totalPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));

        JLabel totalLabel = new JLabel("Grand Total:");
        JLabel totalPrice = new JLabel("₱" + cart.get().totalPrice());

        JButton checkOutBtn = new JButton("Checkout");
        JButton clearBtn = new JButton("Clear Cart");

        totalLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        totalPrice.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");

        checkOutBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        checkOutBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h3");

        checkOutBtn.addActionListener(e -> {
            if (cart.get().getAllItems().isEmpty()) {
                SimpleMessageModal message = new SimpleMessageModal(SimpleMessageModal.Type.WARNING,
                        "Your cart is empty!", "Checkout Error", SimpleMessageModal.CLOSE_OPTION, null);
                return;
            }

            SimpleMessageModal confirm = new SimpleMessageModal(SimpleMessageModal.Type.INFO, "Confirm transaction?",
                    "Checkout", SimpleMessageModal.OK_CANCEL_OPTION, (con, opt) -> {
                        if (opt == SimpleMessageModal.OK_OPTION) {
                            cart.get().destroy();
                            updateCartDisplay();

                            ModalDialog.showModal(rightPanel, new SimpleMessageModal(SimpleMessageModal.Type.SUCCESS,
                                    "Checkout completed!", "Success", SimpleMessageModal.CLOSE_OPTION, null));
                        }
                    });

            ModalDialog.showModal(rightPanel, confirm);
        });

        clearBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        clearBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "secondary h3");

        clearBtn.addActionListener(e -> {
            if (cart.get().getAllItems().isEmpty())
                return;

            SimpleMessageModal clearCart = new SimpleMessageModal(SimpleMessageModal.Type.WARNING,
                    "Are you sure you want to clear your cart?", "Clear Cart", SimpleMessageModal.OK_CANCEL_OPTION,
                    (con, opt) -> {
                        if (opt == SimpleMessageModal.OK_OPTION) {
                            cart.get().destroy();
                            updateCartDisplay();
                        }
                    });
            ModalDialog.showModal(rightPanel, clearCart);
        });

        totalPanel.add(totalLabel, "left");
        totalPanel.add(totalPrice, "right, wrap");

        totalPanel.add(checkOutBtn, "split 2, growx");
        totalPanel.add(clearBtn, "growx");

        rightPanelContentContainer.add(receiptPanel, BorderLayout.CENTER);
        rightPanelContentContainer.add(totalPanel, BorderLayout.SOUTH);

        rightPanel.add(rightPanelContentContainer, "grow, push");
    }

    private JPanel createCartItemCard(CartItem cartItem) {
        JPanel container = new JPanel(new MigLayout("flowx, gapx 9px, insets 6", "[150][grow, fill]", "fill"));

        container.setName(String.format("_itemStockId:%s", cartItem._itemStockId()));

        AvatarIcon itemIcon = new AvatarIcon(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "placeholder.svg"), 76,
                76, 1f);
        JPanel contentContainer = new JPanel(new MigLayout("insets 0"));
        JLabel name = new JLabel(cartItem.productName());
        JLabel categoryLabel = new JLabel(cartItem.category());
        JLabel brandLabel = new JLabel(cartItem.brand());
        JLabel priceLabel = new JLabel(String.format("₱ %s", cartItem.getTotalPrice()));

        JButton minusButton = new JButton(new SVGIconUIColor("minus.svg", 0.5f, "foreground.background"));
        JLabel quantityLabel = new JLabel(cartItem.qty() + "");
        JButton plusButton = new JButton(new SVGIconUIColor("plus.svg", 0.5f, "foreground.background"));

        minusButton.setName("minus");
        quantityLabel.setName("quantity");
        plusButton.setName("plus");

        name.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

        minusButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
        plusButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");

        contentContainer.add(name, "wrap");
        contentContainer.add(categoryLabel, "split 2, gapy 6px");
        contentContainer.add(brandLabel, "wrap, gapx 2px");
        contentContainer.add(priceLabel);
        contentContainer.add(minusButton, "split 3, gapy 18px");
        contentContainer.add(quantityLabel, "gapx 6px");
        contentContainer.add(plusButton, "gapx 6px, wrap");

        container.add(new JLabel(itemIcon));
        container.add(contentContainer, "grow");

        return container;
    }

    private void createContainers() {
        containerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        leftPanel = new JPanel(
                new MigLayout("flowy, gapy 18px, al center center", "[grow, fill]", "[top][grow,fill][bottom]"));
        leftPanelHeader = new JPanel(new MigLayout("flowx, insets 0 0 0 12", "[grow,fill]16px[]push[]"));
        leftPanelContentContainer = new JPanel(
                new ResponsiveLayout(JustifyContent.CENTER, new Dimension(-1, -1), 9, 9));

        rightPanel = new JPanel(new MigLayout("flowy, al center top", "[grow]", "[top]"));
        rightPanelHeader = new JPanel(new MigLayout("flowx, insets 0 0 0 12", "[grow,fill]16px[]push[]"));
        rightPanelContentContainer = new JPanel(
                new ResponsiveLayout(JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 9, 9));

        JLabel title = new JLabel("Products");
        JLabel subtitle = new JLabel("Click a product card to add them to cart.");
        JLabel title2 = new JLabel("Current Cart:");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h2");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted h3");
        title2.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h1");

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

        rightPanel.add(rightPanelHeader, "growx");
        rightPanel.add(title2, "al center top");

        containerSplitPane.add(leftPanel);
        containerSplitPane.add(rightPanel);

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

        JPanel contentContainer = new JPanel(new MigLayout("insets 6, flowx, wrap", "[grow, fill]"));
        JLabel title = new JLabel("Cart", new SVGIconUIColor("shopping-cart.svg", 0.75f, "foreground.background"),
                JLabel.RIGHT);
        cartPanel = new JPanel(new MigLayout("insets 0, flowx, wrap, al center center", "[grow, fill]", ""));
        JScrollPane scroller = new JScrollPane(cartPanel);
        checkoutButton = new JButton("Checkout");
        cancelButton = new JButton("Cancel");
        cartButtonsContainer = new JPanel(new MigLayout("insets 0, fillx, gapx 9px"));

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");
        title.setHorizontalAlignment(JLabel.LEFT);

        rightPanel.putClientProperty(FlatClientProperties.STYLE, "background:tint($Panel.background, 25%);");

        scroller.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        scroller.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        cartButtonsContainer.putClientProperty(FlatClientProperties.STYLE, "background: null;");

        cancelButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted h3");
        checkoutButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h3");

        cartButtonsContainer.add(cancelButton);
        cartButtonsContainer.add(checkoutButton);

        contentContainer.add(title);
        contentContainer.add(scroller, "grow");

        buildRightPanelContent();

        container.add(contentContainer, BorderLayout.CENTER);

        rightPanel.add(container, "grow");

        checkoutButton.addActionListener(new CheckoutButtonActionListener(this));
        cancelButton.addActionListener(new CancelButtonActionListener());
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
            List<ItemCategoryDto> itemCatetgories = itemService.showAllCategories();
            List<InventoryMetadataDto> items = itemService.getAllItems();

            this.items.set((ArrayList<InventoryMetadataDto>) items);

            SwingUtilities.invokeLater(() -> {
                brandCheckBoxes.clear();
                categoryCheckBoxes.clear();

                itemBrands.forEach((brand) -> {
                    JCheckBoxMenuItem c = new JCheckBoxMenuItem(brand.name());

                    brandCheckBoxes.add(c);
                });

                itemCatetgories.forEach((category) -> {
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
            if (c instanceof JButton button) {
                Arrays.stream(button.getActionListeners()).forEach(button::removeActionListener);
            } else if (c instanceof JComponent co) {
                removeActionListeners(co);
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

    private void updateCartDisplay() {
        cartListPanel.removeAll();

        JPanel headerPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][right][center]", "[]"));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        headerPanel.setBackground(Color.WHITE);

        JLabel nameHeader = new JLabel("Product");
        JLabel priceHeader = new JLabel("Price");
        JLabel quantityHeader = new JLabel("Quantity");

        nameHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        priceHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        quantityHeader.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");

        headerPanel.add(nameHeader, "growx");
        headerPanel.add(priceHeader, "gapright 20");
        headerPanel.add(quantityHeader, "gapright 6");

        cartListPanel.add(headerPanel, "growx");

        for (CartItem item : cart.get().getAllItems()) {
            JPanel cartItemPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][right]", "[]"));
            cartItemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            cartItemPanel.setBackground(Color.white);

            JLabel productName = new JLabel(item.productName());
            JLabel productPrice = new JLabel("₱" + item.price());
            JLabel productQty = new JLabel("x" + item.qty());

            JPanel qtyPanel = new JPanel(new MigLayout("insets 0, wrap 3, al center center", "[]5[]5[]", "[]"));
            qtyPanel.setBackground(Color.white);
            JButton decBtn = new JButton("-");
            JButton addBtn = new JButton("+");

            decBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
            decBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "secondary h5");
            addBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
            addBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h5");

            addBtn.addActionListener(e -> {
                cart.get().addItem(new CartItem(item._itemStockId(), item.productName(), item.category(), item.brand(),
                        1, item.price()));
                cart.get().subscribe((v) -> updateCartDisplay());
            });

            decBtn.addActionListener(e -> {
                cart.get().decItem(new CartItem(item._itemStockId(), item.productName(), item.category(), item.brand(),
                        1, item.price()));
                cart.get().subscribe((v) -> updateCartDisplay());
            });

            qtyPanel.add(decBtn, "center, w 25!, h 25!");
            qtyPanel.add(productQty, "center");
            qtyPanel.add(addBtn, "center, w 25!, h 24!");

            cartItemPanel.add(productName, "growx");
            cartItemPanel.add(productPrice, "gapx 10");
            cartItemPanel.add(qtyPanel, "gapx 10");
            cartListPanel.add(cartItemPanel, "growx, wrap");
        }

        cartListPanel.revalidate();
        cartListPanel.repaint();
    }

    private class AddToCartModalActionCallback implements ModalCallback {
        private final InventoryMetadataDto item;
        private final JPanel owner;

        public AddToCartModalActionCallback(InventoryMetadataDto item, JPanel owner) {
            this.item = item;
            this.owner = owner;
        }

        @Override
        public void action(ModalController controller, int action) {
            switch (action) {
                case SimpleMessageModal.OK_CANCEL_OPTION : {
                    CartItem cartItem = new CartItem(item._itemStockId(), item.itemName(), item.categoryName(),
                            item.brandName(), 1, item.itemPricePhp());

                    cart.get().addItem(cartItem);
                    updateCartDisplay();

                    SimpleMessageModal success = new SimpleMessageModal(SimpleMessageModal.Type.SUCCESS,
                            item.itemName() + " has been added to your cart", "Successfully Added",
                            SimpleMessageModal.CLOSE_OPTION, null);

                    ModalDialog.showModal(owner, success);
                }
                    break;
            }
        }
    }

    private class CancelButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
                    controller.close();
                    cb.beforeClose(false);
                    break;
                case SimpleModalBorder.YES_OPTION :
                    controller.consume();
                    cb.beforeClose(true);
                    break;
                case SimpleModalBorder.NO_OPTION :
                    cart.getAcquire().clearCart();
                    removeActionListeners(cartPanel);
                    cartPanel.removeAll();
                    controller.consume();
                    cb.beforeClose(true);
                    break;
            }
        }
    }

    private class ItemPanelMouseListener extends MouseAdapter {
        private final InventoryMetadataDto item;
        private final JPanel owner;

        public ItemPanelMouseListener(JPanel owner, InventoryMetadataDto item) {
            this.owner = owner;
            this.item = item;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            ModalDialog.showModal(owner, new SimpleMessageModal(SimpleMessageModal.Type.INFO,
                    String.format("Are you sure you want to add %s to your cart?", item.itemName()), "Add to cart",
                    SimpleMessageModal.OK_CANCEL_OPTION, new AddToCartModalActionCallback(item, owner)));
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

            add(new JLabel("No items in cart yet :("), BorderLayout.CENTER);
        }
    }

    private class NoResultsPanel extends JPanel {
        public NoResultsPanel() {
            setLayout(new BorderLayout());

            putClientProperty(FlatClientProperties.STYLE, "background: null;");

            JLabel noResults = new JLabel("No results were found :(");

            add(noResults, BorderLayout.CENTER);
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
