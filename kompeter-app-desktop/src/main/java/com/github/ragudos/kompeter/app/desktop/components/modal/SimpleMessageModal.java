/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.components.modal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.listener.ModalCallback;
import raven.modal.toast.ToastPanel;

public class SimpleMessageModal extends SimpleModalBorder {

    private static Component createMessage(Type type, String message) {
        JTextArea text = new JTextArea(message);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
            }
        });
        String gap = type == Type.DEFAULT ? "30" : "62";
        text.putClientProperty(FlatClientProperties.STYLE,
                "" + "border:0," + gap + ",10,30;" + "[light]foreground:lighten($Label.foreground,20%);"
                        + "[dark]foreground:darken($Label.foreground,20%);");
        return text;
    }

    private Component titleComponent;

    private final Type type;

    public SimpleMessageModal(Type type, Component messageComponent, Component titleComponent, int optionType,
            ModalCallback callback) {
        super(messageComponent, null, optionType, callback);
        this.titleComponent = titleComponent;
        this.type = type;
    }

    public SimpleMessageModal(Type type, Component messageComponent, String title, int optionType,
            ModalCallback callback) {
        super(messageComponent, title, optionType, callback);
        this.type = type;
    }

    public SimpleMessageModal(Type type, String message, Component titleComponent, int optionType,
            ModalCallback callback) {
        this(type, createMessage(type, message), titleComponent, optionType, callback);
    }

    public SimpleMessageModal(Type type, String message, String title, int optionType, ModalCallback callback) {
        this(type, createMessage(type, message), title, optionType, callback);
    }

    @Override
    protected JButton createButtonOption(Option option) {
        JButton button;
        if (option.getType() != 0) {
            button = super.createButtonOption(option);
        } else {
            button = new JButton(option.getText());
            button.addActionListener(e -> doAction(option.getType()));
        }
        String[] colors = getColorKey(type);
        if (option.getType() == 0) {
            button.putClientProperty(FlatClientProperties.STYLE,
                    "" + "arc:999;" + "margin:3,33,3,33;" + "borderWidth:0;" + "focusWidth:0;" + "innerFocusWidth:0;"
                            + "default.borderWidth:0;" + "foreground:$Button.default.foreground;" + "[light]background:"
                            + colors[0] + ";" + "[dark]background:" + colors[1] + ";");
        } else {
            button.putClientProperty(FlatClientProperties.STYLE,
                    "" + "arc:999;" + "margin:3,33,3,33;" + "borderWidth:1;" + "focusWidth:0;" + "innerFocusWidth:1;"
                            + "background:null;" + "[light]borderColor:" + colors[0] + ";" + "[dark]borderColor:"
                            + colors[1] + ";" + "[light]focusedBorderColor:" + colors[0] + ";"
                            + "[dark]focusedBorderColor:" + colors[1] + ";" + "[light]focusColor:" + colors[0] + ";"
                            + "[dark]focusColor:" + colors[1] + ";" + "[light]hoverBorderColor:" + colors[0] + ";"
                            + "[dark]hoverBorderColor:" + colors[1] + ";" + "[light]foreground:" + colors[0] + ";"
                            + "[dark]foreground:" + colors[1] + ";");
        }
        return button;
    }

    protected Icon createIcon(Type type) {
        ToastPanel.ThemesData data = Toast.getThemesData().get(asToastType(type));
        FlatSVGIcon icon = new FlatSVGIcon(data.getIcon(), 0.45f);
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(Color.decode("#969696"), Color.decode(data.getColors()[0]), Color.decode(data.getColors()[1]));
        icon.setColorFilter(colorFilter);
        return icon;
    }

    @Override
    protected JComponent createOptionButton(Option[] optionsType) {
        JPanel panel = (JPanel) super.createOptionButton(optionsType);
        if (panel == null)
            return null;

        // modify layout option
        if (panel.getLayout() instanceof MigLayout) {
            MigLayout layout = (MigLayout) panel.getLayout();
            layout.setColumnConstraints("[]12[]");
        }
        return panel;
    }

    @Override
    protected JComponent createTitleComponent(String title) {
        if (titleComponent != null && titleComponent instanceof JComponent) {
            return (JComponent) titleComponent;
        }
        if (type == Type.DEFAULT) {
            return super.createTitleComponent(title);
        }
        Icon icon = createIcon(type);
        JLabel label = (JLabel) super.createTitleComponent(title);
        label.setIconTextGap(10);
        label.setIcon(icon);
        return label;
    }

    protected String[] getColorKey(Type type) {
        if (type == Type.DEFAULT) {
            // use accent color as default type
            return new String[]{"$Component.accentColor", "$Component.accentColor"};
        }
        ToastPanel.ThemesData data = Toast.getThemesData().get(asToastType(type));
        return data.getColors();
    }

    private Toast.Type asToastType(Type type) {
        switch (type) {
            case DEFAULT :
                return Toast.Type.DEFAULT;
            case SUCCESS :
                return Toast.Type.SUCCESS;
            case INFO :
                return Toast.Type.INFO;
            case WARNING :
                return Toast.Type.WARNING;
            default :
                return Toast.Type.ERROR;
        }
    }

    public enum Type {
        DEFAULT, ERROR, INFO, SUCCESS, WARNING
    }
}
