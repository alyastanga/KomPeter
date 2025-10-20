SELECT 
    i.name AS item_name,
    ib.name AS brand_name,
    ic.name AS category_name,
    is1.quantity AS current_quantity,
    DATE(is1._created_at) AS stocked_date,
    ROUND(julianday('now') - julianday(is1._created_at)) AS days_in_stock
FROM item_stocks AS is1
JOIN items AS i 
    ON i._item_id = is1._item_id
LEFT JOIN item_brands AS ib 
    ON ib._item_brand_id = is1._item_brand_id
LEFT JOIN item_category_assignments AS ica 
    ON ica._item_id = i._item_id
LEFT JOIN item_categories AS ic 
    ON ic._item_category_id = ica._item_category_id
WHERE 
    is1.quantity > 0
    AND (julianday('now') - julianday(is1._created_at)) >= 30
ORDER BY days_in_stock DESC;
