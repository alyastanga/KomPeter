/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.UIScale;

// @see
// https://github.com/DJ-Raven/swing-modal-dialog/blob/main/demo/src/main/java/raven/modal/demo/component/RefreshLine.java#L11
public class RefreshLine extends JPanel {
    private float animate;
    private Animator animator;

    public RefreshLine() {
        animator = new Animator(500, new Animator.TimingTarget() {
            @Override
            public void end() {
                animate = 0f;
                repaint();
            }

            @Override
            public void timingEvent(float v) {
                animate = v;
                RefreshLine.this.repaint();
            }
        });

        animator.setInterpolator(CubicBezierEasing.EASE_OUT);
    }

    public void refresh() {
        if (animator.isRunning()) {
            animator.stop();
        }

        animate = 0f;
        animator.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float pad = UIScale.scale(5f);
        float width = getWidth() - (pad * 2);
        float height = getHeight();

        g2.setColor(UIManager.getColor("Component.accentColor"));
        g2.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        g2.fill(new RoundRectangle2D.Float(pad, 0, width * animate, height, height, height));
        g2.dispose();
    }
}
