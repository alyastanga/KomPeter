package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.app.desktop.components.factory.TextFieldFactory;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.objects.ComboBoxItemBrandOption;
import com.github.ragudos.kompeter.app.desktop.objects.ComboBoxItemCategoryOption;
import com.github.ragudos.kompeter.utilities.Debouncer;
import com.github.ragudos.kompeter.utilities.observer.Observer;

import net.miginfocom.swing.MigLayout;

public final class ShopHeader implements SceneComponent, Observer<SearchData> {
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

    private final JPanel view = new JPanel(
            new MigLayout("flowx, insets 3, gapx 9px", "[grow, fill][][]push[grow]", "[grow, fill]"));
    private final JTextField searchField = TextFieldFactory.createSearchTextField(JTextField.LEFT, this::onClearSearch);
    private final JComboBox<ComboBoxItemCategoryOption> categoryComboBox = new JComboBox<>();
    private final JComboBox<ComboBoxItemBrandOption> brandComboBox = new JComboBox<>();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final Debouncer debouncer = new Debouncer(250);

    private final ItemListener comboBoxItemListener = new ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            onSearch();
        };
    };
    private final SearchListener searchListener = new SearchListener();

    private final ArrayList<Consumer<SearchData>> subscribers = new ArrayList<>();

    private void onSearch() {
        ComboBoxItemCategoryOption selectedCategory = (ComboBoxItemCategoryOption) categoryComboBox.getSelectedItem();
        ComboBoxItemBrandOption selectedBrand = (ComboBoxItemBrandOption) brandComboBox.getSelectedItem();

        debouncer.call(() -> notifySubscribers(
                new SearchData(searchField.getText(),
                        selectedCategory._itemCategoryId() == -1 ? "" : selectedCategory.toString(),
                        selectedBrand._itemBrandId() == -1 ? "" : selectedBrand.toString())));
    }

    private void onClearSearch() {
        searchField.setText("");
        onSearch();
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        categoryComboBox.addItem(new ComboBoxItemCategoryOption(-1, "Category"));
        brandComboBox.addItem(new ComboBoxItemBrandOption(-1, "Brand"));

        searchField.getDocument().addDocumentListener(searchListener);
        categoryComboBox.addItemListener(comboBoxItemListener);
        brandComboBox.addItemListener(comboBoxItemListener);

        view.add(searchField, "grow");
        view.add(categoryComboBox);
        view.add(brandComboBox);

        initialized.set(true);
    }

    @Override
    public void destroy() {
        view.removeAll();

        searchField.getDocument().removeDocumentListener(searchListener);
        categoryComboBox.removeItemListener(comboBoxItemListener);
        brandComboBox.removeItemListener(comboBoxItemListener);

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
}
