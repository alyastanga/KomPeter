/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import java.awt.BorderLayout;
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

@SystemForm(name = "Point of Sale Shop", description = "The point of sale shop", tags = { "sales", "shop" })
public class FormPosShop extends Form {
    private ArrayList<JCheckBoxMenuItem> brandCheckBoxes;
    private AtomicReference<ArrayList<String>> brandFilters;
    private JButton cancelButton;
    private AtomicReference<Cart> cart;
    private JPanel cartPanel;
    // No need atomic reference since we'll always access these in EDT
    private ArrayList<JCheckBoxMenuItem> categoryCheckBoxes;
    private AtomicReference<ArrayList<String>> categoryFilters;
    private JPanel cartButtonsContainer;
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
                        SimpleModalBorder.YES_NO_CANCEL_OPTION, new ModalCallback() {
                            @Override
                            public void action(ModalController controller, int action) {
                                switch (action) {
                                    case SimpleModalBorder.CANCEL_OPTION:
                                        controller.close();
                                        cb.beforeClose(false);
                                        break;
                                    case SimpleModalBorder.YES_OPTION:
                                        controller.consume();
                                        cb.beforeClose(true);
                                        break;
                                    case SimpleModalBorder.NO_OPTION:
                                        cart.clearCart();
                                        removeActionListeners(cartPanel);
                                        cartPanel.removeAll();
                                        controller.consume();
                                        cb.beforeClose(true);
                                        break;
                                }

                            }
                        }));
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
            cartPanel.add(new NoItemsInCartPanel());

            rightPanel.repaint();
            rightPanel.revalidate();

            return;
        }

        for (CartItem item : cart.getAllItems()) {
            cartPanel.add(createCartItemCard(item), "growx");
        }

        rightPanel.add(cartButtonsContainer, "growx");
        rightPanel.repaint();
        rightPanel.revalidate();
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

    private void createContainers() {
        containerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPanel = new JPanel(
                new MigLayout("flowy, al center center", "[grow, fill]", "[top]16px[top]4px[top]8px[grow,fill]"));
        rightPanel = new JPanel(new MigLayout("gapy 14px,wrap", "[grow, fill]", "[grow, fill][]"));
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

    private void removeAllItemListenersOfPopupMenu(JPopupMenu menu) {
        for (Component component : menu.getComponents()) {
            if (component instanceof JMenuItem item) {
                for (ItemListener l : item.getItemListeners()) {
                    item.removeItemListener(l);
                }
            }
        }
    }

    private JPanel createCartItemCard(CartItem cartItem) {
        JPanel container = new JPanel(new MigLayout("flowx, gapx 9px, insets 6", "[150][grow, fill]", "fill"));

        container.setName(String.format("_itemStockId:%s", cartItem._itemStockId()));

        AvatarIcon itemIcon = new AvatarIcon(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "placeholder.svg"),
                76, 76, 1f);
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

    private void search() {
    }

    private class NoItemsInCartPanel extends JPanel {
        public NoItemsInCartPanel() {
            setLayout(new BorderLayout());

            add(new JLabel("No items in cart yet :("), BorderLayout.CENTER);
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

    private class ItemPanelMouseListener extends MouseAdapter {
        private InventoryMetadataDto item;

        public ItemPanelMouseListener(InventoryMetadataDto item) {
            this.item = item;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Cart c = cart.getAcquire();

            CartItem newItem = new CartItem(item._itemStockId(), item.itemName(), item.categoryName(), item.brandName(),
                    1,
                    item.itemPricePhp());

            c.addItem(newItem);

            buildRightPanelContent();
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
