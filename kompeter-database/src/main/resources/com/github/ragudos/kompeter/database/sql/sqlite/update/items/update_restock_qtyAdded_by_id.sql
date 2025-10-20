UPDATE item_restocks
SET quantity_added = :quantity_added
WHERE _item_restock_id = :_item_restock_id;