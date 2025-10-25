/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.menu;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.app.desktop.components.modal.SimpleMessageModal;
import com.github.ragudos.kompeter.app.desktop.forms.FormDashboard;
import com.github.ragudos.kompeter.app.desktop.forms.FormInventoryBrowseProducts;
import com.github.ragudos.kompeter.app.desktop.forms.FormInventoryTransactions;
import com.github.ragudos.kompeter.app.desktop.forms.FormMonitoringInventory;
import com.github.ragudos.kompeter.app.desktop.forms.FormMonitoringSales;
import com.github.ragudos.kompeter.app.desktop.forms.FormMonitoringSummary;
import com.github.ragudos.kompeter.app.desktop.forms.FormPosShop;
import com.github.ragudos.kompeter.app.desktop.forms.FormPosTransactions;
import com.github.ragudos.kompeter.app.desktop.forms.FormProfile;
import com.github.ragudos.kompeter.app.desktop.forms.FormSettings;
import com.github.ragudos.kompeter.app.desktop.forms.FormUsers;
import com.github.ragudos.kompeter.app.desktop.system.AllForms;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.system.FormManager;
import com.github.ragudos.kompeter.auth.Authentication;
import com.github.ragudos.kompeter.auth.Authentication.AuthenticationException;
import com.github.ragudos.kompeter.utilities.constants.Metadata;

import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
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
        MenuOption menuOption = new MenuOption();

        MenuItem[] items = new MenuItem[] { new Item("Profile", "user.svg", FormProfile.class),
                new Item("Dashboard", "circle-gauge.svg", FormDashboard.class),
                new Item("Point of Sale", "store.svg").subMenu(new Item("Shop", "shopping-cart.svg", FormPosShop.class))
                        .subMenu(new Item("Transactions", "circle-dollar-sign.svg", FormPosTransactions.class)),
                new Item("Inventory", "boxes.svg")
                        .subMenu(new Item("Browse Products", "package.svg", FormInventoryBrowseProducts.class))
                        .subMenu(new Item("Transactions", "circle-dollar-sign.svg", FormInventoryTransactions.class)),
                new Item("Monitoring", "chart-no-axes-combined.svg")
                        .subMenu(new Item("Summary", "notepad-text.svg", FormMonitoringSummary.class))
                        .subMenu(new Item("Sales", "badge-dollar-sign.svg", FormMonitoringSales.class))
                        .subMenu(new Item("Inventory", "boxes.svg", FormMonitoringInventory.class))
                        .subMenu(new Item("Audits", "notepad-text.svg")),
                new Item("Users", "users.svg", FormUsers.class),
                new Item("Settings", "settings.svg", FormSettings.class), new Item("Logout", "logout.svg") };

        menuOption.setMenuStyle(new MenuStyle() {
            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
            }

            @Override
            public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
                menu.putClientProperty(FlatClientProperties.STYLE, "margin: -1, 0, -1, 0;");
            }
        });

        menuOption.setMenuOpenMode(MenuOpenMode.COMPACT);
        menuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerCurvedLineStyle());
        menuOption.setMenuValidation(new KompeterMenuValidation());

        menuOption.addMenuEvent(new MenuEvent() {
            @Override
            public void selected(MenuAction action, int[] index) {
                Class<?> itemClass = action.getItem().getItemClass();

                int i = index[0];

                if (i == 7) {
                    try {
                        Authentication.signOut();
                    } catch (AuthenticationException err) {
                        ModalDialog.showModal(KompeterDesktopApp.getRootFrame(),
                                new SimpleMessageModal(SimpleMessageModal.Type.ERROR, err.getMessage(),
                                        "Sign Out Failure :(", SimpleModalBorder.CLOSE_OPTION, null));

                        return;
                    }

                    action.consume();
                    FormManager.logout();
                }

                if (itemClass == null || !Form.class.isAssignableFrom(itemClass)) {
                    action.consume();

                    return;
                }

                System.out.println(action);

                @SuppressWarnings("unchecked") // already has been checked above if it is a clazz that extends Form
                Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
                FormManager.showForm(AllForms.getForm(formClass));
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
    public void build(DrawerPanel drawerPanel) {
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
