SELECT 
    i.name AS item_name,
    ib.name AS brand_name,
    ic.name AS category_name,
    is1.quantity
FROM item_stocks AS is1
JOIN items AS i 
    ON is1._item_id = i._item_id
LEFT JOIN item_brands AS ib 
    ON is1._item_brand_id = ib._item_brand_id
LEFT JOIN item_category_assignments AS ica 
    ON i._item_id = ica._item_id
LEFT JOIN item_categories AS ic 
    ON ica._item_category_id = ic._item_category_id
WHERE 
    (
        (ic.name = 'PC Components' AND is1.quantity <= 10)
        OR (ic.name = 'Accessories' AND is1.quantity <= 20)
        OR (ic.name = 'Laptops' AND is1.quantity <= 10)
        OR (ic.name = 'Peripherals' AND is1.quantity <= 15)
    )
ORDER BY 
    ic.name ASC,
    is1.quantity ASC,
    i.name ASC;
