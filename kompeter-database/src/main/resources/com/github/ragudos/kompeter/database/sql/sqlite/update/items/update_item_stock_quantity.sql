UPDATE
    item_stock_storage_locations 
SET
    quantity = :quantity
WHERE
    _item_stock_id = :_item_stock_id 
    AND
        _storage_location_id = :_storage_location_id;
