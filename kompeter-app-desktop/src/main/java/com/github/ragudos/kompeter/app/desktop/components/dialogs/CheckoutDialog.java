/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.dialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.components.jspinner.CurrencySpinner;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.pointofsale.Cart;
import com.github.ragudos.kompeter.pointofsale.Transaction;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.StringUtils;

import lombok.Builder;
import lombok.Data;
import net.miginfocom.swing.MigLayout;

public class CheckoutDialog extends JDialog implements ActionListener, ChangeListener {
    private final Cart cart;

    private final Runnable onCheckout;

    private JLabel discount;
    private JLabel vat;
    private JLabel netTotal;
    private JLabel discountedNetTotal;
    private JLabel total;
    private JLabel change;
    private BigDecimal netTotalVal;
    private BigDecimal discountedNetTotalVal;
    private BigDecimal totalVal;
    private BigDecimal discountVal;
    private BigDecimal changeVal;
    private BigDecimal vatVal;

    private final JPanel topPanel;
    private final JPanel bottomPanel;
    private final JPanel bottomControlPanel;

    private com.github.ragudos.kompeter.app.desktop.components.dialogs.CheckoutDialog.DiscountDialog.DiscountData dData;
    private WindowAdapter windowListener;

    CurrencySpinner paymentSpinner;

    private final AtomicBoolean isBusy;
    private JTextField customerNameTextField;

    public CheckoutDialog(final Window owner, final Cart cart, final Runnable onCheckout) {
        super(owner, "Checkout", Dialog.ModalityType.APPLICATION_MODAL);

        this.cart = cart;
        this.onCheckout = onCheckout;
        this.isBusy = new AtomicBoolean(false);

        this.windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (isBusy.get()) {
                    return;
                }

                dispose();
            }
        };

        setLayout(new MigLayout("insets 12, flowx", "[grow, fill, center]"));

        topPanel = new JPanel(new MigLayout("insets 0, flowx", "[grow, fill, center]"));
        bottomPanel = new JPanel(new MigLayout("insets 6, flowx", "[grow, fill, left]push[grow, fill, right]"));
        bottomControlPanel = new JPanel(new MigLayout("insets 0, flowx", "[grow, fill ,center]"));

        add(topPanel, "grow, wrap");
        add(bottomPanel, "growx, wrap, gapy 16px");
        add(bottomControlPanel, "growx, gapy 10px");

        createTopPanel();
        createBottomPanel();
        createBottomControlPanel();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);
        addWindowListener(windowListener);
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        final CurrencySpinner spinner = (CurrencySpinner) e.getSource();

        final BigDecimal val = ((BigDecimal) spinner.getValue()).subtract(totalVal);

        if (val.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        changeVal = val;
        change.setText(StringUtils.formatBigDecimal(changeVal));
    }

    private void createTopPanel() {
        final JButton addDiscount = new JButton("Add Discount",
                new SVGIconUIColor("plus.svg", 0.5f, "foreground.background"));
        final JButton removeDiscount = new JButton("Remove Discount",
                new SVGIconUIColor("minus.svg", 0.5f, "foreground.background"));
        final JPanel buttonsPanel = new JPanel(new MigLayout("insets 0, flowx"));

        addDiscount.setToolTipText("Add a Discount (If one already exists, replace the old one)");
        removeDiscount.setToolTipText("Remove the set discount.");

        addDiscount.putClientProperty(FlatClientProperties.STYLE, "font:11;");
        addDiscount.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
        removeDiscount.putClientProperty(FlatClientProperties.STYLE, "font:11;");
        removeDiscount.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
        addDiscount.setActionCommand("discount");
        removeDiscount.setActionCommand("remove_discount");

        addDiscount.addActionListener(this);
        removeDiscount.addActionListener(this);

        final JLabel customerNameLbl = new JLabel("Customer Name");
        final JLabel paymentLabel = new JLabel("Payment Amount*");

        customerNameTextField = new JTextField();
        paymentSpinner = new CurrencySpinner();
        paymentSpinner.addChangeListener(this);

        customerNameTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter customer name...");

        buttonsPanel.add(addDiscount);
        buttonsPanel.add(removeDiscount, "gapx 1px");

        topPanel.add(buttonsPanel, "growx, wrap");
        topPanel.add(new JSeparator(JSeparator.HORIZONTAL), "growx, wrap, gapy 2px");
        topPanel.add(customerNameLbl, "growx, gapy 8px, wrap");
        topPanel.add(customerNameTextField, "growx, gapy 2px, wrap");
        topPanel.add(paymentLabel, "growx, gapy 4px, wrap");
        topPanel.add(paymentSpinner, "growx, gapy 2px, wrap");
    }

    private void createBottomPanel() {
        bottomPanel.setBorder(BorderFactory.createDashedBorder(null));

        final JLabel netTotalLabel = new JLabel("Net Price: ");
        netTotal = new JLabel("-.--");
        final JLabel discountLbl = new JLabel("Discount: ");
        discount = new JLabel("-.--");
        final JLabel discountedNetTotalLabel = new JLabel("Discounted Net Price: ");
        discountedNetTotal = new JLabel("-.--");
        final JLabel vatLbl = new JLabel("Tax 12%: ");
        vat = new JLabel("-.--");
        final JLabel totalLbl = new JLabel("Total Price: ");
        total = new JLabel("-.--");
        final JLabel changeLbl = new JLabel("Change: ");
        change = new JLabel("-.--");

        netTotal.setHorizontalAlignment(JLabel.RIGHT);
        discount.setHorizontalAlignment(JLabel.RIGHT);
        discountedNetTotal.setHorizontalAlignment(JLabel.RIGHT);
        vat.setHorizontalAlignment(JLabel.RIGHT);
        total.setHorizontalAlignment(JLabel.RIGHT);
        change.setHorizontalAlignment(JLabel.RIGHT);

        netTotalVal = cart.totalPrice();
        discountedNetTotalVal = netTotalVal;
        discountVal = new BigDecimal("0.00");
        vatVal = discountedNetTotalVal.multiply(Transaction.VAT_RATE);
        changeVal = new BigDecimal("0.00");
        totalVal = discountedNetTotalVal.add(vatVal);

        netTotal.setText(StringUtils.formatBigDecimal(netTotalVal));
        total.setText(StringUtils.formatBigDecimal(totalVal));
        vat.setText(StringUtils.formatBigDecimal(vatVal));
        discountedNetTotal.setText(StringUtils.formatBigDecimal(discountedNetTotalVal));

        netTotalLabel.putClientProperty(FlatClientProperties.STYLE, "font:-2;");
        netTotal.putClientProperty(FlatClientProperties.STYLE, "font:-2;");
        discountLbl.putClientProperty(FlatClientProperties.STYLE, "font:-2;");
        discount.putClientProperty(FlatClientProperties.STYLE, "font:-2;");
        vatLbl.putClientProperty(FlatClientProperties.STYLE, "font:-2;");
        vat.putClientProperty(FlatClientProperties.STYLE, "font:-2;");

        bottomPanel.add(netTotalLabel);
        bottomPanel.add(netTotal, "wrap");
        bottomPanel.add(discountLbl);
        bottomPanel.add(discount, "wrap");
        bottomPanel.add(vatLbl);
        bottomPanel.add(vat, "wrap");
        bottomPanel.add(new JSeparator(JSeparator.HORIZONTAL), "growx, span 2, wrap");
        bottomPanel.add(discountedNetTotalLabel);
        bottomPanel.add(discountedNetTotal, "wrap");
        bottomPanel.add(totalLbl);
        bottomPanel.add(total, "wrap");
        bottomPanel.add(totalLbl);
        bottomPanel.add(total, "wrap");
        bottomPanel.add(changeLbl);
        bottomPanel.add(change);
    }

    private void createBottomControlPanel() {
        final JButton cancelBtn = new JButton("Cancel", new SVGIconUIColor("circle-x.svg", 0.75f, "foreground.muted"));
        final JButton confirmBtn = new JButton("Process Payment",
                new SVGIconUIColor("circle-check.svg", 0.75f, "foreground.primary"));

        cancelBtn.setIconTextGap(16);
        confirmBtn.setIconTextGap(16);
        confirmBtn.setToolTipText("Process payment");
        cancelBtn.setToolTipText("Cancel operation");
        cancelBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        confirmBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        cancelBtn.addActionListener(this);
        confirmBtn.addActionListener(this);

        cancelBtn.setActionCommand("cancel");
        confirmBtn.setActionCommand("confirm");

        bottomControlPanel.add(cancelBtn, "split 2");
        bottomControlPanel.add(confirmBtn, "gapx 2px");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (isBusy.get()) {
            return;
        }

        if (e.getActionCommand().equals("confirm")) {
            if (((BigDecimal) paymentSpinner.getValue()).compareTo(totalVal) == -1) {
                JOptionPane.showMessageDialog(this, "The provided payment amount is insufficient.",
                        "Insufficient Amount", JOptionPane.ERROR_MESSAGE);

                return;
            }

            try {
                isBusy.set(true);
                final int _saleId = Transaction.createTransaction(cart, customerNameTextField.getText(),
                        (BigDecimal) paymentSpinner.getValue(), PaymentMethod.CASH,
                        dData == null ? null : dData.getType(),
                        dData == null ? new BigDecimal("0.00") : dData.getAmt());

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                            String.format("Transaction #%s has been successfully processed. Please proceed to"
                                    + " transactions to view it.", _saleId),
                            "Successful Transaction", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (final Exception err) {
                JOptionPane.showMessageDialog(this, err.getMessage(), "Something Went Wrong",
                        JOptionPane.ERROR_MESSAGE);

                return;
            } finally {
                isBusy.set(false);
            }

            onCheckout.run();
        } else if (e.getActionCommand().equals("discount")) {
            new DiscountDialog(this, (discountData) -> {
                // dont change totalVal here to maintain integrity from only processing
                // static values like Transaction.VAT_RATE
                switch (discountData.getType()) {
                    case FIXED -> {
                        discountVal = discountData.getAmt();
                        discountedNetTotalVal = netTotalVal.subtract(discountVal);
                    }
                    case PERCENTAGE -> {
                        discountVal = netTotalVal.multiply(discountData.getAmt());
                        discountedNetTotalVal = netTotalVal.subtract(discountVal);
                    }
                }

                dData = discountData;
                vatVal = discountedNetTotalVal.multiply(Transaction.VAT_RATE);
                totalVal = discountedNetTotalVal.add(vatVal);
                changeVal = ((BigDecimal) paymentSpinner.getValue()).subtract(totalVal);

                vat.setText(StringUtils.formatBigDecimal(vatVal));
                discount.setText(StringUtils.formatBigDecimal(discountVal));
                discountedNetTotal.setText(StringUtils.formatBigDecimal(discountedNetTotalVal));
                total.setText(StringUtils.formatBigDecimal(totalVal));

                if (changeVal.compareTo(BigDecimal.ZERO) > 0) {
                    change.setText(StringUtils.formatBigDecimal(changeVal));
                } else {
                    change.setText("-.--");
                }
            }).setVisible(true);

            return;
        } else if (e.getActionCommand().equals("remove_discount")) {
            discountVal = new BigDecimal("0.00");
            discountedNetTotalVal = netTotalVal;
            vatVal = discountedNetTotalVal.multiply(Transaction.VAT_RATE);
            totalVal = discountedNetTotalVal.add(vatVal);
            changeVal = ((BigDecimal) paymentSpinner.getValue()).subtract(totalVal);

            discount.setText("-.--");
            total.setText(StringUtils.formatBigDecimal(totalVal));
            discountedNetTotal.setText(StringUtils.formatBigDecimal(discountedNetTotalVal));
            vat.setText(StringUtils.formatBigDecimal(vatVal));

            if (changeVal.compareTo(BigDecimal.ZERO) > 0) {
                change.setText(StringUtils.formatBigDecimal(changeVal));
            } else {
                change.setText("-.--");
            }

            return;
        }

        dispose();
    }

    private class DiscountDialog extends JDialog implements ActionListener, ItemListener {
        JComboBox<DiscountType> dComboBox;

        CurrencySpinner fixedSpinner;

        JSpinner percentageSpinner;
        private final Consumer<DiscountData> onDiscount;

        public DiscountDialog(final Window owner, final Consumer<DiscountData> onDiscount) {
            super(owner, "Choose a Discount", Dialog.ModalityType.APPLICATION_MODAL);

            this.onDiscount = onDiscount;

            final JLabel title = new JLabel("Add a Discount");
            final JLabel subtitle = new JLabel(HtmlUtils.wrapInHtml("Only one discount is allowed."));

            subtitle.putClientProperty(FlatClientProperties.STYLE, "font:11;foreground:$Label.disabledForeground;");
            title.putClientProperty(FlatClientProperties.STYLE, "font:13;");
            title.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

            final JButton cancelBtn = new JButton("Cancel",
                    new SVGIconUIColor("circle-x.svg", 0.75f, "foreground.muted"));
            final JButton confirmBtn = new JButton("Add Discount",
                    new SVGIconUIColor("circle-check.svg", 0.75f, "foreground.primary"));
            dComboBox = new JComboBox<>(DiscountType.values());

            dComboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
                        final boolean isSelected, final boolean cellHasFocus) {
                    final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                            cellHasFocus);

                    if (value == null) {
                        label.setText("Select a discount type");
                        label.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
                    } else {
                        label.setText(value.toString());
                        label.putClientProperty(FlatClientProperties.STYLE, "ComboBox.foreground");
                    }

                    return label;
                }
            });

            percentageSpinner = new JSpinner(new SpinnerNumberModel(0.01, 0.01, 1.00, 0.01));
            fixedSpinner = new CurrencySpinner();

            setLayout(new MigLayout("insets 16, flowx", "[grow, fill, center]"));

            dComboBox.setSelectedIndex(-1);
            dComboBox.addItemListener(this);

            cancelBtn.setIconTextGap(16);
            confirmBtn.setIconTextGap(16);
            confirmBtn.setToolTipText("Add Discount (Replace current discount)");
            cancelBtn.setToolTipText("Cancel operation");
            cancelBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            confirmBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

            cancelBtn.addActionListener(this);
            confirmBtn.addActionListener(this);

            cancelBtn.setActionCommand("cancel");
            confirmBtn.setActionCommand("confirm");

            add(title, "wrap");
            add(subtitle, "wrap");
            add(dComboBox, "growx, gapy 8px, wrap");

            add(cancelBtn, "split 2, gapy 16px");
            add(confirmBtn, "gapx 2px");

            pack();
            setLocationRelativeTo(owner);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            dComboBox.putClientProperty("JComponent.outline", null);

            if (e.getActionCommand().equals("confirm")) {
                final DiscountType selected = (DiscountType) dComboBox.getSelectedItem();

                if (selected == DiscountType.FIXED) {
                    final BigDecimal val = (BigDecimal) fixedSpinner.getValue();

                    if (discountedNetTotalVal.subtract(val).compareTo(BigDecimal.ZERO) <= 0) {
                        JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                                "Cannot add discount because it will make the total price go 0 and below");

                        return;
                    }

                    onDiscount.accept(DiscountData.builder().amt(val)
                            .type(DiscountType.FIXED).build());
                } else if (selected == DiscountType.PERCENTAGE) {
                    final BigDecimal val = new BigDecimal((Double) percentageSpinner.getValue());

                    if (discountedNetTotalVal.subtract(val).compareTo(BigDecimal.ZERO) <= 0) {
                        JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                                "Cannot add discount because it will make the total price go 0 and below");

                        return;
                    }

                    onDiscount.accept(DiscountData.builder().amt(val)
                            .type(DiscountType.PERCENTAGE).build());
                } else {
                    dComboBox.putClientProperty("JComponent.outline", "error");

                    return;
                }
            }

            dispose();
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            if ((DiscountType) dComboBox.getSelectedItem() == DiscountType.FIXED) {
                remove(percentageSpinner);
                add(fixedSpinner, "wrap, gapy 4px", 3);
            } else {
                add(percentageSpinner, "wrap, gapy 4px", 3);
                remove(fixedSpinner);
            }

            repaint();
            revalidate();
            pack();
        }

        @Data
        @Builder
        public static class DiscountData {
            BigDecimal amt;
            DiscountType type;
        }
    }
}
