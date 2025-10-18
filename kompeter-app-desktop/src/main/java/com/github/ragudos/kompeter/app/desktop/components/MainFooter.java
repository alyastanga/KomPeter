/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.utilities.constants.Metadata;
import com.github.ragudos.kompeter.utilities.platform.SystemInfo;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

public class MainFooter implements SceneComponent {
    private final JPanel view = new JPanel(new MigLayout("flowy", "[grow]", "[grow]"));
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final JLabel breadcrumbsPathJLabel = new JLabel();
    private final MemoryBar memoryBar = new MemoryBar();

    private final Consumer<String> navigationListenerClass =
            new Consumer<String>() {
                @Override
                public void accept(String sceneName) {
                    breadcrumbsPathJLabel.setText(sceneName);
                }
            };

    public MainFooter() {}

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        memoryBar.installMemoryBar();

        JPanel container =
                new JPanel(
                        new MigLayout(
                                "gapx 10px,insets 3 n 3 n, al trailing center, height 32!",
                                "[]push[]push[][][]",
                                "fill"));

        JLabel appVersionJLabel = new JLabel(Metadata.APP_TITLE + ": v" + Metadata.APP_VERSION);
        String javaString = SystemInfo.JAVA_VENDOR + " v" + SystemInfo.JAVA_VERSION;
        JLabel javaLabel = new JLabel(String.format("Running on: Java %s", javaString));
        String osString = SystemInfo.OS_NAME + " v" + SystemInfo.OS_VERSION;
        JLabel osLabel = new JLabel(String.format("Using: %s", osString));

        appVersionJLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        appVersionJLabel.setIcon(
                AssetManager.getOrLoadIcon("git-commit-horizontal.svg", 1f, "color.muted"));
        javaLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        javaLabel.setIcon(AssetManager.getOrLoadIcon("coffee.svg", 1f, "color.muted"));
        osLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        osLabel.setIcon(AssetManager.getOrLoadIcon("cpu.svg", 1f, "color.muted"));

        view.add(new JSeparator(JSeparator.HORIZONTAL), "grow");

        container.add(appVersionJLabel);
        container.add(breadcrumbsPathJLabel);
        container.add(osLabel);
        container.add(javaLabel);

        container.add(new JSeparator(JSeparator.VERTICAL));
        container.add(memoryBar);

        SceneNavigator.getInstance().subscribe(navigationListenerClass);

        view.add(container, "grow");

        initialized.set(true);
    }

    @Override
    public void destroy() {
        if (!initialized.get()) {
            return;
        }

        view.removeAll();
        memoryBar.uninstallMemoryBar();
        SceneNavigator.getInstance().unsubscribe(navigationListenerClass);

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
}
