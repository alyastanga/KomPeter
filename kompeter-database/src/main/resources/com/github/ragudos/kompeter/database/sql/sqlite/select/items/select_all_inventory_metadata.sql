SELECT 
    i._item_id,
    ist._item_stock_id,
    issl._item_stock_storage_location_id,
    i._created_at,
    ic.name AS category_name,
    i.name AS item_name,
    i.description AS description,
    ib.name AS brand_name,
    ist.unit_price_php ,
    SUM(issl.quantity) as quantity,
    GROUP_CONCAT(DISTINCT sl.name) AS location_name,
    i.display_image
FROM items as i
INNER join item_stocks as ist ON i._item_id = ist._item_id
INNER JOIN item_category_assignments AS ica oN i._item_id = ica._item_id
INNER JOIN item_categories as ic ON ica._item_category_id = ic._item_category_id
INNER JOIN item_brands AS ib ON ist._item_brand_id = ib._item_brand_id
INNER join item_stock_storage_locations as issl ON ist._item_stock_id = issl._item_stock_id
INNER JOIN storage_locations as sl on issl._storage_location_id = sl._storage_location_id
GROUP BY
    i._item_id,
    i._created_at,
    i.name,
    ic.name,
    ib.name
ORDER BY
    i._item_id;
