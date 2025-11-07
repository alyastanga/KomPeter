/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import javax.swing.JLabel;

import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;

@SystemForm(name = "Profile", description = "Shows information about the currently logged in user.", tags = { "user",
        "profile" })
public final class FormProfile extends Form {
    public FormProfile() {
        init();
    }

    @Override
    public void formInit() {

        add(new JLabel("Welcome!"));

        super.formInit();
    }

    @Override
    public void formOpen() {
        super.formOpen();
    }

    private void init() {
    }
}
