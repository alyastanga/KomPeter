package com.github.ragudos.kompeter.app.desktop.components.buttons;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JButton;

public class BadgeButton extends JButton {
    private int badgeNumber = 0;

    public BadgeButton(String text) {
        super(text);
    }

    public BadgeButton(Icon icon) {
        super(icon);

    }

    public BadgeButton(String text, Icon icon) {
        super(text, icon);
    }

    public void setBadgeNumber(int number) {
        this.badgeNumber = number;
        repaint();
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (badgeNumber > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Badge size
            int badgeSize = 18;
            int x = getWidth() - badgeSize - 2;
            int y = 2;

            // Draw red circle
            g2.setColor(Color.RED);
            g2.fillOval(x, y, badgeSize, badgeSize);

            // Draw number
            g2.setColor(Color.WHITE);
            g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
            String text = (badgeNumber > 99 ? "99+" : String.valueOf(badgeNumber));

            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            g2.drawString(text, x + (badgeSize - textWidth) / 2, y + (badgeSize + textHeight) / 2 - 2);
            g2.dispose();
        }
    }
}
