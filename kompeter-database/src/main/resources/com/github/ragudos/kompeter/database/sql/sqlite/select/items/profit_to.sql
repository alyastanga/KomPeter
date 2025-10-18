WITH RECURSIVE calendar(date) AS (
    SELECT DATE(?) -- start date
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE('now') -- end date
),
TOTAL_REVENUE AS (
    SELECT
        c.date,
        COALESCE(SUM(sis.unit_price_php * sis.quantity), 0) AS total_revenue
    FROM calendar c
    LEFT JOIN sales s
        ON DATE(s.sale_date) = c.date
    LEFT JOIN sale_item_stocks sis
        ON sis._sale_id = s._sale_id
    GROUP BY c.date
),
TOTAL_EXPENSES AS (
    SELECT
        c.date,
        COALESCE(SUM(pis.unit_cost_php * pis.quantity_ordered), 0) AS total_expense
    FROM calendar c
    LEFT JOIN purchases p
        ON DATE(p.purchase_date) = c.date
    LEFT JOIN purchase_item_stocks pis
        ON pis._purchase_id = p._purchase_id
    GROUP BY c.date
)
SELECT
    c.date,
    COALESCE(tr.total_revenue, 0) - COALESCE(te.total_expense, 0) AS total_profit,
    total_revenue,
    total_expense
FROM calendar c
LEFT JOIN TOTAL_REVENUE tr 
    ON tr.date = c.date
LEFT JOIN TOTAL_EXPENSES te 
    ON te.date = c.date
ORDER BY c.date;
