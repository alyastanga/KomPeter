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

    public static boolean validate(int[] index) {
        if (SessionManager.getInstance().session() == null) {
            return false;
        }

        UserMetadataDto user = SessionManager.getInstance().session().user();

        if (user.isAdmin()) {
            return true;
        }

        if (user.isAuditor()) {
            return index[0] == 0 || (index[0] == 4 && index[1] == 3) || index[0] == 6 || index[0] == 7;
        }

        if (user.isCashier()) {
            return index[0] == 0 || index[0] == 2 || index[0] == 6 || index[0] == 7;
        }

        if (user.isInventoryClerk()) {
            return index[0] == 0 || index[0] == 3 || index[0] == 6 || index[0] == 7;
        }

        if (user.isManager()) {
            return index[0] == 0 || index[0] == 4 || index[0] == 5 || index[0] == 6 || index[0] == 7;
        }

        return index[0] == 0 || index[0] == 6 || index[0] == 7;
    }

    @Override
    public boolean menuValidation(int[] index) {
        return validate(index);
    }
}
