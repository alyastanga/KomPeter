INSERT INTO item_restocks(_item_stock_id, quantity_before, quantity_after, quantity_added)
VALUES (
    :_item_stock_id,
    :quantity_before, 
    :quantity_after,
    :quantity_added);