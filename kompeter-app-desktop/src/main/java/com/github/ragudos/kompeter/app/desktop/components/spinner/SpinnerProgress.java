/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.spinner;

import javax.swing.*;

public class SpinnerProgress extends JProgressBar {

    private int horizontalAlignment = CENTER;

    private int horizontalTextPosition = TRAILING;
    private Icon icon;

    private int iconTextGap = 4;
    private int space = 10;

    private int verticalAlignment = CENTER;
    private int verticalTextPosition = CENTER;

    public SpinnerProgress() {
        init();
    }

    public SpinnerProgress(Icon icon) {
        this();
        this.icon = icon;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    public Icon getIcon() {
        return icon;
    }

    public int getIconTextGap() {
        return iconTextGap;
    }

    public int getSpace() {
        return space;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    public void setHorizontalAlignment(int alignment) {
        if (this.horizontalAlignment != alignment) {
            this.horizontalAlignment = alignment;
            revalidate();
        }
    }

    public void setHorizontalTextPosition(int textPosition) {
        if (this.horizontalTextPosition != textPosition) {
            this.horizontalTextPosition = textPosition;
            revalidate();
        }
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint();
        revalidate();
    }

    public void setIconTextGap(int iconTextGap) {
        if (this.iconTextGap != iconTextGap) {
            this.iconTextGap = iconTextGap;
            revalidate();
        }
    }

    public void setSpace(int space) {
        if (this.space != space) {
            this.space = space;
            revalidate();
        }
    }

    public void setVerticalAlignment(int alignment) {
        if (this.verticalAlignment != alignment) {
            this.verticalAlignment = alignment;
            revalidate();
        }
    }

    public void setVerticalTextPosition(int textPosition) {
        if (this.verticalTextPosition != textPosition) {
            this.verticalTextPosition = textPosition;
            revalidate();
        }
    }

    @Override
    public void updateUI() {
        setUI(new SpinnerProgressUI());
    }

    private void init() {
        setUI(new SpinnerProgressUI());
    }
}
