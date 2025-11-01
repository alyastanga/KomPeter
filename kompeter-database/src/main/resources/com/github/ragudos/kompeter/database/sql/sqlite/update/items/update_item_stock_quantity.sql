INSERT INTO 
    item_stock_storage_locations (
        _item_stock_id,
        _storage_location_id,
        quantity
    )
VALUES (
    :_item_stock_id,
    :_storage_location_id,
    :quantity
)
ON
    CONFLICT(
        _item_stock_id,
        _storage_location_id
    )
DO UPDATE SET
    quantity = quantity + excluded.quantity;
