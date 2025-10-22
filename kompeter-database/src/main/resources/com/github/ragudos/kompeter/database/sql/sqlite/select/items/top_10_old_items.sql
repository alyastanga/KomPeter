SELECT *
FROM (
    SELECT 
        i.name AS item_name,
        ib.name AS brand_name,
        ic.name AS category_name,
        SUM(issl.quantity) AS total_quantity,
        DATE(p.purchase_date) AS stocked_date,
        ROUND(julianday('now') - julianday(p.purchase_date)) AS days_in_stock
    FROM purchases AS p
    JOIN purchase_item_stocks AS pis 
        ON pis._purchase_id = p._purchase_id
    JOIN item_stocks AS is1 
        ON is1._item_stock_id = pis._item_stock_id
    JOIN items AS i 
        ON i._item_id = is1._item_id
    LEFT JOIN item_brands AS ib 
        ON ib._item_brand_id = is1._item_brand_id
    LEFT JOIN item_category_assignments AS ica 
        ON ica._item_id = i._item_id
    LEFT JOIN item_categories AS ic 
        ON ic._item_category_id = ica._item_category_id
    LEFT JOIN item_stock_storage_locations AS issl 
        ON issl._item_stock_id = is1._item_stock_id
    GROUP BY 
        i._item_id, ib._item_brand_id, ic._item_category_id, p.purchase_date
) AS aggregated
WHERE total_quantity > 0
  AND days_in_stock >= 0
ORDER BY days_in_stock DESC
LIMIT 10;
