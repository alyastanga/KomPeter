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

public class EditQuantityDialog extends JDialog {
    private final @NotNull Runnable onUpdate;
    private final @NotNull Window owner;

    public EditQuantityDialog(@NotNull Window owner, @NotNull Runnable onUpdateCb) {
        super(owner, "Edit Quantity", Dialog.ModalityType.APPLICATION_MODAL);

        this.owner = owner;
        this.onUpdate = onUpdateCb;
    }

    public void destroy() {
    }

    public void initialize() {
    }
}
