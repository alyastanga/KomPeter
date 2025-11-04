/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import javax.swing.JTable;

import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;

@SystemForm(name = "Point of Sale Transactions", description = "Shows the list of transactions or all products that have been sold.", tags = {
        "sales", "transactions"})
public class FormPosTransactions extends Form {

    @Override
    public void formInit() {
        init();
    }

    @Override
    public void formOpen() {
    }

    private void init() {
    }

    private class TransactionsTable extends JTable {
        public TransactionsTable() {
        }
    }
}
