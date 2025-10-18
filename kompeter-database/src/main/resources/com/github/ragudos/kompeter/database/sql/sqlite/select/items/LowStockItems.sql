SELECT item_id,
       item_name,
       item_quantity
FROM items
WHERE item_quantity <= ?
ORDER BY item_id;