package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.dialog;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.components.factory.ButtonFactory;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

import net.miginfocom.swing.MigLayout;

public final class AddToCartDialog extends JDialog {
    private @NotNull final Window owner;
    private @NotNull final Consumer<UpdatePayload> onUpdate;

    private @NotNull final JPanel itemMetadataContainer = new JPanel(new MigLayout("insets 3, gap 3 9"));
    private @NotNull final JLabel itemStockId = new JLabel();
    private @NotNull final JLabel itemName = new JLabel();
    private @NotNull final JLabel itemCategory = new JLabel();
    private @NotNull final JLabel itemBrand = new JLabel();
    private @NotNull final JLabel itemPrice = new JLabel();
    private @NotNull final JPanel qtyContainer = new JPanel(
            new MigLayout("insets 3, flowy, gapy 3", "[grow, center, fill]"));
    private @NotNull final JPanel buttonsContainer = new JPanel(
            new MigLayout("insets 3, flowx, gapx 3", "[grow, fill, center]"));
    private @NotNull final JButton submitButton = ButtonFactory.createButton("Submit", "primary");
    private @NotNull final JButton cancelButton = ButtonFactory.createButton("Cancel", "muted");
    private @NotNull final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
    private @NotNull final SubmitButtonActionListener submitButtonListener = new SubmitButtonActionListener();
    private @NotNull final CancelButtonActionListener cancelButtonListener = new CancelButtonActionListener();

    public AddToCartDialog(@NotNull Window owner, Consumer<UpdatePayload> onUpdateCb) {
        super(owner, "Add to cart", Dialog.ModalityType.APPLICATION_MODAL);

        this.owner = owner;
        this.onUpdate = onUpdateCb;

        setLayout(new MigLayout("insets 9, flowy", "[grow, fill, center]", "[]9px[]48px[]"));

        itemStockId.putClientProperty(FlatClientProperties.STYLE_CLASS, "h0");
        itemName.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");
        itemCategory.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");
        itemBrand.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");
        itemPrice.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");

        itemMetadataContainer.add(itemStockId, "wrap");
        itemMetadataContainer.add(itemName, "wrap");
        itemMetadataContainer.add(itemCategory);
        itemMetadataContainer.add(itemBrand, "wrap");
        itemMetadataContainer.add(itemPrice);

        qtyContainer.add(new JLabel("quantity"));
        qtyContainer.add(quantitySpinner);

        buttonsContainer.add(cancelButton);
        buttonsContainer.add(submitButton);

        add(itemMetadataContainer);
        add(qtyContainer);
        add(buttonsContainer);

        itemStockId.setText(String.format("Item Stock ID: %s", 0));
        itemName.setText("Item Name");
        itemCategory.setText("Category: " + "");
        itemBrand.setText("Brand: " + "");
        itemPrice.setText(String.format("Price: ₱ %s", 0.00));
        quantitySpinner.setValue(0);

        pack();
        setLocationRelativeTo(owner);
    }

    public void initialize() {
        submitButton.addActionListener(submitButtonListener);
        cancelButton.addActionListener(cancelButtonListener);
    }

    public void destroy() {
        submitButton.removeActionListener(submitButtonListener);
        cancelButton.removeActionListener(cancelButtonListener);
    }

    public void open(@Range(from = 0, to = Integer.MAX_VALUE) int _itemStockId, @NotNull String name,
            @NotNull String category, @NotNull String brand, double price, int qty) {
        itemStockId.setText(String.format("Item Stock ID: %s", _itemStockId));
        itemName.setText(name);
        itemCategory.setText("Category: " + category);
        itemBrand.setText("Brand: " + brand);
        itemPrice.setText(String.format("Price: ₱ %s", price));
        quantitySpinner.setValue(qty);

        pack();

        setVisible(true);
    }

    public static record UpdatePayload(@Range(from = 0, to = Integer.MAX_VALUE) int _itemStockId,
            @NotNull String itemName, @NotNull String itemCategory,
            @NotNull String itemBrand,
            double price, @Range(from = 0, to = Integer.MAX_VALUE) int quantity) {
    };

    private class SubmitButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                UpdatePayload payload = new UpdatePayload(
                        Integer.parseUnsignedInt(itemStockId.getText().split("Item Stock ID: ")[1]), itemName.getText(),
                        itemCategory.getText().split("Category: ")[1],
                        itemBrand.getText().split("Brand: ")[1],
                        Double.parseDouble(itemPrice.getText().split("Price: ₱ ")[1]),
                        (int) quantitySpinner.getValue());

                onUpdate.accept(payload);

                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(owner, "Failed to add item to cart.\n\nReason:\n" + e,
                        "Something went wrong", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private class CancelButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}
