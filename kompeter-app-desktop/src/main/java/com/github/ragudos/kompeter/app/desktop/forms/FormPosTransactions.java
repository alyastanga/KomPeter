/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import java.math.BigDecimal;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.app.desktop.components.scroller.ScrollerFactory;
import com.github.ragudos.kompeter.app.desktop.components.table.Currency;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.sales.SaleMetadataDto;
import com.github.ragudos.kompeter.database.dto.sales.SaleMetadataDto.SaleItemStocks;
import com.github.ragudos.kompeter.database.dto.sales.SaleMetadataDto.SaleMetadataPayments;
import com.github.ragudos.kompeter.pointofsale.Transaction;

import net.miginfocom.swing.MigLayout;

@SystemForm(name = "Point of Sale Transactions", description = "Shows the list of transactions or all products that have been sold.", tags = {
        "sales", "transactions" })
public class FormPosTransactions extends Form {
    TransactionsTable table = new TransactionsTable();

    @Override
    public void formInit() {
        init();
    }

    @Override
    public void formOpen() {
        new Thread(() -> {
            loadData();
        }, "load transactions data").start();
    }

    @Override
    public void formRefresh() {
        formOpen();
    }

    private void init() {
        setLayout(new MigLayout("insets 0, flowx, wrap", "[grow, fill, left]", "[top][top][top,grow]"));

        final JLabel title = new JLabel("Point of Sale Transactions");
        final JLabel subtitle = new JLabel("A history of all sales made");

        final JScrollPane scroller = ScrollerFactory.createScrollPane(table);

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

        add(title, "wrap");
        add(subtitle, "wrap");
        add(scroller, "gapy 8px, grow");
    }

    private void loadData() {
        try {
            final SaleMetadataDto[] sales = Transaction.getAllTransactions();

            SwingUtilities.invokeLater(() -> {
                table.populate(sales);
            });
        } catch (final Exception err) {
            JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(), err.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private class TransactionsTable extends JTable {

        public static final int COL_CUSTOMER_NAME = 2;
        public static final int COL_DISCOUNT = 7;
        public static final int COL_ID = 0;
        public static final int COL_CODE = 1;
        public static final int COL_PAYMENT = 3;
        public static final int COL_RAW_PRICE = 4;
        public static final int COL_REAL_PRICE = 5;
        public static final int COL_VAT_PRICE = 6;

        public TransactionsTable() {
            getTableHeader().putClientProperty(FlatClientProperties.STYLE, "font:+2 semibold;");
            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer())
                    .setHorizontalAlignment(SwingConstants.CENTER);

            final TransactionsModel tableModel = new TransactionsModel();

            tableModel.setColumnIdentifiers(
                    new String[] { "Id", "Code", "Customer Name", "Amount Paid", "Net Revenue", "Revenue", "Vat",
                            "Discount" });

            setModel(tableModel);

            final TableColumnModel columnModel = getColumnModel();

            columnModel.getColumn(COL_PAYMENT).setCellRenderer(new Currency());
            columnModel.getColumn(COL_RAW_PRICE).setCellRenderer(new Currency());
            columnModel.getColumn(COL_VAT_PRICE).setCellRenderer(new Currency());
            columnModel.getColumn(COL_REAL_PRICE).setCellRenderer(new Currency());
            columnModel.getColumn(COL_DISCOUNT).setCellRenderer(new Currency());

            columnModel.getColumn(COL_ID).setPreferredWidth(76);

            setRowSorter(new TransactionsRowSorter(tableModel));

            setShowGrid(true);
            setRowHeight(38);
            putClientProperty(FlatClientProperties.STYLE, "font:12;");
        }

        public void populate(final SaleMetadataDto[] sales) {
            final TransactionsModel model = (TransactionsModel) getModel();

            model.setRowCount(0);

            for (final SaleMetadataDto sale : sales) {
                BigDecimal payment = new BigDecimal("0.00");
                BigDecimal revenue = new BigDecimal("0.00");

                for (final SaleMetadataPayments p : sale.getPayments()) {
                    payment = payment.add(p.getAmountPhp());
                }

                for (final SaleItemStocks p : sale.getSaleItemStocks()) {
                    revenue = revenue.add(p.getUnitPricePhp().multiply(new BigDecimal(p.getQuantity())));
                }

                BigDecimal discount = new BigDecimal("0.00");

                System.out.println(sale);

                if (sale.getDiscountType() == null || sale.getDiscountType().isEmpty()) {
                } else if (sale.getDiscountType().equals(DiscountType.FIXED.toString())) {
                    discount = sale.getDiscountValue();
                } else if (sale.getDiscountType().equals(DiscountType.PERCENTAGE.toString())) {
                    discount = revenue.multiply(sale.getDiscountValue());
                }

                System.out.println(discount);

                revenue = revenue.subtract(discount);
                final BigDecimal vatPrice = revenue.multiply(sale.getVatPercent());

                model.addRow(new Object[] { sale.getSaleId(),
                        sale.getSaleCode(),
                        sale.getCustomerName() == null || sale.getCustomerName().isEmpty() ? "No entry provided"
                                : sale.getCustomerName(),
                        payment, revenue,
                        revenue.add(vatPrice), vatPrice, discount });
            }

            repaint();
            revalidate();
        }

        public class TransactionsModel extends DefaultTableModel {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(final int columnIndex) {
                return switch (columnIndex) {
                    case COL_ID -> Integer.class;
                    case COL_CODE -> String.class;
                    case COL_PAYMENT -> BigDecimal.class;
                    case COL_RAW_PRICE -> BigDecimal.class;
                    case COL_VAT_PRICE -> BigDecimal.class;
                    case COL_REAL_PRICE -> BigDecimal.class;
                    case COL_DISCOUNT -> BigDecimal.class;
                    case COL_CUSTOMER_NAME -> String.class;
                    default -> super.getColumnClass(columnIndex);
                };
            }
        }

        public class TransactionsRowSorter extends TableRowSorter<TransactionsModel> {
            public TransactionsRowSorter(final TransactionsModel model) {
                super(model);
            }

            @Override
            public Comparator<?> getComparator(final int column) {
                return switch (column) {
                    case COL_RAW_PRICE -> new Comparator<BigDecimal>() {
                        public int compare(final BigDecimal arg0, final BigDecimal arg1) {
                            return arg0.compareTo(arg1);
                        };
                    };
                    case COL_VAT_PRICE -> new Comparator<BigDecimal>() {
                        public int compare(final BigDecimal arg0, final BigDecimal arg1) {
                            return arg0.compareTo(arg1);
                        };
                    };
                    case COL_REAL_PRICE -> new Comparator<BigDecimal>() {
                        public int compare(final BigDecimal arg0, final BigDecimal arg1) {
                            return arg0.compareTo(arg1);
                        };
                    };
                    case COL_PAYMENT -> new Comparator<BigDecimal>() {
                        public int compare(final BigDecimal arg0, final BigDecimal arg1) {
                            return arg0.compareTo(arg1);
                        };
                    };
                    case COL_DISCOUNT -> new Comparator<BigDecimal>() {
                        public int compare(final BigDecimal arg0, final BigDecimal arg1) {
                            return arg0.compareTo(arg1);
                        };
                    };
                    default -> super.getComparator(column);
                };
            }
        }
    }
}
