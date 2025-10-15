/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.components;

import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class ProductList implements SceneComponent {
    private final JPanel view = new JPanel();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public ProductList() {}

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        initialized.set(true);
    }

    @Override
    public void destroy() {
        view.removeAll();

        initialized.set(false);
    }

    @Override
    public boolean isInitialized() {
        return initialized.get();
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
