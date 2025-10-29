/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.system;

import javax.swing.JFrame;

import com.github.ragudos.kompeter.app.desktop.forms.FormProfile;
import com.github.ragudos.kompeter.app.desktop.forms.auth.FormAuthWelcome;
import com.github.ragudos.kompeter.app.desktop.menu.KompeterDrawerBuilder;
import com.github.ragudos.kompeter.app.desktop.utilities.UndoRedo;
import com.github.ragudos.kompeter.auth.SessionManager;

import raven.modal.Drawer;

public class FormManager {
    public static final UndoRedo<Form> AUTH_FORMS = new UndoRedo<>();
    public static final UndoRedo<Form> FORMS = new UndoRedo<>();
    private static JFrame frame;
    private static MainAuthForm mainAuthForm;
    private static MainForm mainForm;
    private static FormAuthWelcome welcomeAuthForm;

    public static JFrame getFrame() {
        return frame;
    }

    public static void install(JFrame f) {
        frame = f;
        install();

        if (SessionManager.getInstance().session() != null) {
            login();
        } else {
            logout();
        }
    }

    public static void login() {
        KompeterDrawerBuilder.getInstance().rebuildMenu();
        Drawer.setVisible(true);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(getMainForm());

        Drawer.setSelectedItemClass(FormProfile.class);

        AUTH_FORMS.clear();

        frame.repaint();
        frame.revalidate();
    }

    public static void logout() {
        Drawer.setVisible(false);

        MainForm.getMemoryBar().uninstallMemoryBar();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(getMainAuthForm());
        frame.getContentPane().add(getMainAuthForm());

        getMainAuthForm().setForm(getWelcome());

        FORMS.clear();
        AllForms.clear();

        frame.repaint();
        frame.revalidate();
    }

    public static void redo() {
        if (FORMS.isRedoAble()) {
            if (FORMS.current().isPresent() && !FORMS.current().get().formBeforeClose()) {
                return;
            }

            Form form = FORMS.redo().get();

            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    public static void refresh() {
        if (!FORMS.current().isEmpty()) {
            FORMS.current().get().formRefresh();
            mainForm.refresh();
        }
    }

    public static void showAuthForm(Form form) {
        if (AUTH_FORMS.current().isEmpty() || form != AUTH_FORMS.current().get()) {
            AUTH_FORMS.add(form);
            form.formCheck();
            form.formOpen();
            mainAuthForm.setForm(form);
            form.formAfterOpen();
        }
    }

    public static void showForm(Form form) {
        if (FORMS.current().isEmpty() || form != FORMS.current().get()) {
            FORMS.add(form);
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
            mainForm.refresh();
            form.formAfterOpen();
        }
    }

    public static void undo() {
        if (FORMS.isUndoAble()) {
            if (FORMS.current().isPresent() && !FORMS.current().get().formBeforeClose()) {
                return;
            }

            Form form = FORMS.undo().get();

            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    private static MainAuthForm getMainAuthForm() {
        if (mainAuthForm == null) {
            mainAuthForm = new MainAuthForm();
        }

        return mainAuthForm;
    }

    private static MainForm getMainForm() {
        if (mainForm == null) {
            mainForm = new MainForm();
        }

        return mainForm;
    }

    private static FormAuthWelcome getWelcome() {
        if (welcomeAuthForm == null) {
            welcomeAuthForm = new FormAuthWelcome();
        }

        return welcomeAuthForm;
    }

    private static void install() {
    }

    private boolean isLoggedOut = true;

    public boolean isLoggedOut() {
        return isLoggedOut;
    }
}
