/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import com.github.ragudos.kompeter.app.desktop.components.factory.ButtonFactory;
import com.github.ragudos.kompeter.app.desktop.listeners.ButtonSceneNavigationActionListener;
import com.github.ragudos.kompeter.app.desktop.navigation.ParsedSceneName;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.app.desktop.scenes.SceneNames;
import com.github.ragudos.kompeter.app.desktop.utilities.KompeterSwingUtilities;
import com.github.ragudos.kompeter.auth.Session;
import com.github.ragudos.kompeter.auth.SessionManager;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class MainSidebar implements SceneComponent {
    private final JPanel view;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private ButtonGroup buttonGroup = new ButtonGroup();
    private final HashMap<String, JButton> buttons = new HashMap<>();
    private final Consumer<String> navigationListenerClass = new Consumer<String>() {
        @Override
        public void accept(String sceneName) {
            int firstSeparatorOccurence = sceneName.indexOf(ParsedSceneName.SEPARATOR);
            int secondSeparatorOcurrence = sceneName.indexOf(ParsedSceneName.SEPARATOR, firstSeparatorOccurence + 1);

            JButton pressedButton = buttons.get(
                    secondSeparatorOcurrence == -1 ? sceneName : sceneName.subSequence(0, secondSeparatorOcurrence));

            if (pressedButton == null) {
                return;
            }

            buttonGroup.setSelected(pressedButton.getModel(), true);
            pressedButton.requestFocusInWindow();
        }
    };

    public MainSidebar() {
        view = new JPanel();
    }

    private void createLogisticsOfficerSidebar() {
        JButton inventoryButton = ButtonFactory.createButton(
                "Inventory",
                "package.svg",
                SceneNames.HomeScenes.InventoryScenes.INVENTORY_SCENE,
                "ghost");

        buttons.put(inventoryButton.getActionCommand(), inventoryButton);

        view.add(inventoryButton, "growx,h 48!");
    }

    private void createPurchasingOfficerSidebar() {
        JButton inventoryButton = ButtonFactory.createButton(
                "Inventory",
                "package.svg",
                SceneNames.HomeScenes.InventoryScenes.INVENTORY_SCENE,
                "ghost");
        JButton monitoringButton = ButtonFactory.createButton(
                "Monitoring",
                "chart-no-axes-combined.svg",
                SceneNames.HomeScenes.MonitoringScenes.MONITORING_SCENE,
                "ghost");

        buttons.put(inventoryButton.getActionCommand(), inventoryButton);
        buttons.put(monitoringButton.getActionCommand(), monitoringButton);

        view.add(inventoryButton, "growx,h 48!");
        view.add(monitoringButton, "growx,h 48!");
    }

    private void createClerkSidebar() {
        JButton pointOfSaleButton = ButtonFactory.createButton(
                "Point Of Sale",
                "boxes.svg",
                SceneNames.HomeScenes.PointOfSaleScenes.POINT_OF_SALE_SCENE,
                "ghost");

        buttons.put(pointOfSaleButton.getActionCommand(), pointOfSaleButton);

        view.add(pointOfSaleButton, "growx,h 48!");
    }

    private void createAdminSidebar() {
        JButton pointOfSaleButton = ButtonFactory.createButton(
                "Point Of Sale",
                "boxes.svg",
                SceneNames.HomeScenes.PointOfSaleScenes.POINT_OF_SALE_SCENE,
                "ghost");
        JButton inventoryButton = ButtonFactory.createButton(
                "Inventory",
                "package.svg",
                SceneNames.HomeScenes.InventoryScenes.INVENTORY_SCENE,
                "ghost");
        JButton monitoringButton = ButtonFactory.createButton(
                "Monitoring",
                "chart-no-axes-combined.svg",
                SceneNames.HomeScenes.MonitoringScenes.MONITORING_SCENE,
                "ghost");

        buttons.put(inventoryButton.getActionCommand(), inventoryButton);
        buttons.put(monitoringButton.getActionCommand(), monitoringButton);
        buttons.put(pointOfSaleButton.getActionCommand(), pointOfSaleButton);

        view.add(pointOfSaleButton, "growx,h 48!");
        view.add(inventoryButton, "growx,h 48!");
        view.add(monitoringButton, "growx,h 48!");
    }

    private void createRoleLessSidebar() {
    }

    @Override
    public void destroy() {
        if (!initialized.get()) {
            return;
        }

        SceneNavigator.getInstance().unsubscribe(navigationListenerClass);
        buttonGroup.clearSelection();
        buttons.values().forEach((button) -> KompeterSwingUtilities.removeAllListeners(button));
        buttons.clear();
        buttonGroup = new ButtonGroup();
        view.removeAll();
        initialized.set(false);
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        SceneNavigator.getInstance().subscribe(navigationListenerClass);

        view.setLayout(new MigLayout("insets 9, fillx, flowy", "[grow, center]", "[top]"));

        Session session = SessionManager.getInstance().session();

        JButton profileButton = ButtonFactory.createButton(
                "Profile",
                "user-circle.svg",
                SceneNames.HomeScenes.ProfileScenes.PROFILE_SCENE,
                "ghost");
        JButton settingsButton = ButtonFactory.createButton(
                "Settings",
                "settings.svg",
                SceneNames.HomeScenes.SettingsScenes.SETTINGS_SCENE,
                "ghost");

        buttons.put(profileButton.getActionCommand(), profileButton);
        buttons.put(settingsButton.getActionCommand(), settingsButton);

        view.add(profileButton, "growx,h 48!");

        if (session == null) {
            createAdminSidebar();
        } else if (session.user().isPurchasingOfficer()) {
            createPurchasingOfficerSidebar();
        } else if (session.user().isLogistics()) {
            createLogisticsOfficerSidebar();
        } else if (session.user().isClerk()) {
            createClerkSidebar();
        } else if (session.user().isAdmin()) {
            createAdminSidebar();
        } else if (session.user().isRoleLess()) {
            createRoleLessSidebar();
        }

        view.add(settingsButton, "growx,h 48!");

        buttons
                .values()
                .forEach(
                        (button) -> {
                            buttonGroup.add(button);
                            button.addActionListener(ButtonSceneNavigationActionListener.LISTENER);
                        });

        initialized.set(true);
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
