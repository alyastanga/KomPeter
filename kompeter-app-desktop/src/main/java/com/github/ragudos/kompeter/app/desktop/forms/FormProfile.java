/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import javax.swing.JLabel;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.auth.SessionManager;
import com.github.ragudos.kompeter.database.dto.user.UserMetadataDto;

import net.miginfocom.swing.MigLayout;

@SystemForm(name = "Profile", description = "Shows information about the currently logged in user.", tags = { "user",
        "profile" })
public final class FormProfile extends Form {
    public FormProfile() {
        init();
    }

    @Override
    public void formInit() {
        setLayout(new MigLayout("insets 12, flowx"));

        if (SessionManager.getInstance().session() == null) {
            return;
        }

        final UserMetadataDto user = SessionManager.getInstance().session().user();

        final JLabel fullName = new JLabel(
                String.format("%s (%s %s)", user.displayName(), user.firstName(), user.lastName()));
        final JLabel email = new JLabel(String.format("Email: %s", user.email()));
        final JLabel roles = new JLabel(String.format("Role/s: %s", String.join(", ", user.roles())));

        fullName.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");

        email.putClientProperty(FlatClientProperties.STYLE, "foreground:$TextField.placeholderForeground;");

        add(fullName, "wrap");
        add(email, "wrap");
        add(roles);

        super.formInit();
    }

    @Override
    public void formOpen() {

    }

    private void init() {
    }
}
