UPDATE item_stock_storage_locations SET
    quantity = :quantity,
    _restock_date = DATETIME('now') WHERE
        _item_stock_storage_location_id = :_item_stock_storage_location_id;
