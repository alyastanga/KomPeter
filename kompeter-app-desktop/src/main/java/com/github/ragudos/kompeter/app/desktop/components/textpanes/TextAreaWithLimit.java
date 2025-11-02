/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.textpanes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextAreaWithLimit extends JTextArea {
    private final int limit;

    public TextAreaWithLimit(final int limit) {
        this.limit = limit;

        ((AbstractDocument) getDocument()).setDocumentFilter(new LimitFilter());
    }

    public int currentTextCount() {
        return getText().length();
    }

    public int limit() {
        return limit;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        // Draw live counter
        final int count = getText().length();
        final String counterText = count + " / " + limit;

        final Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final java.awt.FontMetrics fm = g2.getFontMetrics();
        final int x = getWidth() - fm.stringWidth(counterText) - 6;
        final int y = getHeight() - fm.getDescent() - 4;

        final float ratio = (float) count / limit;
        Color color;

        if (ratio < 0.7f)
            color = UIManager.getColor("Label.disabledForeground");
        else if (ratio < 1.0f)
            color = UIManager.getColor("Component.accentColor");
        else
            color = UIManager.getColor("Component.errorFocusColor");

        if (color == null)
            color = Color.GRAY;

        g2.setFont(getFont().deriveFont(Font.PLAIN, getFont().getSize() - 1f));
        g2.setColor(color);
        g2.drawString(counterText, x, y);

        g2.dispose();
    }

    private class LimitFilter extends DocumentFilter {
        @Override
        public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr)
                throws BadLocationException {
            if (string == null) {
                return;
            }

            final int newLen = fb.getDocument().getLength() + string.length();

            if (newLen <= limit) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();

                final int allowed = limit - fb.getDocument().getLength();

                if (allowed > 0) {
                    super.insertString(fb, offset, string.substring(0, allowed), attr);
                }
            }

            repaint();
        }

        @Override
        public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
            super.remove(fb, offset, length);
            repaint();
        }

        @Override
        public void replace(final FilterBypass fb, final int offset, final int length, final String text,
                final AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }

            final int newLen = fb.getDocument().getLength() - length + text.length();

            if (newLen <= limit) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
                final int allowed = limit - (fb.getDocument().getLength() - length);
                if (allowed > 0)
                    super.replace(fb, offset, length, text.substring(0, allowed), attrs);
            }
            repaint(); // update counter
        }
    }
}
