SELECT 
    (unit_cost_php * quantity_ordered) AS cost
FROM 
    purchase_item_stocks 
WHERE 
    _purchase_id = ? AND _item_stock_id = ?;