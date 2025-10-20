UPDATE item_restocks
SET quantity_before = :quantity_before
WHERE _item_restock_id = :_item_restock_id;