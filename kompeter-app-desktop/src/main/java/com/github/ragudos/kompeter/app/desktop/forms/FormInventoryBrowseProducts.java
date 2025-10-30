/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;

@SystemForm(name = "Inventory Browse Products", description = "Shows all products", tags = {"inventory"})
public class FormInventoryBrowseProducts extends Form {
    private JPanel bodyPanel;

    private JPanel headerPanel;
    private AtomicBoolean isFetching;
    private JTable productsTable;
    private JTextField searchTextField;

    @Override
    public boolean formBeforeClose() {
        return super.formBeforeClose();
    }

    @Override
    public boolean formBeforeLogout() {
        // TODO Auto-generated method stub
        return super.formBeforeLogout();
    }

    @Override
    public void formInit() {
        init();
        formRefresh();
    }

    @Override
    public void formRefresh() {
        if (isFetching.get()) {
            return;
        }

        new Thread(this::loadData, "Load Inventory Products Data").start();
    }

    private void init() {
        isFetching = new AtomicBoolean(false);
    }

    private void loadData() {
        isFetching.set(true);
    }
}
