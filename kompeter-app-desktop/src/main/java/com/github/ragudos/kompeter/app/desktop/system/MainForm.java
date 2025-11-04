/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.system;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.github.ragudos.kompeter.app.desktop.components.MemoryBar;
import com.github.ragudos.kompeter.app.desktop.components.RefreshLine;
import com.github.ragudos.kompeter.app.desktop.components.icons.SVGIconUIColor;
import com.github.ragudos.kompeter.utilities.constants.Metadata;
import com.github.ragudos.kompeter.utilities.platform.SystemInfo;

import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;

public class MainForm extends JPanel {
    private static MemoryBar memoryBar;

    public static MemoryBar getMemoryBar() {
        if (memoryBar == null) {
            memoryBar = new MemoryBar();
        }

        return memoryBar;
    }

    private JButton buttonRedo;

    private JButton buttonRefresh;

    private JButton buttonUndo;

    private JPanel mainPanel;

    private RefreshLine refreshLine;

    public MainForm() {
        init();
    }

    public void refresh() {
        refreshLine.refresh();
    }

    public void setForm(Form form) {
        buttonUndo.setEnabled(FormManager.FORMS.isUndoAble());
        buttonRedo.setEnabled(FormManager.FORMS.isRedoAble());

        if (mainPanel.getComponentOrientation().isLeftToRight() != form.getComponentOrientation().isLeftToRight()) {
            applyComponentOrientation(mainPanel.getComponentOrientation());
        }

        SwingUtilities.invokeLater(() -> {
            mainPanel.removeAll();
            mainPanel.add(form);
            mainPanel.repaint();
            mainPanel.revalidate();
        });
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(
                new MigLayout("insets 1 n 1 n,al trailing center,gapx 10,height 30!", "[]push[][]", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "" + "[light]background:tint($Panel.background,20%);" + "[dark]background:tint($Panel.background,5%);");

        JLabel lbDemoVersion = new JLabel(Metadata.APP_TITLE + " v" + Metadata.APP_VERSION);
        lbDemoVersion.putClientProperty(FlatClientProperties.STYLE, "" + "foreground:$Label.disabledForeground;");
        lbDemoVersion.setIcon(new SVGIconUIColor("git-commit-horizontal.svg", 1f, "Label.disabledForeground"));
        panel.add(lbDemoVersion);

        String javaVendor = SystemInfo.JAVA_VENDOR;

        if (javaVendor.equals("Oracle Corporation")) {
            javaVendor = "";
        }

        String java = javaVendor + " v" + SystemInfo.JAVA_VERSION;
        String st = "Running on: Java %s";
        JLabel lbJava = new JLabel(String.format(st, java));
        lbJava.putClientProperty(FlatClientProperties.STYLE, "" + "foreground:$Label.disabledForeground;");
        lbJava.setIcon(new SVGIconUIColor("coffee.svg", 1f, "Label.disabledForeground"));
        panel.add(lbJava);

        panel.add(new JSeparator(JSeparator.VERTICAL));

        getMemoryBar().installMemoryBar();
        panel.add(getMemoryBar());

        return panel;
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push", "[fill]"));
        JToolBar toolBar = new JToolBar();
        JButton buttonDrawer = new JButton(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "menu.svg", 0.75f));

        buttonUndo = new JButton(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "undo.svg", 0.75f));
        buttonRedo = new JButton(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "redo.svg", 0.75f));
        buttonRefresh = new JButton(new FlatSVGIcon(SVGIconUIColor.ICONS_BASE_PATH + "refresh.svg", 0.75f));

        buttonDrawer.addActionListener(e -> {
            if (Drawer.isOpen()) {
                Drawer.showDrawer();
            } else {
                Drawer.toggleMenuOpenMode();
            }
        });

        buttonUndo.addActionListener(e -> FormManager.undo());
        buttonRedo.addActionListener(e -> FormManager.redo());
        buttonRefresh.addActionListener(e -> FormManager.refresh());

        toolBar.add(buttonDrawer);
        toolBar.add(buttonUndo);
        toolBar.add(buttonRedo);
        toolBar.add(buttonRefresh);
        panel.add(toolBar);

        return panel;
    }

    private Component createMain() {
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    private JPanel createRefreshLine() {
        refreshLine = new RefreshLine();
        return refreshLine;
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 0,gap 0", "[fill]", "[][][fill,grow][]"));
        add(createHeader());
        add(createRefreshLine(), "height 3!");
        add(createMain());
        add(new JSeparator(), "height 2!");
        add(createFooter());
    }
}
