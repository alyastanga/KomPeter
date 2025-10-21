/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.spinner.render;

import java.awt.*;

public interface SpinnerRender {
    int getInsets();

    boolean isDisplayStringAble();

    boolean isPaintComplete();

    void paintCompleteIndeterminate(Graphics2D g2, Component component, Rectangle rec, float last, float f, float p);

    void paintDeterminate(Graphics2D g2, Component component, Rectangle rec, float p);

    void paintIndeterminate(Graphics2D g2, Component component, Rectangle rec, float f);
}
