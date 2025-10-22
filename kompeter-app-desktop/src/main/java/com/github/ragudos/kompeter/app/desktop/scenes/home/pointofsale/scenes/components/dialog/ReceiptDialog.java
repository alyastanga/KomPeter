/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.dialog;

import java.awt.Dialog;
import java.awt.Window;

import javax.swing.JDialog;

import org.jetbrains.annotations.NotNull;

public class ReceiptDialog extends JDialog {
    private final @NotNull Window owner;

    public ReceiptDialog(@NotNull Window owner) {
        super(owner, "Receipt", Dialog.ModalityType.APPLICATION_MODAL);

        this.owner = owner;

        pack();
    }

    public void destroy() {
    }

    public void initialize() {
    }
}
