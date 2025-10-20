/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.components.icons.AvatarIcon;
import com.github.ragudos.kompeter.app.desktop.components.icons.SpinnerIcon;
import com.github.ragudos.kompeter.app.desktop.components.spinner.SpinnerProgress;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout.JustifyContent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.inventory.InventoryException;
import com.github.ragudos.kompeter.inventory.ItemService;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.jetbrains.annotations.NotNull;

public class ProductList implements SceneComponent {
    private static Logger LOGGER = KompeterLogger.getLogger(ProductList.class);

    private class ErrorPanel extends JPanel {
        public ErrorPanel(String message) {
            add(new JLabel("Failed to show items. Try again."));
        }
    }

    private class LoadingPanel extends JPanel {
        public LoadingPanel() {
            setLayout(new BorderLayout());

            SpinnerProgress spinner = new SpinnerProgress();

            spinner.setHorizontalTextPosition(SwingConstants.CENTER);
            spinner.setVerticalTextPosition(SwingConstants.BOTTOM);

            spinner.setStringPainted(true);
            spinner.setString("Loading...");

            spinner.setIndeterminate(true);

            add(spinner, BorderLayout.CENTER);

        }
    }

    private class NoResultsPanel extends JPanel {
        public NoResultsPanel() {

        }
    }

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final JPanel view = new JPanel(
            new MigLayout("insets 0, flowy, gapy 16px", "[grow, fill, center]", "[grow, fill, center]"));
    private final JPanel itemsContainer = new JPanel(new ResponsiveLayout(
            ResponsiveLayout.JustifyContent.CENTER, new Dimension(-1, -1), 9, 9));
    private final JScrollPane itemsContainerScroller = new JScrollPane(itemsContainer);

    private ExecutorService executorService;
    private final AtomicReference<List<InventoryMetadataDto>> items = new AtomicReference<>();

    private final JaroWinklerSimilarity fuzzyFinder = new JaroWinklerSimilarity();

    public static final double THRESHOLD = 0.7;

    private final AtomicReference<SearchData> prevSearchData = new AtomicReference<>();

    private final AtomicBoolean isBusy = new AtomicBoolean(false);

    private void createItemCard(InventoryMetadataDto item) {
        JPanel itemContainer = new JPanel(
                new MigLayout("insets 3, gapx 9px", "[grow 0][grow, fill]", "[grow, fill]"));

        AvatarIcon avatarIcon = new AvatarIcon(AssetManager.class.getResource("placeholder.png"), 120, 120, 8);
        JLabel itemImage = new JLabel(avatarIcon);

        JPanel contentContainer = new JPanel(new MigLayout("insets 0, gapy 2px, flowy", "[grow]", "[][][]push[]"));

        JButton addButton = new JButton("add", AssetManager.getOrLoadIcon("plus.svg", 0.75f, "foreground.primary"));
        JLabel itemName = new JLabel(HtmlUtils.wrapInHtml(item.itemName()));
        JLabel brand = new JLabel(HtmlUtils.wrapInHtml(item.brandName()));
        JLabel category = new JLabel(HtmlUtils.wrapInHtml(item.categoryName()));

        addButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        addButton.putClientProperty(FlatClientProperties.STYLE, "margin:3,9,3,9;" + "arc:8;");

        itemName.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        brand.putClientProperty(FlatClientProperties.STYLE_CLASS, "h6");
        category.putClientProperty(FlatClientProperties.STYLE_CLASS, "h6");

        contentContainer.add(itemName);
        contentContainer.add(brand);
        contentContainer.add(category);
        contentContainer.add(addButton, "growx");

        FlatRoundBorder b = new FlatRoundBorder();

        b.applyStyleProperty("arc", 16);
        itemContainer.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken($Panel.background,3%);" +
                        "[dark]background:lighten($Panel.background,3%);");
        itemContainer.setBorder(b);
        itemContainer.add(itemImage);
        itemContainer.add(contentContainer);

        itemsContainer.add(itemContainer);
    }

    public void searchItems(SearchData searchData) {
        searchItems(searchData, true);
    }

    public void searchItems(SearchData searchData, boolean returnIfSame) {
        if (returnIfSame) {
            if (searchData.equals(prevSearchData.getAcquire())) {
                return;
            }
        }

        prevSearchData.set(searchData);

        SwingUtilities.invokeLater(() -> {
            isBusy.set(true);

            try {

                itemsContainer.removeAll();

                ResponsiveLayout layout = ((ResponsiveLayout) itemsContainer.getLayout());

                if (searchData == null) {
                    layout.setJustifyContent(JustifyContent.FIT_CONTENT);

                    items.getAcquire().forEach(this::createItemCard);
                } else {
                    ArrayList<InventoryMetadataDto> filteredItems = new ArrayList<>(
                            items.getAcquire().stream().filter((item) -> {
                                double itemNameSimilarity = searchData.searchText().isEmpty() ? THRESHOLD
                                        : fuzzyFinder.apply(searchData.searchText(), item.itemName());

                                if (searchData.searchCategory().isEmpty() && searchData.searchBrand().isEmpty()) {
                                    return itemNameSimilarity >= THRESHOLD;
                                }

                                return itemNameSimilarity >= THRESHOLD &&
                                        (item.categoryName().isEmpty()
                                                ? true
                                                : item.categoryName().toLowerCase()
                                                        .contains(searchData.searchCategory().toLowerCase()))
                                        &&
                                        (item.brandName().isEmpty() ? true
                                                : item.brandName().toLowerCase()
                                                        .equals(searchData.searchBrand().toLowerCase()));
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

    public void refresh() {
        executorService.submit(() -> {
            isBusy.set(true);

            try {
                ItemService itemService = new ItemService();

                items.set((itemService.showInventoryItems()));

                searchItems(prevSearchData.getAcquire(), false);
            } catch (InventoryException e) {
                SwingUtilities.invokeLater(() -> {
                    itemsContainer.removeAll();

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

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        executorService = Executors.newSingleThreadExecutor();

        itemsContainerScroller.getHorizontalScrollBar().setUnitIncrement(16);
        itemsContainerScroller.getVerticalScrollBar().setUnitIncrement(16);
        itemsContainerScroller.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackArc:$ScrollBar.thumbArc;" +
                "thumbInsets:0,0,0,0;" +
                "width:5;");
        itemsContainerScroller.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackArc:$ScrollBar.thumbArc;" +
                "thumbInsets:0,0,0,0;" +
                "width:5;");
        itemsContainerScroller.setBorder(BorderFactory.createEmptyBorder(9, 0, 9, 0));

        itemsContainer.add(new LoadingPanel());
        view.add(itemsContainerScroller, "grow, center");

        initialized.set(true);

        refresh();
    }

    @Override
    public void destroy() {
        view.removeAll();

        executorService.shutdown();

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
    public boolean isBusy() {
        return isBusy.get();
    }
}
