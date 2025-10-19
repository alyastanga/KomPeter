SELECT 
    i._item_stock_id,
    i._item_id,
    i._item_brand_id,
    i._created_at,
    i.unit_price_php,
    SUM(issl.quantity) AS quantity,
    i.minimum_quantity
FROM item_stocks AS i
INNER JOIN item_stock_storage_locations AS issl ON i._item_stock_id = issl._item_stock_id
WHERE i._item_stock_id = ?;


