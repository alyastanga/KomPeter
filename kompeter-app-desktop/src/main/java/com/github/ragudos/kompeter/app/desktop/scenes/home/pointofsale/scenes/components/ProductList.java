/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.components.icons.AvatarIcon;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout.JustifyContent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.dialog.AddToCartDialog;
import com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components.dialog.AddToCartDialog.UpdatePayload;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.inventory.ItemService;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import net.miginfocom.swing.MigLayout;

public class ProductList implements SceneComponent {
    public static final double THRESHOLD = 0.7;

    private static Logger LOGGER = KompeterLogger.getLogger(ProductList.class);

    private final AddButtonListener addButtonListener = new AddButtonListener();

    private final AddToCartDialog addToCartDialog;

    private ExecutorService executorService;

    private final JaroWinklerSimilarity fuzzyFinder = new JaroWinklerSimilarity();
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicBoolean isBusy = new AtomicBoolean(false);

    private final AtomicReference<List<InventoryMetadataDto>> items = new AtomicReference<>();
    private final JPanel itemsContainer = new JPanel(
            new ResponsiveLayout(ResponsiveLayout.JustifyContent.CENTER, new Dimension(-1, -1), 9, 9));

    private final JScrollPane itemsContainerScroller = new JScrollPane(itemsContainer);

    private final AtomicReference<SearchData> prevSearchData = new AtomicReference<>();
    private final JPanel view = new JPanel(new MigLayout("insets 0", "[grow, fill]", "[grow, fill]"));

    public ProductList(Consumer<UpdatePayload> addToCartConsumer) {
        this.addToCartDialog = new AddToCartDialog(SwingUtilities.getWindowAncestor(view), addToCartConsumer);
    }

    @Override
    public void destroy() {
        removeAllItems();
        view.removeAll();

        addToCartDialog.destroy();
        executorService.shutdownNow();

        initialized.set(false);
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        executorService = Executors.newSingleThreadExecutor();

        itemsContainerScroller.getHorizontalScrollBar().setUnitIncrement(10);
        itemsContainerScroller.getVerticalScrollBar().setUnitIncrement(10);
        itemsContainerScroller.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        itemsContainerScroller.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "" + "trackArc:$ScrollBar.thumbArc;" + "thumbInsets:0,0,0,0;" + "width:9;");
        itemsContainerScroller.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        itemsContainer.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        itemsContainer.putClientProperty(FlatClientProperties.STYLE_CLASS, "monochrome");
        itemsContainer.add(new LoadingPanel());
        view.add(itemsContainerScroller, "grow");
        addToCartDialog.initialize();

        initialized.set(true);

        refresh();
    }

    @Override
    public boolean isBusy() {
        return isBusy.get();
    }

    @Override
    public boolean isInitialized() {
        return initialized.get();
    }

    public void refresh() {
        executorService.submit(() -> {
            isBusy.set(true);

            try {
                ItemService itemService = new ItemService();

                items.set((itemService.showInventoryItems()));

                searchItems(prevSearchData.get(), false);
            } catch (InventoryException e) {
                SwingUtilities.invokeLater(() -> {
                    removeAllItems();

                    ResponsiveLayout layout = ((ResponsiveLayout) itemsContainer.getLayout());
                    layout.setJustifyContent(JustifyContent.CENTER);
                    itemsContainer.add(new ErrorPanel(e.getMessage()));

                    view.repaint();
                    view.revalidate();
                });
            } finally {
                isBusy.set(false);
            }
        });
    }

    public void searchItems(SearchData searchData) {
        searchItems(searchData, true);
    }

    public void searchItems(SearchData searchData, boolean returnIfSame) {
        if (returnIfSame) {
            if (searchData.equals(prevSearchData.get())) {
                return;
            }
        }

        prevSearchData.set(searchData);

        SwingUtilities.invokeLater(() -> {
            isBusy.set(true);

            try {
                removeAllItems();

                ResponsiveLayout layout = ((ResponsiveLayout) itemsContainer.getLayout());

                if (searchData == null) {
                    layout.setJustifyContent(JustifyContent.FIT_CONTENT);

                    items.get().forEach(this::createItemCard);
                } else {
                    ArrayList<InventoryMetadataDto> filteredItems = new ArrayList<>(
                            items.get().stream().filter((item) -> {
                                double itemNameSimilarity = searchData.searchText().isEmpty()
                                        ? THRESHOLD
                                        : fuzzyFinder.apply(searchData.searchText(), item.itemName());

                                if (searchData.searchCategory().isEmpty() && searchData.searchBrand().isEmpty()) {
                                    return itemNameSimilarity >= THRESHOLD;
                                }

                                boolean categoryMatches = searchData.searchCategory().isEmpty()
                                        || item.categoryName().toLowerCase(Locale.ENGLISH)
                                                .equals(searchData.searchCategory().toLowerCase(Locale.ENGLISH));
                                boolean brandMatches = searchData.searchBrand().isEmpty()
                                        || item.brandName().toLowerCase(Locale.ENGLISH)
                                                .equals(searchData.searchBrand().toLowerCase(Locale.ENGLISH));

                                return itemNameSimilarity >= THRESHOLD && categoryMatches && brandMatches;
                            }).collect(Collectors.toList()));

                    if (filteredItems.isEmpty()) {
                        layout.setJustifyContent(JustifyContent.CENTER);

                        itemsContainer.add(new NoResultsPanel());
                    } else {
                        layout.setJustifyContent(JustifyContent.FIT_CONTENT);

                        filteredItems.forEach(this::createItemCard);
                    }
                }

                view.repaint();
                view.revalidate();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "", e);
            } finally {
                isBusy.set(false);
            }
        });
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }

    private void createItemCard(InventoryMetadataDto item) {
        JPanel itemContainer = new JPanel(new MigLayout("insets 9, gapx 9px", "[grow 0][grow, fill]", "[grow, fill]"));

        AvatarIcon avatarIcon = new AvatarIcon(AssetManager.class.getResource("placeholder.png"), 120, 120, 8);
        JLabel itemImage = new JLabel(avatarIcon);

        JPanel contentContainer = new JPanel(
                new MigLayout("insets 0, gapy 2px, gapx 6px", "[grow]", "[][]4px[]push[]"));

        JButton addButton = new JButton("add", AssetManager.getOrLoadIcon("plus.svg", 0.5f, "foreground.primary"));
        JLabel itemName = new JLabel(HtmlUtils.wrapInHtml(item.itemName()));
        JLabel brand = new JLabel(HtmlUtils.wrapInHtml(item.brandName()));
        JLabel category = new JLabel(HtmlUtils.wrapInHtml(item.categoryName()));
        JLabel quantity = new JLabel(HtmlUtils.wrapInHtml(item.quantity() + " items left"));

        addButton.setLayout(new MigLayout("insets 0", "[]push[]"));

        addButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        addButton.putClientProperty(FlatClientProperties.STYLE, "margin:3,6,3,6;" + "arc:8;");
        addButton.addActionListener(addButtonListener);
        addButton.setActionCommand(
                String.format("%s;%s;%s", item._itemId(), item._itemStockId(), item._stockLocationId()));

        itemName.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

        contentContainer.add(itemName, "wrap");
        contentContainer.add(brand, "split 2");
        contentContainer.add(category, "wrap");
        contentContainer.add(quantity, "wrap");
        contentContainer.add(addButton, "growx");

        itemContainer.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken($Panel.background,3%);" + "[dark]background:lighten($Panel.background,3%);"
                        + "arc: 16;" + String.format("border:%s;", DropShadowBorder.class.getPackageName()));
        contentContainer.putClientProperty(FlatClientProperties.STYLE, "background:fade($Panel.background, 0%);");
        itemContainer.add(itemImage);
        itemContainer.add(contentContainer);

        itemsContainer.add(itemContainer);
    }

    private void removeAllButtonListeners(Container container) {
        for (Component component : container.getComponents()) {
            System.out.println("Removing Button Listener!");
            if (component instanceof JButton) {
                JButton button = (JButton) component;

                for (ActionListener listener : button.getActionListeners()) {
                    button.removeActionListener(listener);
                }
            } else if (component instanceof Container) {
                removeAllButtonListeners((Container) component);
            }
        }
    }

    private void removeAllItems() {
        removeAllButtonListeners(itemsContainer);
        itemsContainer.removeAll();
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] stringData = e.getActionCommand().split(";");

            try {
                int itemId = Integer.parseUnsignedInt(stringData[0]);
                int itemStockId = Integer.parseUnsignedInt(stringData[1]);
                int itemStockLocationId = Integer.parseUnsignedInt(stringData[2]);
                Optional<InventoryMetadataDto> chosenItem = items.get().stream()
                        .filter((item) -> item._itemId() == itemId && item._itemStockId() == itemStockId
                                && item._stockLocationId() == itemStockLocationId)
                        .findFirst();

                if (chosenItem.isEmpty()) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
                            "Can't add item to cart because of bad logic.", "Something went wrong",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                InventoryMetadataDto item = chosenItem.get();

                addToCartDialog.open(item._itemStockId(), item.itemName(), item.categoryName(), item.brandName(),
                        item.itemPricePhp(), 1);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(view),
                        "Can't add item to cart because of bad logic.\n\nReason:\n" + err.getMessage(),
                        "Something went wrong", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ErrorPanel extends JPanel {
        public ErrorPanel(String message) {
            add(new JLabel("Failed to show items. Try again."));
        }
    }

    private class LoadingPanel extends JPanel {
        public LoadingPanel() {
            setLayout(new BorderLayout());

            add(new JLabel("Loading..."), BorderLayout.CENTER);
        }
    }

    private class NoResultsPanel extends JPanel {
        public NoResultsPanel() {
            setLayout(new BorderLayout());

            JLabel label = new JLabel("No results were found. :(");

            label.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");

            add(label, BorderLayout.CENTER);
        }
    }
}
