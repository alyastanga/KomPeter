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
import com.github.ragudos.kompeter.app.desktop.assets.SVGIconUIColor;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import org.jetbrains.annotations.NotNull;

public final class ButtonFactory {
    public static JButton createButton(@NotNull String text, @NotNull String styleClass) {
        JButton b = new JButton(text);

        b.putClientProperty(FlatClientProperties.STYLE_CLASS, styleClass);

        return b;
    }

    public static JButton createButton(
            @NotNull String text,
            @NotNull String iconName,
            @NotNull String actionCommand,
            @NotNull String styleClass) {
        JButton b = new JButton(text);

        SVGIconUIColor icon = AssetManager.getOrLoadIcon(iconName, 1f, "foreground.background");

        b.setIconTextGap(12);
        b.setIcon(icon);
        b.setActionCommand(actionCommand);
        b.putClientProperty(FlatClientProperties.STYLE_CLASS, styleClass);
        b.setHorizontalAlignment(JButton.LEFT);

        b.addItemListener(
                (e) -> {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        icon.setColorKey("foreground.primary");
                    } else {
                        icon.setColorKey("foreground.background");
                    }
                });

        return b;
    }
}
