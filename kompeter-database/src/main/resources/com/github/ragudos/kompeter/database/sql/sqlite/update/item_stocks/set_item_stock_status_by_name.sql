UPDATE
    item_stocks
SET
    status = :status
WHERE
    _item_id
    IN (
        SELECT
            _item_id
        FROM
            items
        WHERE
            name = :name
    );
