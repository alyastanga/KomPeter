/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.scenes.home.inventory;

import com.github.ragudos.kompeter.app.desktop.navigation.Scene;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class InventoryView extends JPanel implements Scene {
    private JPanel content;
    private JPanel header;
    private JScrollPane tableScrollPane;
    private JTable table;
    private JPanel titlePnl;
    private JPanel searchPnl;
    private JPanel addPnl;
    private JPanel deletePnl;
    private JPanel editPnl;
    private JLabel title;

    @Override
    public void onCreate() {
        initialize();
    }

    public void initialize() {
        setLayout(new MigLayout("", "[grow]", "[100]10[grow]"));
        add(header(), "cell 0 0, grow");
        add(content(), "cell 0 1, grow");
    }

    private JPanel content() {
        content = new JPanel();
        content.setLayout(new MigLayout("insets 10", "grow", ""));
        tableScrollPane = new JScrollPane(table());
        content.add(tableScrollPane, "cell 0 0, grow");
        return content;
    }

    private JTable table() {
        String[] colNames = {
            "ID",
            "Product",
            "Category",
            "Brand",
            "Quantity",
            "Price",
            "Location",
            "Supplier",
            "Date Added",
            "Last Modified Date"
        };
        Object[][] data = {};
        DefaultTableModel model = new DefaultTableModel(data, colNames);
        table = new JTable(model);
        return table;
    }

    private JPanel header() {
        header = new JPanel();
        header.setLayout(new MigLayout());
        header.setBackground(this.getBackground());
        header.add(title(), "cell 0 0, alignx left, aligny center");
        return header;
    }

    private JLabel title() {
        title = new JLabel("Inventory");
        title.setFont(new Font("Montserrat", Font.BOLD, 30));
        title.setBackground(this.getBackground());
        return title;
    }

    public static void main(String[] args) {
        InventoryView view = new InventoryView();

        JFrame frame = new JFrame();
        frame.setTitle("Inventory");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.setVisible(true);
    }

    @Override
    public boolean canHide() {
        return Scene.super.canHide(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public boolean canShow() {
        return Scene.super.canShow(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Scene self() {
        return Scene.super.self(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public String name() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public JPanel view() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onBeforeHide() {
        Scene.super.onBeforeHide(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void onBeforeShow() {
        Scene.super.onBeforeShow(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void onCannotHide() {
        Scene.super.onCannotHide(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void onCannotShow() {
        Scene.super.onCannotShow(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void onDestroy() {
        Scene.super.onDestroy(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void onHide() {
        Scene.super.onHide(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void onShow() {
        Scene.super.onShow(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public boolean supportsSubScenes() {
        return Scene.super.supportsSubScenes(); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
}
