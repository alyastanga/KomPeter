SELECT 
    i.name AS item_name,
    ib.name AS brand_name,
    ic.name AS category_name,
    SUM(sis.quantity) AS total_sold,
    SUM(sis.quantity * sis.unit_price_php) AS total_revenue
FROM sale_item_stocks sis
INNER JOIN item_stocks ist 
    ON sis._item_stock_id = ist._item_stock_id
INNER JOIN items i 
    ON ist._item_id = i._item_id
INNER JOIN sales s
    ON sis._sale_id = s._sale_id
LEFT JOIN item_brands ib 
    ON ist._item_brand_id = ib._item_brand_id
LEFT JOIN item_category_assignments ica 
    ON ica._item_id = i._item_id
LEFT JOIN item_categories ic 
    ON ic._item_category_id = ica._item_category_id
WHERE DATE(s.sale_date) BETWEEN DATE(?) AND DATE(?)
GROUP BY i._item_id, i.name, ib.name, ic.name
ORDER BY total_sold DESC
LIMIT 10;
