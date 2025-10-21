/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.dialog;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

import javax.swing.JDialog;

import org.jetbrains.annotations.NotNull;

import net.miginfocom.swing.MigLayout;

public final class FilterDialog extends JDialog {
    private @NotNull final ComponentAdapter componentListener;;

    private @NotNull final Consumer<FilterDialogResult> onUpdate;

    private @NotNull final Window owner;
    private @NotNull final WindowAdapter windowListener;

    public FilterDialog(@NotNull Window owner, Consumer<FilterDialogResult> onUpdate) {
        super(owner, "Filter products", Dialog.ModalityType.APPLICATION_MODAL);

        this.onUpdate = onUpdate;
        this.owner = owner;

        this.windowListener = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                dispose();
            }
        };
        this.componentListener = new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateDisplay();
            }
        };

        setLayout(new MigLayout("insets 16, flowy, gap 2 16", "[grow, fill]", "[top]"));
        pack();

        setSize(new Dimension(500, 450));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(windowListener);
        addComponentListener(componentListener);
    }

    private record FilterDialogResult(String category, String brand) {
    }

    private void updateDisplay() {
    }
}
