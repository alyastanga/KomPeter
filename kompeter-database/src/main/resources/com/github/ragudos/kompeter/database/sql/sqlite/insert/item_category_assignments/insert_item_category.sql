INSERT INTO item_category_assignments (
    _item_category_id, _item_id
) VALUES (
    (SELECT _item_category_id FROM item_categories WHERE name = :category_name),
    :_item_id
);
