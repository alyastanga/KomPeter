package com.github.ragudos.kompeter.inventory;

import javax.swing.JTable;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;

import com.github.ragudos.kompeter.utilities.helper.Palette; 

public class InventoryView extends JPanel implements IInventory{
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
	
	
	public InventoryView() {
		initialize();
	}
	
	public void initialize() {
		setLayout(new MigLayout("", "[grow]", "[100]10[grow]"));
		setBackground(Palette.BACKGROUND_COLOR);
                add(header(), "cell 0 0, grow");
                add(content(), "cell 0 1, grow");
		
		
	}
	
        
	private JPanel content() {
		content = new JPanel();
                content.setLayout(new MigLayout("insets 10","grow",""));
		content.setBackground(Palette.WHITE);
                tableScrollPane = new JScrollPane(table());
                content.add(tableScrollPane, "cell 0 0, grow");
                
		return content;
	}
        
        private JTable table(){
            String[] colNames = {"ID" ,"Category", "Product", "Brand" , "Price", "Quantity", "Location", "Date Added", "Last Modified Date"};
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
        
        private JLabel title(){
            title = new JLabel("Inventory");
            title.setFont(new Font("Montserrat", Font.BOLD, 30));
            title.setBackground(this.getBackground());
            return title;
        }

        
        public static void main(String[] args){
            JFrame frame = new JFrame();
            frame.setTitle("Inventory");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new InventoryView());
            frame.setVisible(true);
        }

    @Override
    public void addItem(String category, String itemName, int quantity) {
    }

    @Override
    public void showItem() {
        
    }

    @Override
    public void deleteItem() {
    }

    @Override
    public void updateItem() {
    }

    @Override
    public void searchItem() {
    }
    
    @Override
    public void purchaseItem(String category, String brand, String itemName, int quantity, String supplier) {
    }

    @Override
    public void sortByDateAdded() {
    }

    @Override
    public void sortAlphabetically() {
    }

    @Override
    public void sortByDateUpdated() {
    }

    @Override
    public void sortByCategory() {  
    }

    @Override
    public void refresh() {
    }
}
 
