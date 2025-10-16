/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.utilities;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeListener;

public class KompeterSwingUtilities {
    public static void removeAllListeners(AbstractButton button) {
        // Action listeners
        for (ActionListener l : button.getActionListeners()) {
            button.removeActionListener(l);
        }

        // Item listeners (for toggle buttons, checkboxes, radios)
        for (ItemListener l : button.getItemListeners()) {
            button.removeItemListener(l);
        }

        // Change listeners (for model changes)
        for (ChangeListener l : button.getChangeListeners()) {
            button.removeChangeListener(l);
        }

        // Mouse listeners
        for (MouseListener l : button.getMouseListeners()) {
            button.removeMouseListener(l);
        }

        // Mouse motion listeners
        for (MouseMotionListener l : button.getMouseMotionListeners()) {
            button.removeMouseMotionListener(l);
        }

        // Focus listeners
        for (FocusListener l : button.getFocusListeners()) {
            button.removeFocusListener(l);
        }

        // Key listeners
        for (KeyListener l : button.getKeyListeners()) {
            button.removeKeyListener(l);
        }

        // Property change listeners
        for (PropertyChangeListener l : button.getPropertyChangeListeners()) {
            button.removePropertyChangeListener(l);
        }

        // Ancestor listeners (less common, but possible)
        for (AncestorListener l : button.getAncestorListeners()) {
            button.removeAncestorListener(l);
        }
    }
}
