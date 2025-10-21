UPDATE item_restocks
SET quantity_after = :quantity_after
WHERE _item_restock_id = :_item_restock_id;