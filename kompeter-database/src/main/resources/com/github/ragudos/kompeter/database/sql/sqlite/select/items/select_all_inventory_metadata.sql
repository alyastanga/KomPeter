/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Peter M. Dela Cruz
 * Created: Oct 11, 2025
 */

SELECT
    i._item_id,                             
    ist._item_stock_id,                    
    issl._item_stock_storage_location_id,  
    ic._item_category_id,                   
    i._created_at,
    ic.name AS category_name,
    i.name AS item_name,
    ib.name AS brand_name,
    ist.unit_price_php,
    issl.quantity,
    sl.name AS location_name
FROM
    items AS i
INNER JOIN item_category_assignments AS ica ON i._item_id = ica._item_id
INNER JOIN item_categories AS ic ON ica._item_category_id = ic._item_category_id
INNER JOIN item_stocks AS ist ON i._item_id = ist._item_id
LEFT JOIN item_brands AS ib ON ist._item_brand_id = ib._item_brand_id
INNER JOIN item_stock_storage_locations AS issl ON ist._item_stock_id = issl._item_stock_id
INNER JOIN storage_locations AS sl ON issl._storage_location_id = sl._storage_location_id
