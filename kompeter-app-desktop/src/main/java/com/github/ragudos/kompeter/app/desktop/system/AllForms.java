/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.system;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

// all logged in forms
public class AllForms {
    private static AllForms instance;

    public static void formInit(Form form) {
        SwingUtilities.invokeLater(() -> form.formInit());
    }

    public static Form getForm(Class<? extends Form> cls) {
        if (getInstance().formsMap.containsKey(cls)) {
            return getInstance().formsMap.get(cls);
        }

        try {
            Form form = cls.getDeclaredConstructor().newInstance();
            getInstance().formsMap.put(cls, form);
            formInit(form);
            return form;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear() {
        getInstance().formsMap.clear();
    }

    private static AllForms getInstance() {
        if (instance == null) {
            instance = new AllForms();
        }
        return instance;
    }

    private final Map<Class<? extends Form>, Form> formsMap;

    private AllForms() {
        formsMap = new HashMap<>();
    }
}
