WITH total_stock AS (
    SELECT 
        issl._item_stock_id,
        SUM(issl.quantity) AS total_quantity
    FROM item_stock_storage_locations AS issl
    GROUP BY issl._item_stock_id
)
SELECT 
    i.name AS item_name,
    ib.name AS brand_name,
    ic.name AS category_name,
    COALESCE(ts.total_quantity, 0) AS quantity
FROM item_stocks AS is1
JOIN items AS i 
    ON is1._item_id = i._item_id
LEFT JOIN item_brands AS ib 
    ON is1._item_brand_id = ib._item_brand_id
LEFT JOIN item_category_assignments AS ica 
    ON i._item_id = ica._item_id
LEFT JOIN item_categories AS ic 
    ON ica._item_category_id = ic._item_category_id
LEFT JOIN total_stock AS ts 
    ON is1._item_stock_id = ts._item_stock_id
WHERE 
    (
        (ic.name = 'PC Components' AND COALESCE(ts.total_quantity, 0) <= 10)
        OR (ic.name = 'Accessories' AND COALESCE(ts.total_quantity, 0) <= 20)
        OR (ic.name = 'Laptops' AND COALESCE(ts.total_quantity, 0) <= 10)
        OR (ic.name = 'Peripherals' AND COALESCE(ts.total_quantity, 0) <= 15)
    )
ORDER BY total_quantity ASC, i.name ASC
LIMIT 10;
