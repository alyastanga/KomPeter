UPDATE item_stocks
SET minimum_quantity = :minimum_quantity
WHERE _item_stock_id = :_item_stock_id;
