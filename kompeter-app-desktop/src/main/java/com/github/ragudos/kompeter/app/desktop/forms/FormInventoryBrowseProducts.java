/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
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
import com.github.ragudos.kompeter.app.desktop.components.menu.FilterPopupMenu.CategoryBrandFilterPopupMenu;
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

    private Debouncer debouncer;
    private CategoryBrandFilterPopupMenu filterPopupMenu;
    private JPanel headerPanel;
    private Inventory inventory;
    private AtomicBoolean isBusy;
    private ManageStockPopupMenu manageStockPopupMenu;
    private Inventory.InventoryProductListData productListData;
    private ProductsTable productsTable;
    private JScrollPane productsTableContainer;
    private ProductsTableFooter productsTableControlFooter;
    private JTextField searchTextField;
    private ItemStatus itemStatusFilter;
    private StatusFilterPopupMenu statusFilterPopupMenu;

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

        headerPanel = new JPanel(new MigLayout("insets 0, flowx", "[grow 50]8px[]push[]", "[]16[]4[]"));
        bodyPanel = new JPanel(
                new MigLayout("insets 0, flowx, wrap", "[grow, fill, center]", "[grow, fill, top]4px[]"));

        add(headerPanel, "grow");
        add(bodyPanel, "grow");
    }

    private void createHeader() {
        final JLabel title = new JLabel("Browse Products");
        final JLabel description = new JLabel(HtmlUtils
                .escapeHtml(
                        "Browse all products (Highlight/Right-click products to select them for editing and deletion.)"));
        searchTextField = new JTextField();
        filterPopupMenu = new CategoryBrandFilterPopupMenu(this::search);
        manageStockPopupMenu = new ManageStockPopupMenu();
        statusFilterPopupMenu = new StatusFilterPopupMenu();

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
        description.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new SVGIconUIColor("search.svg", 0.5f, "TextField.placeholderForeground"));
        searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search products...");

        searchTextField.getDocument().addDocumentListener(new SearchTextFieldDocumentListener());

        headerPanel.add(searchTextField, "grow 50");
        headerPanel.add(filterPopupMenu.trigger());
        headerPanel.add(statusFilterPopupMenu.statusFilterTrigger(), "gapleft 32px, wrap");
        headerPanel.add(title, "wrap");
        headerPanel.add(description, "wrap");
    }

    private void init() {
        isBusy = new AtomicBoolean(false);
        inventory = Inventory.getInstance();
        debouncer = new Debouncer(250);

        createContainers();
        createBody();
        createHeader();
    }

    private void loadData() {
        isBusy.set(true);

        try {
            productListData = inventory.getProductList(
                    (productListData != null && productListData.getRowsPerPage() != 0)
                            ? productListData.getRowsPerPage()
                            : 20,
                    searchTextField.getText(),
                    filterPopupMenu.categoryFilters.getAcquire().toArray(String[]::new),
                    filterPopupMenu.brandFilters.getAcquire().toArray(String[]::new), itemStatusFilter);

            SwingUtilities.invokeLater(() -> {
                filterPopupMenu.populate();

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

    private void search() {
        debouncer.call(() -> {
            try {
                productListData = inventory.getProductList(
                        ((productListData != null && productListData.getTotalPages() != 0)
                                ? productListData.getRowsPerPage()
                                : 20),
                        searchTextField.getText(),
                        filterPopupMenu.categoryFilters.getAcquire().toArray(String[]::new),
                        filterPopupMenu.brandFilters.getAcquire().toArray(String[]::new), itemStatusFilter);
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

        private final ProductsTableMouseAdapter mouseListener;

        public ItemStatus getSelectedRowItemStatus() {
            final int row = convertRowIndexToModel(getSelectedRow());
            final int col = convertColumnIndexToModel(COL_STATUS);

            return (ItemStatus) getValueAt(row, col);
        }

        public boolean allSelectedRowsAreOfStatus(final ItemStatus status) {
            final int[] selectedRows = getSelectedRows();
            final int col = convertColumnIndexToModel(COL_STATUS);

            if (selectedRows.length == 1) {
                final int realRow = convertRowIndexToModel(selectedRows[0]);

                return ((ItemStatus) getValueAt(realRow, col)) == status;
            }

            for (int i = 0; i < selectedRows.length; ++i) {
                final int realRow = convertRowIndexToModel(selectedRows[i]);
                final ItemStatus st = (ItemStatus) getValueAt(realRow, col);

                if (st != status) {
                    return false;
                }
            }

            return true;
        }

        public ProductsTable self() {
            return this;
        }

        public ProductsTable() {
            mouseListener = new ProductsTableMouseAdapter();

            addMouseListener(mouseListener);

            getTableHeader().putClientProperty(FlatClientProperties.STYLE, "font:14 semibold;");
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

            columnModel.getColumn(COL_NAME).setPreferredWidth(800);
            columnModel.getColumn(COL_PRICE).setPreferredWidth(96);
            columnModel.getColumn(COL_STOCK_QTY).setPreferredWidth(142);
            columnModel.getColumn(COL_STATUS).setPreferredWidth(82);

            setRowSorter(new ProductsTableRowSorter(tableModel));

            setShowGrid(true);
            setRowHeight(38);
            putClientProperty(FlatClientProperties.STYLE, "font:12;");
        }

        public int[] getModelSelectedRows() {
            final int[] selectedRows = getSelectedRows();
            final int[] modelSelectedRows = new int[selectedRows.length];

            for (int i = 0; i < selectedRows.length; ++i) {
                modelSelectedRows[i] = convertRowIndexToModel(selectedRows[i]);
            }

            return modelSelectedRows;
        }

        public String[] getNamesOfSelectedItems() {
            final int[] selectedRows = getModelSelectedRows();
            final int col = convertColumnIndexToModel(COL_NAME);
            final String[] names = new String[selectedRows.length];

            for (int i = 0; i < selectedRows.length; ++i) {
                final LabelWithImageData data = (LabelWithImageData) getModel().getValueAt(selectedRows[i], col);

                names[i] = data.label();
            }

            return names;
        }

        public void addStockToSelectedItem() {
            new AddStockDialog(SwingUtilities.getWindowAncestor(bodyPanel)).setVisible(true);
        }

        public void deleteSelectedItems() {
            new DeleteSelectedItemsDialog(SwingUtilities.getWindowAncestor(bodyPanel)).setVisible(true);
        }

        public void changeStatusOfSelectedItems(final ItemStatus status) {
            try {
                inventory.setStatusOfItemsByName(getNamesOfSelectedItems(), status);

                productListData = inventory.getProductList(
                        (productListData != null && productListData.getRowsPerPage() != 0)
                                ? productListData.getRowsPerPage()
                                : 20,
                        searchTextField.getText(),
                        filterPopupMenu.categoryFilters.getAcquire().toArray(String[]::new),
                        filterPopupMenu.brandFilters.getAcquire().toArray(String[]::new), itemStatusFilter);

                if (productListData.getTotalPages() == 0) {
                    productsTableContainer.setViewportView(new NoResultsPanel());
                    productsTableContainer.repaint();
                    productsTableContainer.revalidate();

                    productsTableControlFooter.setVisible(false);

                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    self().populate();
                });
            } catch (final InventoryException err) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), err.getMessage(),
                            "Failed to change item/s status to: " + status.toString(),
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }

        private class AddStockDialog extends JDialog implements ActionListener {
            public AddStockDialog(final Window owner) {
                super(owner, "Add stock", Dialog.ModalityType.APPLICATION_MODAL);

            }

            @Override
            public void actionPerformed(final ActionEvent e) {

            }
        }

        private class DeleteSelectedItemsDialog extends JDialog implements ActionListener {
            private final Window owner;

            public DeleteSelectedItemsDialog(final Window owner) {
                super(owner, "Delete selected item/s",
                        Dialog.ModalityType.APPLICATION_MODAL);

                this.owner = owner;

                setLayout(new MigLayout("insets 16, flowx", "[grow, center, fill]"));

                final String[] namesToBeDeleted = getNamesOfSelectedItems();
                final JLabel title = new JLabel(String.format("Deleting %s items", namesToBeDeleted.length));
                final JLabel subtitle = new JLabel(HtmlUtils.wrapInHtml(
                        String.format("This will archive %s items. Are you sure?", namesToBeDeleted.length)));
                final String html = String.format("""
                        <html>
                        <head>
                        <style>
                        ul {
                            margin-left: 8px;
                            list-style-position: inside;
                        }
                        </style>
                        </head>
                        <body>
                            <ul>%s</ul>
                        </body>
                        </html>
                        """, Arrays.stream(namesToBeDeleted).map((n) -> String.format("<li>%s</li>", n))
                        .collect(Collectors.joining("\n")));
                final JTextPane textPane = new JTextPane();
                textPane.setContentType("text/html");
                textPane.setText(html);
                textPane.setEditable(false);
                textPane.setOpaque(false);

                final JButton confirmButton = new JButton("Yes, I'm sure",
                        new SVGIconUIColor("trash.svg", 0.75f, "foreground.error"));
                final JButton cancelButton = new JButton("No, Cancel",
                        new SVGIconUIColor("x.svg", 0.75f, "foreground.muted"));

                confirmButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
                cancelButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

                confirmButton.setActionCommand("confirm");
                cancelButton.setActionCommand("cancel");

                confirmButton.setToolTipText("Delete selected items");
                cancelButton.setToolTipText("Cancel deletion");

                final JScrollPane scroller = new JScrollPane(textPane);

                scroller.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                        "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
                scroller.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                        "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
                scroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                scroller.getVerticalScrollBar().setUnitIncrement(16);
                scroller.getHorizontalScrollBar().setUnitIncrement(16);
                scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3 primary");
                subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
                subtitle.putClientProperty(FlatClientProperties.STYLE, "font:11;");

                add(title, "growx, wrap");
                add(subtitle, "growx, wrap");
                add(scroller, "growx, gapy 8px, wrap");
                add(cancelButton, "gapy 20px, split 2");
                add(confirmButton, "gapx 4px");

                pack();
                setLocationRelativeTo(owner);

                confirmButton.addActionListener(this);
                cancelButton.addActionListener(this);
            }

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JButton button = (JButton) e.getSource();

                if (button.getActionCommand().equals("confirm")) {
                    confirmDelete();
                } else if (button.getActionCommand().equals("cancel")) {
                    dispose();
                }
            }

            private void confirmDelete() {
                productsTable.changeStatusOfSelectedItems(ItemStatus.ARCHIVED);
            }
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

        private class ProductsTableMouseAdapter extends MouseAdapter {
            @Override
            public void mouseReleased(final MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    final int row = rowAtPoint(e.getPoint());
                    if (row >= 0 && !isRowSelected(row)) {
                        getSelectionModel().setSelectionInterval(row, row);
                    }

                    manageStockPopupMenu.rebuildMenu();
                    manageStockPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    private class StatusFilterPopupMenu extends JPopupMenu implements ItemListener, ActionListener, PopupMenuListener {
        private final SVGIconUIColor chevronDown;
        private final SVGIconUIColor chevronUp;
        private final JButton statusFilterTrigger;
        private final ButtonGroup buttonGroup;

        public StatusFilterPopupMenu() {
            setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, center, fill]"));

            chevronDown = new SVGIconUIColor("chevron-down.svg", 0.75f, "foreground.background");
            chevronUp = new SVGIconUIColor("chevron-up.svg", 0.75f, "foreground.background");
            buttonGroup = new ButtonGroup();

            statusFilterTrigger = new JButton(chevronDown);

            statusFilterTrigger.setText("Showing: all");
            statusFilterTrigger.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            statusFilterTrigger.putClientProperty(FlatClientProperties.STYLE, "font:11;");

            final JCheckBoxMenuItem allButton = new JCheckBoxMenuItem("ALL");

            allButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            allButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            allButton.setToolTipText("Filter table to show items: ALL");
            allButton.setActionCommand("all");
            allButton.setSelected(true);
            allButton.addItemListener(this);

            buttonGroup.add(allButton);

            add(allButton, "growx");

            for (final ItemStatus status : ItemStatus.values()) {
                final JCheckBoxMenuItem statusButton = new JCheckBoxMenuItem(status.toString());

                statusButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
                statusButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
                statusButton.setToolTipText(String.format("Filter table to show all: %s", status));
                statusButton.setActionCommand(status.toString());
                statusButton.addItemListener(this);

                buttonGroup.add(statusButton);

                add(statusButton, "growx");
            }

            statusFilterTrigger.addActionListener(this);
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            final JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getItemSelectable();

            if (menuItem.getActionCommand().equals("all")) {
                itemStatusFilter = null;
            } else if (menuItem.getActionCommand().equals(ItemStatus.ARCHIVED.toString())) {
                itemStatusFilter = ItemStatus.ARCHIVED;
            } else if (menuItem.getActionCommand().equals(ItemStatus.ACTIVE.toString())) {
                itemStatusFilter = ItemStatus.ACTIVE;
            } else if (menuItem.getActionCommand().equals(ItemStatus.INACTIVE.toString())) {
                itemStatusFilter = ItemStatus.INACTIVE;
            }

            search();

            SwingUtilities.invokeLater(() -> {
                statusFilterTrigger
                        .setText(String.format("Showing: %s", buttonGroup.getSelection().getActionCommand()));
            });

        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            show(statusFilterTrigger, 0, statusFilterTrigger.getHeight());
        }

        public JButton statusFilterTrigger() {
            return statusFilterTrigger;
        }

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
            statusFilterTrigger.setIcon(chevronDown);
        }

        @Override
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            statusFilterTrigger.setIcon(chevronDown);
        }

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            statusFilterTrigger.setIcon(chevronUp);
        }
    }

    private class ManageStockPopupMenu extends JPopupMenu implements ActionListener {
        private JButton addStockButton;
        private JButton deleteButton;
        private JButton markActiveButton;
        private JButton markInactiveButton;
        private JButton unDeleteButton;

        public ManageStockPopupMenu() {
            setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, center, fill]"));
            rebuildMenu();
        }

        private void createAddStockButton() {
            addStockButton = new JButton("Add Stocks",
                    new SVGIconUIColor("add-stock.svg", 0.5f, "foreground.background"));

            addStockButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            addStockButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            addStockButton.setToolTipText("Add stock to selected item");
            addStockButton.setActionCommand("add_stock");
            addStockButton.addActionListener(this);
        }

        private void createUnDeleteButton() {
            unDeleteButton = new JButton("Restore",
                    new SVGIconUIColor("archive-restore.svg", 0.5f, "foreground.background"));

            unDeleteButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            unDeleteButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            unDeleteButton.setToolTipText("Restore selected item/s");
            unDeleteButton.setActionCommand("undelete");
            unDeleteButton.addActionListener(this);
        }

        private void createDeleteButton() {
            deleteButton = new JButton("Delete Items",
                    new SVGIconUIColor("archive.svg", 0.5f, "foreground.background"));

            deleteButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            deleteButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            deleteButton.setToolTipText("Delete selected item/s");
            deleteButton.setActionCommand("delete");
            deleteButton.addActionListener(this);
        }

        private void createMarkActiveButton() {
            markActiveButton = new JButton("Mark as Active",
                    new SVGIconUIColor("circle-check.svg", 0.5f, "foreground.background"));

            markActiveButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            markActiveButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            markActiveButton.setToolTipText("Mark selected item/s as active");
            markActiveButton.setActionCommand("mark_active");
            markActiveButton.addActionListener(this);
        }

        private void createMarkInactiveButton() {
            markInactiveButton = new JButton("Mark as Inactive",
                    new SVGIconUIColor("circle-x.svg", 0.5f, "foreground.background"));

            markInactiveButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            markInactiveButton.putClientProperty(FlatClientProperties.STYLE, "font:11;");
            markInactiveButton.setToolTipText("Mark selected item/s as inactive");
            markInactiveButton.setActionCommand("mark_inactive");
            markInactiveButton.addActionListener(this);
        }

        private void toggleMenuForAllUnArchived() {
            removeAll();

            if (deleteButton == null) {
                createDeleteButton();
            }

            add(deleteButton, "growx", 0);

            if (productsTable.getSelectedRowCount() == 1) {
                if (productsTable.getSelectedRowItemStatus() != ItemStatus.INACTIVE) {
                    if (addStockButton == null) {
                        createAddStockButton();
                    }

                    add(addStockButton, "growx", 1);
                }
            }

            if (productsTable.allSelectedRowsAreOfStatus(ItemStatus.ACTIVE)) {
                if (markInactiveButton == null) {
                    createMarkInactiveButton();
                }

                add(markInactiveButton, "growx");
            } else if (productsTable.allSelectedRowsAreOfStatus(ItemStatus.INACTIVE)) {
                if (markActiveButton == null) {
                    createMarkActiveButton();
                }

                add(markActiveButton, "growx");
            }
        }

        private void toggleMenuForAllActive() {
            if (deleteButton == null) {
                createDeleteButton();
            }

            add(deleteButton, "growx", 0);

            if (productsTable.getSelectedRowCount() == 1) {
                if (productsTable.getSelectedRowItemStatus() == ItemStatus.INACTIVE) {
                    return;
                }

                if (addStockButton == null) {
                    createAddStockButton();
                }

                add(addStockButton, "growx", 1);
            }

            if (markInactiveButton == null) {
                createMarkInactiveButton();
            }

            add(markInactiveButton, "growx");
        }

        private void toggleMenuForAllInactive() {
            if (deleteButton == null) {
                createDeleteButton();
            }

            add(deleteButton, "growx", 0);

            if (markActiveButton == null) {
                createMarkActiveButton();
            }

            add(markActiveButton, "growx");
        }

        public void toggleArchiveMenu() {
            removeAll();

            if (unDeleteButton == null) {
                createUnDeleteButton();
            }

            add(unDeleteButton, "growx", 0);
        }

        public void rebuildMenu() {
            if (itemStatusFilter == null) {
                toggleMenuForAllUnArchived();
            } else if (itemStatusFilter == ItemStatus.ARCHIVED) {
                toggleArchiveMenu();
            } else if (itemStatusFilter == ItemStatus.ACTIVE) {
                toggleMenuForAllActive();
            } else if (itemStatusFilter == ItemStatus.INACTIVE) {
                toggleMenuForAllInactive();
            }
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                setVisible(false);
            });

            if (e.getActionCommand().equals("delete")) {
                productsTable.deleteSelectedItems();
            } else if (e.getActionCommand().equals("add_stock")) {
                productsTable.addStockToSelectedItem();
            } else if (e.getActionCommand().equals("mark_inactive")) {
                productsTable.changeStatusOfSelectedItems(ItemStatus.INACTIVE);
            } else if (e.getActionCommand().equals("mark_active")) {
                productsTable.changeStatusOfSelectedItems(ItemStatus.ACTIVE);
            } else if (e.getActionCommand().equals("undelete")) {
                productsTable.changeStatusOfSelectedItems(ItemStatus.INACTIVE);
            }
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
                    "foreground:$TextField.placeholderForeground;font:10;");
            rowsLabel.putClientProperty(FlatClientProperties.STYLE,
                    "foreground:$TextField.placeholderForeground;font:10;");
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
                    if (b != null) {
                        b.removeActionListener(this);
                    }
                }
            }

            buttonGroup = new ButtonGroup();

            // Calculate pages to display
            final int totalPages = productListData.getTotalPages();
            int[] pages;
            final int MAX_PAGINATION_BUTTONS = 5;

            if (totalPages <= MAX_PAGINATION_BUTTONS) {
                pages = new int[totalPages];

                for (int i = 0; i < totalPages; i++) {
                    pages[i] = i + 1;
                }
            } else {
                pages = new int[MAX_PAGINATION_BUTTONS];
                int start = Math.max(1, currentPage - 2);
                final int end = Math.min(totalPages, start + 4);
                start = Math.max(1, end - 4);

                for (int i = 0; i < MAX_PAGINATION_BUTTONS; i++) {
                    pages[i] = start + i;
                }
            }

            paginationButtons = new JButton[pages.length];

            for (int i = 0; i < pages.length; i++) {
                final int page = pages[i];
                final JButton b = new JButton(String.valueOf(page));
                b.addActionListener(this);
                b.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
                b.putClientProperty(FlatClientProperties.STYLE, "arc:0;font:12;");
                b.setMaximumSize(new Dimension(36, 36));

                if (page == currentPage) {
                    b.setSelected(true);
                }

                buttonGroup.add(b);
                paginationContainer.add(b);
                paginationButtons[i] = b;
            }

            paginationContainer.revalidate();
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
