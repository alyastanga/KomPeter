WITH RECURSIVE calendar(date) AS (
    SELECT DATE((SELECT MIN(sale_date) FROM sales)) -- start date, e.g. '2025-01-01'
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?) -- end date, e.g. '2025-12-31'
)
SELECT 
    c.date,
    COALESCE(SUM(sis.unit_price_php * sis.quantity), 0) AS total_revenue
FROM calendar c
LEFT JOIN sales s 
    ON DATE(s.sale_date) = c.date
LEFT JOIN sale_item_stocks sis 
    ON sis._sale_id = s._sale_id
GROUP BY c.date
ORDER BY c.date;
