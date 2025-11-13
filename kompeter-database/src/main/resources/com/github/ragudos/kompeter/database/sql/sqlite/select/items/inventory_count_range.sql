SELECT
    _created_at AS day,
    SUM(quantity_after) AS total_quantity,
    SUM(quantity_added) AS total_quantity_added,
    SUM(quantity_before) AS total_quantity_before
FROM item_restocks
GROUP BY STRFTIME('%Y-%m-%d', _created_at)
ORDER BY day ASC;
