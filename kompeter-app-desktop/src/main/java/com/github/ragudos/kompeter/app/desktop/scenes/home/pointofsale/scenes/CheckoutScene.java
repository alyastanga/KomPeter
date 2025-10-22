/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.components.factory.ButtonFactory;
import com.github.ragudos.kompeter.app.desktop.components.icons.AvatarIcon;
import com.github.ragudos.kompeter.app.desktop.layout.ResponsiveLayout;
import com.github.ragudos.kompeter.app.desktop.listeners.ButtonSceneNavigationActionListener;
import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.pointofsale.Cart;
import com.github.ragudos.kompeter.utilities.HtmlUtils;

import net.miginfocom.swing.MigLayout;

public final class CheckoutScene implements Scene {
    public static final String SCENE_NAME = "checkout";

    private final JButton backButton = ButtonFactory.createButton("Back", "arrow-left.svg",
            SceneNames.HomeScenes.PointOfSaleScenes.SHOP_SCENE, "ghost");

    private final JPanel body = new JPanel(
            new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 9, 9));

    private final Cart cart = Cart.getInstance();
    private final JPanel controlsPanel = new JPanel(new MigLayout("insets 0, flowx, gapx 3px"));
    private final JPanel header = new JPanel(new MigLayout("insets 9, flowx", "[]push[]push[]", "[grow, fill]"));
    private final JButton placeOrder = ButtonFactory.createButton("Order", "package-check.svg", "", "ghost");
    private final JScrollPane scrollPane = new JScrollPane(body);

    private final JLabel totalTitle = new JLabel("");
    private final JPanel view = new JPanel(new MigLayout("insets 0, fill, flowy", "[grow, fill, center]",
            "[top, grow 0, shrink][grow, fill, center]"));

    @Override
    public boolean canHide() {
        return Scene.super.canHide();
    }

    @Override
    public boolean canShow() {
        return Scene.super.canShow();
    }

    @Override
    public @NotNull String name() {
        return SCENE_NAME;
    }

    @Override
    public void onCreate() {
        controlsPanel.add(placeOrder);

        header.add(backButton);
        header.add(totalTitle);
        header.add(controlsPanel);

        totalTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");

        view.add(header, "growx");
        view.add(scrollPane, "grow");
    }

    @Override
    public void onDestroy() {
        body.removeAll();
        backButton.removeActionListener(ButtonSceneNavigationActionListener.LISTENER);
        cart.destroy();
    }

    @Override
    public void onHide() {
        body.removeAll();
        backButton.removeActionListener(ButtonSceneNavigationActionListener.LISTENER);
    }

    @Override
    public void onShow() {
        totalTitle.setText(String.format("Quantity: %s, Total: ₱%s", cart.totalQuantity(), cart.totalPrice()));
        backButton.addActionListener(ButtonSceneNavigationActionListener.LISTENER);

        cart.getAllItems().forEach((item) -> {
            JPanel itemContainer = new JPanel(
                    new MigLayout("insets 3, gapx 9px", "[grow 0][grow, fill]", "[grow, fill]"));

            AvatarIcon avatarIcon = new AvatarIcon(AssetManager.class.getResource("placeholder.png"), 120, 120, 8);
            JLabel itemImage = new JLabel(avatarIcon);

            JPanel contentContainer = new JPanel(new MigLayout("insets 9, gap 3px 2px"));

            JLabel itemName = new JLabel(HtmlUtils.wrapInHtml(item.productName()));
            JLabel brand = new JLabel(HtmlUtils.wrapInHtml(item.brand()));
            JLabel category = new JLabel(HtmlUtils.wrapInHtml(item.category()));
            JLabel quantity = new JLabel(HtmlUtils.wrapInHtml("Quantity: " + item.qty()));

            JButton editButton = new JButton("edit",
                    AssetManager.getOrLoadIcon("pen.svg", 0.75f, "foreground.primary"));

            itemName.putClientProperty(FlatClientProperties.STYLE_CLASS, "h2");
            brand.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
            category.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
            quantity.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

            editButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
            editButton.putClientProperty(FlatClientProperties.STYLE, "margin:3,9,3,9;" + "arc:8;");
            // addButton.addActionListener(addButtonListener);
            editButton.setActionCommand(String.format("%s", item._itemStockId()));

            contentContainer.add(itemName, "wrap");
            contentContainer.add(brand);
            contentContainer.add(category, "wrap");
            contentContainer.add(quantity, "wrap, pushy");

            contentContainer.add(editButton);

            FlatRoundBorder b = new FlatRoundBorder();

            b.applyStyleProperty("arc", 16);
            itemContainer.putClientProperty(FlatClientProperties.STYLE,
                    "[light]background:darken($Panel.background,3%);"
                            + "[dark]background:lighten($Panel.background,3%);");
            itemContainer.setBorder(b);
            itemContainer.add(itemImage);
            itemContainer.add(contentContainer);

            body.add(itemContainer);
        });
    }

    @Override
    public @NotNull JPanel view() {
        return view;
    }
}
