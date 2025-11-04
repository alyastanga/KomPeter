UPDATE item_stock_storage_locations SET
    quantity = :quantity WHERE
        _item_stock_storage_location_id = :_item_stock_storage_location_id;
