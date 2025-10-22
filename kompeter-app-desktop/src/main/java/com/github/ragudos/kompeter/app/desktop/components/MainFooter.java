/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneComponent;
import com.github.ragudos.kompeter.app.desktop.navigation.SceneNavigator;
import com.github.ragudos.kompeter.utilities.HtmlUtils;
import com.github.ragudos.kompeter.utilities.constants.Metadata;
import com.github.ragudos.kompeter.utilities.platform.SystemInfo;

import net.miginfocom.swing.MigLayout;

public class MainFooter implements SceneComponent {
    private final JLabel appVersionJLabel;
    private final JLabel breadcrumbsPathJLabel = new JLabel();
    private final JPanel container = new JPanel(
            new MigLayout("gapx 9px,insets 3 16 3 16, al trailing center, height 32!", "[]push[]push[][][]", "fill"));

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final JLabel javaLabel;
    private final ComponentListener listener = new ComponentAdapter() {
        public void componentResized(java.awt.event.ComponentEvent e) {
            MigLayout layout = (MigLayout) container.getLayout();
            int width = view.getWidth();

            boolean showBreadCrumbs = width > 500;
            boolean showJava = width > 650;
            boolean showOs = width > 850;

            updateComponent(layout, breadcrumbsPathJLabel, showBreadCrumbs, "cell 1 0", 1);
            // 2 because they are added and removed one-by-one, and 2 is the index where
            // they are placed before removal
            updateComponent(layout, javaLabel, showJava, "cell 2 0", 2);
            updateComponent(layout, osLabel, showOs, "cell 2 0", 2);
        };
    };
    private final MemoryBar memoryBar = new MemoryBar();
    private final Consumer<String> navigationListenerClass = new Consumer<String>() {
        @Override
        public void accept(String sceneName) {
            breadcrumbsPathJLabel.setText(HtmlUtils.wrapInHtml(sceneName));
        }
    };

    private final JLabel osLabel;

    private final JPanel view = new JPanel(new MigLayout("flowy, insets 0", "[grow]", "[top, grow 0][grow]"));

    public MainFooter() {
        appVersionJLabel = new JLabel(HtmlUtils.wrapInHtml(Metadata.APP_TITLE + ": v" + Metadata.APP_VERSION));
        String javaString = SystemInfo.JAVA_VENDOR + " v" + SystemInfo.JAVA_VERSION;
        javaLabel = new JLabel(HtmlUtils.wrapInHtml(String.format("Java %s", javaString)));
        String osString = SystemInfo.OS_NAME + " v" + SystemInfo.OS_VERSION;
        osLabel = new JLabel(HtmlUtils.wrapInHtml(String.format("%s", osString)));

        appVersionJLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 9));
        appVersionJLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        appVersionJLabel.setIcon(AssetManager.getOrLoadIcon("git-commit-horizontal.svg", 0.75f, "color.muted"));
        javaLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        javaLabel.setIcon(AssetManager.getOrLoadIcon("coffee.svg", 0.75f, "color.muted"));
        osLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        osLabel.setIcon(AssetManager.getOrLoadIcon("cpu.svg", 0.75f, "color.muted"));

        breadcrumbsPathJLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 9));

        container.add(appVersionJLabel);
        container.add(breadcrumbsPathJLabel);
        container.add(osLabel);
        container.add(javaLabel);

        container.add(new JSeparator(JSeparator.VERTICAL));
        container.add(memoryBar, "height 32!");

        view.add(new JSeparator(JSeparator.HORIZONTAL), "grow, height 2!");
        view.add(container, "grow");
    }

    @Override
    public void destroy() {
        if (!initialized.get()) {
            return;
        }

        view.removeAll();
        view.removeComponentListener(listener);
        memoryBar.uninstallMemoryBar();
        SceneNavigator.getInstance().unsubscribe(navigationListenerClass);

        initialized.set(false);
    }

    @Override
    public void initialize() {
        if (initialized.get()) {
            return;
        }

        memoryBar.installMemoryBar();

        view.addComponentListener(listener);

        SceneNavigator.getInstance().subscribe(navigationListenerClass);

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

    private void updateComponent(MigLayout layout, JComponent comp, boolean shouldShow, String cell, int index) {
        if (shouldShow && !comp.isVisible()) {
            container.add(comp, cell, index);
            comp.setVisible(true);

            container.repaint();
            container.revalidate();
        } else if (!shouldShow && comp.isVisible()) {
            container.remove(comp);
            comp.setVisible(false);

            container.repaint();
            container.revalidate();
        }
    }
}
