WITH RECURSIVE calendar(date) AS (
    SELECT DATE((SELECT MIN(purchase_date) FROM purchases)) -- start date, e.g. '2025-01-01'
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?) -- end date, e.g. '2025-12-31'
)
SELECT 
    c.date,
    COALESCE(SUM(pis.unit_cost_php * pis.quantity_ordered), 0) AS total_expense
FROM calendar c
LEFT JOIN purchases p 
    ON DATE(p.purchase_date) = c.date
LEFT JOIN purchase_item_stocks pis 
    ON pis._purchase_id = p._purchase_id
GROUP BY c.date
ORDER BY c.date;
