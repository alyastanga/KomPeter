/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.menu;

import com.github.ragudos.kompeter.auth.SessionManager;
import com.github.ragudos.kompeter.database.dto.user.UserMetadataDto;

import raven.modal.drawer.menu.MenuValidation;

public class KompeterMenuValidation extends MenuValidation {
    private static final int TAB_PROFILE = 0;
    private static final int TAB_LOGOUT = 4;
    private static final int TAB_POS = 1;
    private static final int TAB_INVENTORY = 2;
    private static final int TAB_MONITORING = 3;

    public static boolean validate(final int[] index) {
        if (SessionManager.getInstance().session() == null) {
            return false;
        }

        final UserMetadataDto user = SessionManager.getInstance().session().user();

        if (user.isAdmin()) {
            return true;
        }

        final boolean allAllowed = index[0] == TAB_PROFILE || index[0] == TAB_LOGOUT;

        if (user.isCashier()) {
            return allAllowed || index[0] == TAB_POS;
        }

        if (user.isInventoryClerk()) {
            return allAllowed || index[0] == TAB_INVENTORY;
        }

        if (user.isAuditor()) {
            return allAllowed || index[0] == TAB_MONITORING;
        }

        return allAllowed;
    }

    @Override
    public boolean menuValidation(final int[] index) {
        return validate(index);
    }
}
