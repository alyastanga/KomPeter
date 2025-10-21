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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.objects.ComboBoxItemBrandOption;
import com.github.ragudos.kompeter.app.desktop.objects.ComboBoxItemCategoryOption;
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemCategoryDto;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.inventory.ItemService;

import net.miginfocom.swing.MigLayout;

public final class FilterDialog extends JDialog {
    private @NotNull final ComponentAdapter componentListener;

    private @NotNull final Runnable onUpdate;

    private @NotNull final Window owner;
    private @NotNull final WindowAdapter windowListener;

    private @NotNull final JComboBox<ComboBoxItemBrandOption> brandComboBox;
    private @NotNull final JComboBox<ComboBoxItemCategoryOption> categoryComboBox;

    private ExecutorService executorService;

    private @NotNull final AtomicBoolean isBusy;

    private @NotNull JButton resetBtn;
    private @NotNull JButton submitBtn;
    private @NotNull ActionListener submitBtnListener;
    private @NotNull ActionListener resetBtnListener;

    private int currentSelectedCategoryIndex = 0;
    private int currentSelectedBrandIndex = 0;
    private String currentSelectedCategory = "";
    private String currentSelectedBrand = "";

    public FilterDialog(@NotNull Window owner, Runnable onUpdateCb) {
        super(owner, "Filter products", Dialog.ModalityType.APPLICATION_MODAL);

        this.onUpdate = onUpdateCb;
        this.owner = owner;

        this.categoryComboBox = new JComboBox<>();
        this.brandComboBox = new JComboBox<>();
        this.isBusy = new AtomicBoolean(false);

        this.windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                categoryComboBox.setSelectedIndex(currentSelectedCategoryIndex);
                brandComboBox.setSelectedIndex(currentSelectedBrandIndex);

                dispose();
            }
        };
        this.componentListener = new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                currentSelectedBrand = getSelectedBrand();
                currentSelectedCategory = getSelectedCategory();
            }
        };
        this.submitBtnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        };
        this.resetBtnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        };

        setLayout(new MigLayout("insets 16, flowy, gapy 16", "[grow, fill, center]9px[grow, fill, center]",
                "[grow, center, fill][]"));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        submitBtn = new JButton("Apply");
        resetBtn = new JButton("Reset");

        submitBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        resetBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");

        JPanel comboBoxContainers = new JPanel(new MigLayout("insets 0, flowy", "[grow, fill, center]"));

        comboBoxContainers.add(categoryComboBox, "grow");
        comboBoxContainers.add(brandComboBox, "grow");

        add(comboBoxContainers, "cell 0 0 2, grow");
        add(resetBtn, "growx, cell 0 1");
        add(submitBtn, "growx, cell 1 1");

        setSize(new Dimension(225, 175));
        setLocationRelativeTo(owner);
    }

    public void initialize() {
        addWindowListener(windowListener);
        addComponentListener(componentListener);

        categoryComboBox.addItem(new ComboBoxItemCategoryOption(-1, "Category"));
        brandComboBox.addItem(new ComboBoxItemBrandOption(-1, "Brand"));

        categoryComboBox.setSelectedIndex(0);
        brandComboBox.setSelectedIndex(0);

        executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> populateComboBoxes());

        submitBtn.addActionListener(submitBtnListener);
        resetBtn.addActionListener(resetBtnListener);
    }

    public void destroy() {
        submitBtn.removeActionListener(submitBtnListener);

        executorService.shutdown();

        brandComboBox.removeAllItems();
        categoryComboBox.removeAllItems();

        removeWindowListener(windowListener);
        removeComponentListener(componentListener);
    }

    public String getSelectedCategory() {
        ComboBoxItemCategoryOption categoryOption = (ComboBoxItemCategoryOption) categoryComboBox.getSelectedItem();

        return categoryOption._itemCategoryId() == -1 ? "" : categoryOption.name();
    }

    public String getSelectedBrand() {
        ComboBoxItemBrandOption brandOption = (ComboBoxItemBrandOption) brandComboBox.getSelectedItem();

        return brandOption._itemBrandId() == -1 ? "" : brandOption.name();
    }

    private void submit() {
        if (!currentSelectedBrand.equals(getSelectedBrand())
                || !currentSelectedCategory.equals(getSelectedCategory())) {
            onUpdate.run();

            currentSelectedBrand = getSelectedBrand();
            currentSelectedCategory = getSelectedCategory();
            currentSelectedBrandIndex = brandComboBox.getSelectedIndex();
            currentSelectedCategoryIndex = categoryComboBox.getSelectedIndex();
        }

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void reset() {
        currentSelectedBrandIndex = 0;
        currentSelectedCategoryIndex = 0;
        categoryComboBox.setSelectedIndex(0);
        brandComboBox.setSelectedIndex(0);

        onUpdate.run();

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void populateComboBoxes() {
        isBusy.set(true);

        try {
            ItemService itemService = new ItemService();

            List<ItemBrandDto> itemBrands = itemService.showAllBrands();
            List<ItemCategoryDto> itemCategories = itemService.showAllCategories();

            SwingUtilities.invokeLater(() -> {
                itemBrands.forEach((brand) -> {
                    brandComboBox.addItem(new ComboBoxItemBrandOption(brand._itemBrandId(), brand.name()));
                });

                itemCategories.forEach((category) -> {
                    categoryComboBox
                            .addItem(new ComboBoxItemCategoryOption(category._itemCategoryId(),
                                    category.name()));
                });
            });
        } catch (InventoryException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "Failed to get item categories and brands. Filtering by these criterion won't"
                                + " work.",
                        "Something went wrong", JOptionPane.ERROR_MESSAGE);
            });
        } finally {
            isBusy.set(false);
        }
    }
}
