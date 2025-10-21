/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.components.buttons.BadgeButton;
import com.github.ragudos.kompeter.app.desktop.components.factory.TextFieldFactory;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.dialog.FilterDialog;
import com.github.ragudos.kompeter.utilities.Debouncer;
import com.github.ragudos.kompeter.utilities.observer.Observer;

import net.miginfocom.swing.MigLayout;

public final class ShopHeader implements SceneComponent, Observer<SearchData> {
    private final BadgeButton cartButton = new BadgeButton(
            AssetManager.getOrLoadIcon("shopping-cart.svg", 0.75f, "foreground.primary"));
    private final ActionListener cartButtonListener = new CartActionListener();

    private final Debouncer debouncer = new Debouncer(250);

    private ExecutorService executorService;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final AtomicBoolean isBusy = new AtomicBoolean(false);

    private final JTextField searchField = TextFieldFactory.createSearchTextField(JTextField.LEFT, this::onClearSearch);

    private final SearchListener searchListener = new SearchListener();
    private final ArrayList<Consumer<SearchData>> subscribers = new ArrayList<>();

    private final JPanel view = new JPanel(
            new MigLayout("flowx, insets 3 9 3 9, gapx 9px", "[grow 50, shrink 25]9px[]48px[]", "[grow, fill]"));

    private final FilterDialog filterDialog = new FilterDialog(SwingUtilities.getWindowAncestor(view), this::onSearch);
    private final JButton filterButton = new JButton(
            AssetManager.getOrLoadIcon("filter.svg", 0.75f, "foreground.background"));
    private final FilterActionListener filterButtonListener = new FilterActionListener();

    public void changeCartQty(int qty) {
        cartButton.setBadgeNumber(qty);
    }

    @Override
    public void destroy() {
        view.removeAll();

        filterDialog.destroy();

        executorService.shutdown();

        filterButton.removeActionListener(filterButtonListener);
        cartButton.removeActionListener(cartButtonListener);
        searchField.getDocument().removeDocumentListener(searchListener);

        initialized.set(false);
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        executorService = Executors.newSingleThreadExecutor();

        filterButton.addActionListener(filterButtonListener);
        filterDialog.initialize();
        cartButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        cartButton.addActionListener(cartButtonListener);

        searchField.getDocument().addDocumentListener(searchListener);

        view.add(searchField, "growx");
        view.add(filterButton);
        view.add(cartButton);

        initialized.set(true);
    }

    @Override
    public boolean isBusy() {
        return isBusy.get();
    }

    @Override
    public boolean isInitialized() {
        return initialized.get();
    }

    @Override
    public void notifySubscribers(SearchData value) {
        for (Consumer<SearchData> subscriber : subscribers) {
            subscriber.accept(value);
        }
    }

    @Override
    public void subscribe(Consumer<SearchData> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Consumer<SearchData> subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }

    private void onClearSearch() {
        searchField.setText("");
        onSearch();
    }

    private void onSearch() {
        debouncer.call(() -> {
            SearchData searchData = new SearchData(searchField.getText(), filterDialog.getSelectedCategory(),
                    filterDialog.getSelectedBrand());

            notifySubscribers(searchData);
        });
    }

    private class FilterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            filterDialog.setVisible(true);
        }
    }

    private class CartActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cartButton.getBadgeNumber() == 0) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
                        "There are no items in cart yet :(", "Can't show cart", JOptionPane.ERROR_MESSAGE);

                return;
            }

            SceneNavigator.getInstance().navigateTo(SceneNames.HomeScenes.PointOfSaleScenes.CHECKOUT_SCENE);
        }
    }

    private class SearchListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            onSearch();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onSearch();
        }
    }
}
