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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.components.table.Currency;
import com.github.ragudos.kompeter.app.desktop.components.table.ItemStatusText;
import com.github.ragudos.kompeter.app.desktop.components.table.LabelWithImage;
import com.github.ragudos.kompeter.app.desktop.components.table.LabelWithImage.LabelWithImageData;
import com.github.ragudos.kompeter.app.desktop.components.table.PercentageBar;
import com.github.ragudos.kompeter.app.desktop.components.table.PercentageBar.PercentageBarData;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.inventory.Inventory;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.utilities.Debouncer;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

import net.miginfocom.swing.MigLayout;

@SystemForm(name = "Inventory Browse Products", description = "Shows all products", tags = { "inventory" })
public class FormInventoryBrowseProducts extends Form {
    private JPanel bodyPanel;

    private AtomicReference<ArrayList<String>> brandFilters;
    private AtomicReference<ArrayList<String>> categoryFilters;
    private Debouncer debouncer;
    private FilterPopupMenu filterPopupMenu;
    private JPanel headerPanel;
    private Inventory inventory;
    private AtomicBoolean isBusy;
    private ManageStockPopupMenu manageStockPopupMenu;
    private Inventory.InventoryProductListData productListData;
    private ProductsTable productsTable;
    private JScrollPane productsTableContainer;
    private ProductsTableFooter productsTableControlFooter;
    private JTextField searchTextField;

    @Override
    public boolean formBeforeClose() {
        return super.formBeforeClose();
    }

    @Override
    public boolean formBeforeLogout() {
        return super.formBeforeLogout();
    }

    @Override
    public void formInit() {
        init();
        formRefresh();
    }

    @Override
    public void formRefresh() {
        if (isBusy.get()) {
            return;
        }

        new Thread(this::loadData, "Load Inventory Products Data").start();
    }

    private void createBody() {
        productsTable = new ProductsTable();
        productsTableControlFooter = new ProductsTableFooter();
        productsTableContainer = new JScrollPane(productsTable);

        productsTableContainer.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        productsTableContainer.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        productsTableContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        productsTableContainer.getVerticalScrollBar().setUnitIncrement(16);
        productsTableContainer.getHorizontalScrollBar().setUnitIncrement(16);
        productsTableContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        productsTableContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        productsTableControlFooter.putClientProperty(FlatClientProperties.STYLE, "background:$color.gray;");

        bodyPanel.add(productsTableContainer, "grow");
        bodyPanel.add(productsTableControlFooter, "growx");
    }

    private void createContainers() {
        setLayout(new MigLayout("insets 2, flowx, wrap", "[grow, fill, center]", "[]12px[grow, top, fill]"));

        headerPanel = new JPanel(new MigLayout("insets 0, flowx", "[]8px[]push[]", "[]16[]4[]"));
        bodyPanel = new JPanel(
                new MigLayout("insets 0, flowx, wrap", "[grow, fill, center]", "[grow, fill, top]4px[]"));

        add(headerPanel, "grow");
        add(bodyPanel, "grow");
    }

    private void createHeader() {
        final JLabel title = new JLabel("Browse Products");
        final JLabel description = new JLabel("Browse all products");
        searchTextField = new JTextField();
        filterPopupMenu = new FilterPopupMenu();
        manageStockPopupMenu = new ManageStockPopupMenu();

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
        description.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new SVGIconUIColor("search.svg", 0.5f, "TextField.placeholderForeground"));
        searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search products...");

        searchTextField.getDocument().addDocumentListener(new SearchTextFieldDocumentListener());

        headerPanel.add(searchTextField, "width 100px:800px:");
        headerPanel.add(filterPopupMenu.filterButtonTrigger());
        headerPanel.add(manageStockPopupMenu.manageStockTrigger(), "gapleft 12px,wrap");
        headerPanel.add(title, "wrap");
        headerPanel.add(description, "wrap");
    }

    private void init() {
        isBusy = new AtomicBoolean(false);
        inventory = Inventory.getInstance();
        categoryFilters = new AtomicReference<>(new ArrayList<>());
        brandFilters = new AtomicReference<>(new ArrayList<>());
        debouncer = new Debouncer(250);

        createContainers();
        createBody();
        createHeader();
    }

    private void loadData() {
        isBusy.set(true);

        try {
            final String[] itemBrands = inventory.getAllItemBrands();
            final String[] itemCategories = inventory.getAllItemCategories();

            productListData = inventory.getProductList(20, searchTextField.getText(),
                    categoryFilters.getAcquire().toArray(String[]::new),
                    brandFilters.getAcquire().toArray(String[]::new));

            SwingUtilities.invokeLater(() -> {
                filterPopupMenu.populate(itemCategories, itemBrands);

                if (productListData.getTotalPages() == 0) {
                    productsTableContainer.setViewportView(new NoResultsPanel());
                    productsTableContainer.repaint();
                    productsTableContainer.revalidate();

                    productsTableControlFooter.setVisible(false);

                    return;
                }

                productsTableContainer.setViewportView(productsTable);
                productsTableControlFooter.setVisible(true);
                productsTable.populate();

                isBusy.set(false);
            });

        } catch (final InventoryException err) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                    "Failed to load data :(", JOptionPane.ERROR_MESSAGE);

            isBusy.set(false);
        }
    }

    private void removeAllListenersOfPopupMenu(final JPopupMenu menu) {
        for (final Component component : menu.getComponents()) {
            if (component instanceof final JMenuItem item) {
                for (final ItemListener l : item.getItemListeners()) {
                    item.removeItemListener(l);
                }

                for (final ActionListener l : item.getActionListeners()) {
                    item.removeActionListener(l);
                }
            }
        }
    }

    private void search() {
        debouncer.call(() -> {
            try {
                productListData = inventory.getProductList(20, searchTextField.getText(),
                        categoryFilters.getAcquire().toArray(String[]::new),
                        brandFilters.getAcquire().toArray(String[]::new));
            } catch (final InventoryException e) {
                e.printStackTrace();
            }

            if (productListData.getTotalPages() == 0) {
                productsTableContainer.setViewportView(new NoResultsPanel());
                productsTableContainer.repaint();
                productsTableContainer.revalidate();
                productsTableControlFooter.setVisible(false);

                return;
            }

            productsTableContainer.setViewportView(productsTable);
            productsTableControlFooter.setVisible(true);
            productsTable.populate();
        });
    }

    public class ProductsTable extends JTable {
        public static final int COL_NAME = 0;
        public static final int COL_PRICE = 1;
        public static final int COL_STATUS = 3;
        public static final int COL_STOCK_QTY = 2;

        public ProductsTable() {
            getTableHeader().setBackground(Color.decode("#c2bdb9"));
            getTableHeader().putClientProperty(FlatClientProperties.STYLE, "font:14;");
            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer())
                    .setHorizontalAlignment(SwingConstants.CENTER);

            final ProductsTableModel tableModel = new ProductsTableModel();

            tableModel.setColumnIdentifiers(new String[] { "Name", "Price", "Current Stock", "Status" });

            setModel(tableModel);

            final TableColumnModel columnModel = getColumnModel();

            columnModel.getColumn(COL_NAME).setCellRenderer(new LabelWithImage());
            columnModel.getColumn(COL_PRICE).setCellRenderer(new Currency());
            columnModel.getColumn(COL_STOCK_QTY).setCellRenderer(new PercentageBar());
            columnModel.getColumn(COL_STATUS).setCellRenderer(new ItemStatusText());

            columnModel.getColumn(COL_NAME).setPreferredWidth(250);
            columnModel.getColumn(COL_PRICE).setMaxWidth(150);
            columnModel.getColumn(COL_PRICE).setMinWidth(100);
            columnModel.getColumn(COL_STOCK_QTY).setMaxWidth(200);
            columnModel.getColumn(COL_STOCK_QTY).setMinWidth(150);
            columnModel.getColumn(COL_STATUS).setMinWidth(112);
            columnModel.getColumn(COL_STATUS).setMaxWidth(96);

            setRowSorter(new ProductsTableRowSorter(tableModel));

            setShowGrid(true);
            setRowHeight(32);
            putClientProperty(FlatClientProperties.STYLE, "font:12;");
        }

        public void addStockToSelectedItem() {
        }

        public void deleteSelectedItems() {
        }

        public void populate() {
            final ProductsTableModel model = (ProductsTableModel) getModel();

            model.setRowCount(0);

            for (final InventoryMetadataDto item : productListData.getItemsAtCurrentPage()) {
                model.addRow(new Object[] { new LabelWithImage.LabelWithImageData(item.displayImage(), item.itemName()),
                        item.unitPricePhp(), new PercentageBar.PercentageBarData(item._itemStockId(),
                                item.totalQuantity(), item.minimumQuantity(), "unit/s"),
                        item.status(), "" });
            }

            productsTableControlFooter.rerender();

            bodyPanel.revalidate();
        }

        private class ProductsTableModel extends DefaultTableModel {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(final int columnIndex) {
                // class types for comparison
                return switch (columnIndex) {
                    case COL_NAME -> String.class;
                    case COL_PRICE -> BigDecimal.class;
                    case COL_STOCK_QTY -> Integer.class;
                    case COL_STATUS -> Integer.class;
                    default -> super.getColumnClass(columnIndex);
                };
            }
        }

        private class ProductsTableRowSorter extends TableRowSorter<ProductsTableModel> {
            public ProductsTableRowSorter(final ProductsTableModel model) {
                super(model);
            }

            @Override
            public Comparator<?> getComparator(final int column) {
                return switch (column) {
                    case COL_NAME -> Comparator.comparing(LabelWithImageData::label);
                    case COL_PRICE -> new Comparator<BigDecimal>() {
                        public int compare(final BigDecimal arg0, final BigDecimal arg1) {
                            return arg0.compareTo(arg1);
                        };
                    };
                    case COL_STOCK_QTY -> Comparator.comparing(PercentageBarData::currentValue);
                    case COL_STATUS -> Comparator.comparing(ItemStatus::priority);
                    default -> super.getComparator(column);
                };
            }
        }
    }

    private class FilterPopupMenu extends JPopupMenu implements ItemListener, ActionListener {
        private final JButton filterButtonTrigger;

        public FilterPopupMenu() {
            filterButtonTrigger = new JButton(new SVGIconUIColor("filter.svg", 0.5f, "foreground.muted"));

            filterButtonTrigger.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            filterButtonTrigger.addActionListener(this);
            filterButtonTrigger.setToolTipText("Filter Products");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            show(filterButtonTrigger, 0, filterButtonTrigger.getHeight());
        }

        public JButton filterButtonTrigger() {
            return filterButtonTrigger;
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
        }

        public void populate(final String[] itemCategories, final String[] itemBrands) {
            removeAllListenersOfPopupMenu(this);
            filterPopupMenu.removeAll();

            final JLabel categoryLabel = new JLabel("Category");
            final JLabel brandLabel = new JLabel("Brand");

            filterPopupMenu.add(categoryLabel);

            for (final String category : itemCategories) {
                final JCheckBoxMenuItem c = new JCheckBoxMenuItem(category);

                if (categoryFilters.getAcquire().contains(category)) {
                    c.setSelected(true);
                }

                c.setName("category");
                c.addItemListener(filterPopupMenu);
                filterPopupMenu.add(c);
            }

            filterPopupMenu.addSeparator();

            filterPopupMenu.add(brandLabel);

            for (final String brand : itemBrands) {
                final JCheckBoxMenuItem c = new JCheckBoxMenuItem(brand);

                if (brandFilters.getAcquire().contains(brand)) {
                    c.setSelected(true);
                }

                c.setName("brand");
                c.addItemListener(filterPopupMenu);
                filterPopupMenu.add(c);
            }
        }
    }

    private class ManageStockPopupMenu extends JPopupMenu implements ActionListener, PopupMenuListener {
        private final JButton addStockButton;
        private final SVGIconUIColor chevronDown;
        private final SVGIconUIColor chevronUp;
        private final JButton deleteButton;
        private final JButton manageStockTrigger;

        public ManageStockPopupMenu() {
            chevronDown = new SVGIconUIColor("chevron-down.svg", 0.75f, "foreground.background");
            chevronUp = new SVGIconUIColor("chevron-up.svg", 0.75f, "foreground.background");
            manageStockTrigger = new JButton("Manage Stock", chevronDown);
            deleteButton = new JButton("Delete", new SVGIconUIColor("trash.svg", 0.5f, "foreground.background"));
            addStockButton = new JButton("Add Stock",
                    new SVGIconUIColor("add-stock.svg", 0.5f, "foreground.background"));

            setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, center, fill]"));

            manageStockTrigger.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            deleteButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            addStockButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            manageStockTrigger.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            deleteButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            addStockButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");

            manageStockTrigger.setToolTipText("Manage Stock");
            deleteButton.setToolTipText("Delete selected item/s");
            addStockButton.setToolTipText("Add stock to selected item");

            manageStockTrigger.setActionCommand("trigger");
            deleteButton.setActionCommand("delete");
            addStockButton.setActionCommand("add_stock");

            manageStockTrigger.addActionListener(this);
            deleteButton.addActionListener(this);
            addStockButton.addActionListener(this);

            add(deleteButton, "growx");
            add(addStockButton, "growx");
            addPopupMenuListener(this);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("trigger")) {
                show(manageStockTrigger, 0, manageStockTrigger.getHeight());
            } else if (e.getActionCommand().equals("delete")) {
                productsTable.deleteSelectedItems();
            } else if (e.getActionCommand().equals("add_stock")) {
                productsTable.addStockToSelectedItem();
            }
        }

        public JButton manageStockTrigger() {
            return manageStockTrigger;
        }

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
            manageStockTrigger.setIcon(chevronDown);
        }

        @Override
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            manageStockTrigger.setIcon(chevronDown);
        }

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            manageStockTrigger.setIcon(chevronUp);
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

    private class ProductsTableFooter extends JPanel implements ActionListener, ChangeListener {
        private ButtonGroup buttonGroup;
        private final JLabel leftLabel;
        private JButton[] paginationButtons;
        private final JPanel paginationContainer;
        private final JSpinner rowsPerPageSpinner;

        public ProductsTableFooter() {
            setLayout(new MigLayout("insets 0 5 0 5, flowx, al center center", "[]push[center]push[][]"));

            leftLabel = new JLabel();
            paginationContainer = new JPanel(new MigLayout("insets 0, flowx, gap 1px"));
            rowsPerPageSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
            final JLabel rowsLabel = new JLabel("Rows per page");

            paginationContainer.putClientProperty(FlatClientProperties.STYLE, "background:null;");

            leftLabel.putClientProperty(FlatClientProperties.STYLE,
                    "foreground:fade($TextField.placeholderForeground, 87%);font:10;");
            rowsLabel.putClientProperty(FlatClientProperties.STYLE,
                    "foreground:fade($TextField.placeholderForeground, 87%);font:10;");
            rowsPerPageSpinner.putClientProperty(FlatClientProperties.STYLE, "arc:0;");

            rowsPerPageSpinner.addChangeListener(this);

            add(leftLabel);
            add(paginationContainer);
            add(rowsLabel);
            add(rowsPerPageSpinner);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!(e.getSource() instanceof JButton))
                return;
            final JButton clicked = (JButton) e.getSource();

            try {
                final int selectedPage = Integer.parseInt(clicked.getText());
                productListData.setCurrentPage(selectedPage); // update page in your data

                SwingUtilities.invokeLater(() -> {
                    productsTable.populate();
                });
            } catch (final NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        public void rerender() {
            rowsPerPageSpinner.setModel(new SpinnerNumberModel(productListData.getRowsPerPage(), 1,
                    productListData.getTotalPages() * productListData.getRowsPerPage(), 1));
            final int currentPage = productListData.getCurrentPage();
            final int rowsPerPage = productListData.getRowsPerPage();
            final int totalItems = productListData.getTotalPages() * rowsPerPage;

            // Adjust displayed range
            final int startItem = (currentPage - 1) * rowsPerPage + 1;
            final int endItem = Math.min(currentPage * rowsPerPage, totalItems);

            leftLabel.setText(String.format("Displaying product %d-%d of %d in total", startItem, endItem, totalItems));

            if (productListData.getTotalPages() == 1) {
                paginationContainer.removeAll();
                paginationContainer.revalidate();
                paginationContainer.repaint();
                return;
            }

            // Clear old pagination
            paginationContainer.removeAll();
            if (paginationButtons != null) {
                for (final JButton b : paginationButtons) {
                    if (b != null)
                        b.removeActionListener(this);
                }
            }

            buttonGroup = new ButtonGroup();

            // Calculate pages to display
            final int totalPages = productListData.getTotalPages();
            int[] pages;

            if (totalPages <= 5) {
                pages = new int[totalPages];
                for (int i = 0; i < totalPages; i++)
                    pages[i] = i + 1;
            } else {
                pages = new int[5];
                int start = Math.max(1, currentPage - 2);
                final int end = Math.min(totalPages, start + 4);
                start = Math.max(1, end - 4);

                for (int i = 0; i < 5; i++)
                    pages[i] = start + i;
            }

            paginationButtons = new JButton[pages.length];

            for (int i = 0; i < pages.length; i++) {
                final int page = pages[i];
                final JButton b = new JButton(String.valueOf(page));
                b.addActionListener(this);
                b.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
                b.putClientProperty(FlatClientProperties.STYLE, "arc:0;");
                b.setMaximumSize(new Dimension(42, 42));

                if (page == currentPage) {
                    b.setSelected(true);
                }

                buttonGroup.add(b);
                paginationContainer.add(b);
                paginationButtons[i] = b;
            }

            paginationContainer.revalidate();
            paginationContainer.repaint();
        }

        @Override
        public void stateChanged(final ChangeEvent e) {
            if (e.getSource() == rowsPerPageSpinner) {
                final int newRows = (int) rowsPerPageSpinner.getValue();
                productListData.setRowsPerPage(newRows);

                SwingUtilities.invokeLater(() -> {
                    productsTable.populate();
                });
            }
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
