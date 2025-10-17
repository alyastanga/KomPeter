/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.factory;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.assets.AssetManager;
import javax.swing.JButton;
import org.jetbrains.annotations.NotNull;

public final class ButtonFactory {
    public static JButton createButton(
            @NotNull String text,
            @NotNull String iconName,
            @NotNull String actionCommand,
            @NotNull String styleClass) {
        JButton b = new JButton(text);

        b.setIconTextGap(12);
        b.setIcon(AssetManager.getOrLoadIcon("icons/" + iconName));
        b.setActionCommand(actionCommand);
        b.putClientProperty(FlatClientProperties.STYLE_CLASS, styleClass);
        b.setHorizontalAlignment(JButton.LEFT);

        return b;
    }
}
