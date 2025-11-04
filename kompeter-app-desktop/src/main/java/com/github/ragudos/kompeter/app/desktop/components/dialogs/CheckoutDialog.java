package com.github.ragudos.kompeter.app.desktop.components.dialogs;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.github.ragudos.kompeter.pointofsale.Cart;

import net.miginfocom.swing.MigLayout;

public class CheckoutDialog extends JDialog {
    private final Window owner;
    private final WindowAdapter windowListener;
    private final Cart cart;

    private final Runnable onCheckout;

    private final JPanel topPanel;
    private final JPanel bottomPanel;
    private final JPanel bottomControlPanel;

    private final AtomicBoolean isBusy;

    public CheckoutDialog(final Window owner, final Cart cart, final Runnable onCheckout) {
        super(owner, "Checkout", Dialog.ModalityType.APPLICATION_MODAL);

        this.owner = owner;
        this.cart = cart;
        this.onCheckout = onCheckout;
        this.isBusy = new AtomicBoolean(false);

        this.windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                dispose();
            }
        };

        setLayout(new MigLayout("insets 12, growx", "[grow, fill, center]"));

        topPanel = new JPanel();
        bottomPanel = new JPanel(new MigLayout("insets 0, flowx"));
        bottomControlPanel = new JPanel(new MigLayout("insets 0, flowx"));

        add(topPanel, "grow, wrap");
        add(bottomPanel, "growx, wrap, gapy 12px");
        add(bottomControlPanel, "growx, gapy 8px");

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);
    }

    private void createBottomPanel() {

    }

    private void createBottomControlPanel() {

    }
}
