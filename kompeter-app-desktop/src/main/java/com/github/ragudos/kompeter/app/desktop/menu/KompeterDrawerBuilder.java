/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.menu;

import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.forms.FormInventoryAddProduct;
import com.github.ragudos.kompeter.app.desktop.forms.FormInventoryBrowseProducts;
import com.github.ragudos.kompeter.app.desktop.forms.FormMonitoringInventory;
import com.github.ragudos.kompeter.app.desktop.forms.FormMonitoringSales;
import com.github.ragudos.kompeter.app.desktop.forms.FormPosShop;
import com.github.ragudos.kompeter.app.desktop.forms.FormPosTransactions;
import com.github.ragudos.kompeter.app.desktop.forms.FormProfile;
import com.github.ragudos.kompeter.app.desktop.system.AllForms;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.system.FormManager;
import com.github.ragudos.kompeter.auth.Authentication;
import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.utilities.constants.Metadata;

import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.item.Item;
import raven.modal.drawer.item.MenuItem;
import raven.modal.drawer.menu.AbstractMenuElement;
import raven.modal.drawer.menu.MenuAction;
import raven.modal.drawer.menu.MenuEvent;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.menu.MenuOption.MenuOpenMode;
import raven.modal.drawer.menu.MenuStyle;
import raven.modal.drawer.renderer.DrawerCurvedLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.SimpleFooter;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;

public class KompeterDrawerBuilder extends SimpleDrawerBuilder {
    private static KompeterDrawerBuilder instance;

    public static KompeterDrawerBuilder getInstance() {
        if (instance == null) {
            instance = new KompeterDrawerBuilder();
        }

        return instance;
    }

    private static MenuOption createSimpleMenuOption() {
        final MenuOption menuOption = new MenuOption();

        final MenuItem[] items = new MenuItem[] { new Item("Profile", "user.svg", FormProfile.class),
                new Item("Point of Sale", "store.svg").subMenu(new Item("Shop", "shopping-cart.svg", FormPosShop.class))
                        .subMenu(new Item("Transactions", "circle-dollar-sign.svg", FormPosTransactions.class)),
                new Item("Inventory", "boxes.svg")
                        .subMenu(new Item("Browse Products", "package.svg", FormInventoryBrowseProducts.class))
                        .subMenu(new Item("Add Product", "plus.svg", FormInventoryAddProduct.class)),
                new Item("Monitoring", "chart-no-axes-combined.svg")
                        .subMenu(new Item("Sales", "badge-dollar-sign.svg", FormMonitoringSales.class))
                        .subMenu(new Item("Inventory", "boxes.svg", FormMonitoringInventory.class)),
                new Item("Logout", "logout.svg") };

        menuOption.setMenuStyle(new MenuStyle() {
            @Override
            public void styleMenu(final JComponent component) {
                component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
            }

            @Override
            public void styleMenuItem(final JButton menu, final int[] index, final boolean isMainItem) {
                menu.putClientProperty(FlatClientProperties.STYLE, "margin: -1, 0, -1, 0;");
                menu.setToolTipText("Navigate to " + menu.getText());
            }
        });

        menuOption.setMenuOpenMode(MenuOpenMode.COMPACT);
        menuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerCurvedLineStyle());
        menuOption.setMenuValidation(new KompeterMenuValidation());

        menuOption.addMenuEvent(new MenuEvent() {
            @Override
            public void selected(final MenuAction action, final int[] index) {
                final Class<?> itemClass = action.getItem().getItemClass();

                final int i = index[0];

                if (i == 4) {
                    action.consume();

                    final Optional<Form> currentForm = FormManager.FORMS.current();

                    if (currentForm.isPresent() && !currentForm.get().formBeforeLogout()) {
                        return;
                    }

                    try {
                        Authentication.signOut();
                    } catch (final AuthenticationException err) {
                        JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(), err.getMessage(),
                                "Sign Out Failure :(", JOptionPane.ERROR_MESSAGE);

                        return;
                    }

                    SwingUtilities.invokeLater(() -> {
                        FormManager.logout();
                    });

                    return;
                }

                if (itemClass == null || !Form.class.isAssignableFrom(itemClass)) {
                    action.consume();

                    return;
                }

                @SuppressWarnings("unchecked") // already has been checked above if it is a clazz that extends Form
                final Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
                final Form form = AllForms.getForm(formClass);

                final Optional<Form> currentForm = FormManager.FORMS.current();

                if (currentForm.isPresent() && !currentForm.get().formBeforeClose()) {
                    action.consume();

                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    FormManager.showForm(form);
                });
            }
        });

        menuOption.setMenus(items).setBaseIconPath(SVGIconUIColor.ICONS_BASE_PATH).setIconScale(1f);

        return menuOption;
    }

    private static String getDrawerBackgroundStyle() {
        return "" + "[light]background:tint($Panel.background,10%);" + "[dark]background:tint($Panel.background,5%);";
    }

    private final int SHADOW_SIZE = 12;

    private KompeterDrawerBuilder() {
        super(createSimpleMenuOption());
    }

    @Override
    public void build(final DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
    }

    @Override
    public AbstractMenuElement createFooter() {
        return new SimpleFooter(getSimpleFooterData());
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80 + SHADOW_SIZE;
    }

    @Override
    public int getDrawerWidth() {
        return 270 + SHADOW_SIZE;
    }

    @Override
    public int getOpenDrawerAt() {
        return 1000;
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData().setTitle("KomPeter").setDescription("Version " + Metadata.APP_VERSION);
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData().setDescription("A school project.");
    }

    @Override
    public boolean openDrawerAtScale() {
        return false;
    }
}
