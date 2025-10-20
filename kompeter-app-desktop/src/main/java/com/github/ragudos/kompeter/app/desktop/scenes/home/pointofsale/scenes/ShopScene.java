/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.ProductList;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.SearchData;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.ShopHeader;

import net.miginfocom.swing.MigLayout;

import java.util.function.Consumer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

public final class ShopScene implements Scene {
    public static final String SCENE_NAME = "shop";

    private final JPanel view = new JPanel(
            new MigLayout("insets 0, fill, flowy", "[grow, fill]", "[top, grow 0, shrink][grow, fill]"));
    private final ProductList productList = new ProductList();
    private final ShopHeader shopHeader = new ShopHeader();

    private final Consumer<SearchData> shopHeaderSubscriber = new Consumer<SearchData>() {
        public void accept(SearchData arg0) {
            productList.searchItems(arg0);
        };
    };

    public ShopScene() {
        onCreate();
    }

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }

    @Override
    public void onCreate() {
        view.add(shopHeader.view(), "growx");
        view.add(productList.view(), "grow");

        shopHeader.initialize();
        productList.initialize();
    }

    @Override
    public void onShow() {
        shopHeader.subscribe(shopHeaderSubscriber);
    }

    @Override
    public void onHide() {
        shopHeader.unsubscribe(shopHeaderSubscriber);
    }

    @Override
    public void onDestroy() {
        shopHeader.destroy();
        productList.destroy();
    }

    @Override
    public boolean canHide() {
        return !shopHeader.isBusy() && !productList.isBusy();
    }

    @Override
    public void onCannotHide() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, "The current page is loading. Cannot navigate away right now",
                    "Failed to navigate", JOptionPane.ERROR_MESSAGE);
        });
    }
}
