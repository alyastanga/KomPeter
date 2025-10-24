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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
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
import com.github.ragudos.kompeter.utilities.HtmlUtils;

import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.ModalDialog;
import raven.modal.component.DropShadowBorder;
import raven.modal.component.SimpleModalBorder;

@SystemForm(name = "Point of Sale Shop", description = "The point of sale shop", tags = { "sales", "shop" })
public class FormPosShop extends Form {
    private ArrayList<JCheckBoxMenuItem> brandCheckBoxes;
    private AtomicReference<ArrayList<String>> brandFilters;
    private AtomicReference<Cart> cart;
    private ArrayList<JCheckBoxMenuItem> categoryCheckBoxes;
    private AtomicBoolean isFetching;

    private AtomicReference<ArrayList<String>> categoryFilters;
    private JSplitPane containerSplitPane;
    private JButton filterButtonTrigger;
    private JPopupMenu filterPopupMenu;
    private AtomicReference<ArrayList<InventoryMetadataDto>> items;

    private JPanel leftPanel;
    private JPanel leftPanelContentContainer;
    private JPanel leftPanelHeader;
    private JPanel rightPanel;
    private JTextField searchTextField;

    public FormPosShop() {
        init();
        formRefresh();
    }

    @Override
    public void formOpen() {
        repaint();
        revalidate();
    }

    @Override
    public void formRefresh() {
        new Thread(this::loadData, "Load Point of Sale Shop Data").start();
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

    private List<InventoryMetadataDto> filterItems() {
        return items.getAcquire();
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
                    new MigLayout("flowx, wrap, insets 3", "[grow, center, fill, 120]"));

            AvatarIcon itemIcon = new AvatarIcon(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "placeholder.svg"),
                    100, 100, 0f);
            JLabel itemName = new JLabel(HtmlUtils.wrapInHtml(String.format("<p align='center'>%s", item.itemName())));
            JLabel itemPrice = new JLabel(
                    String.format(HtmlUtils.wrapInHtml("<p align='center'>₱ %s"), item.itemPricePhp()));

            itemName.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
            itemPrice.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

            itemName.setHorizontalAlignment(JLabel.CENTER);
            itemPrice.setHorizontalAlignment(JLabel.CENTER);

            itemPanel.setName(
                    String.format("%s;%s;%s", item._itemId(), item._itemStockId(), item._stockLocationId()));

            itemIcon.setType(AvatarIcon.Type.MASK_SQUIRCLE);

            itemContentContainer.add(new JLabel(itemIcon), "grow");
            itemContentContainer.add(itemName, "growx, gapy 14px");
            itemContentContainer.add(itemPrice, "growx, gapy 6px");

            itemPanel.add(itemContentContainer);

            leftPanelContentContainer.add(itemPanel, BorderLayout.CENTER);

            itemPanel.addMouseListener(new ItemPanelMouseListener());
        }

        leftPanelContentContainer.repaint();
        leftPanelContentContainer.revalidate();
    }

    private void applyShadowBorder(JPanel panel) {
        if (panel != null) {
            panel.setBorder(new DropShadowBorder(new Insets(4, 8, 12, 8), 1, 25));
        }
    }

    private void createContainers() {
        containerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPanel = new JPanel(
                new MigLayout("flowy, gapy 18px, al center center", "[grow, fill]", "[top][top][grow,fill]"));
        rightPanel = new JPanel();
        leftPanelHeader = new JPanel(
                new MigLayout("flowx, insets 0 0 0 12", "[grow,fill]16px[]push[]"));
        leftPanelContentContainer = new JPanel(
                new ResponsiveLayout(JustifyContent.CENTER, new Dimension(-1, -1), 9, 9));
        JLabel title = new JLabel("Products");

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary h2");

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

                repaint();
                revalidate();
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

    private void search() {
    }

    private class ItemPanelMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {

        }
    }

    private class FilterButtonTriggerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            filterPopupMenu.show(filterButtonTrigger, 0, filterButtonTrigger.getHeight());
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

    private class LoadingPanel extends JPanel {
        public LoadingPanel() {
            setLayout(new BorderLayout());

            putClientProperty(FlatClientProperties.STYLE, "background: null;");

            JLabel loading = new JLabel("Loading...");

            loading.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");

            add(loading, BorderLayout.CENTER);
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
