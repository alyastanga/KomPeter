package com.github.ragudos.kompeter.inventory;

public interface IInventory {
	void addItem(String category, String itemName, int quantity );
        void purchaseItem(String category, String brand, String itemName, int quantity, String supplier);
        void showItem();
        void deleteItem();
        void updateItem();
        void searchItem();
        void sortByDateAdded();
        void sortAlphabetically();
        void sortByDateUpdated();
        void sortByCategory();
        void refresh();
	
}
