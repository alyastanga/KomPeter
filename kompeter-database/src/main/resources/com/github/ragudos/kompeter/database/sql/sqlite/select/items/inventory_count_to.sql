WITH RECURSIVE calendar(date) AS (
    SELECT DATE((SELECT MIN(purchase_date) FROM purchases))              -- start date (YYYY-MM-DD)
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?)        -- end date (YYYY-MM-DD)
),
daily_purchases AS (
    SELECT 
        DATE(p.purchase_date) AS purchase_date,
        SUM(pis.quantity_received) AS purchased_qty
    FROM purchase_item_stocks pis
    JOIN purchases p ON pis._purchase_id = p._purchase_id
    GROUP BY DATE(p.purchase_date)
),
daily_sales AS (
    SELECT 
        DATE(s.sale_date) AS sold_date,
        SUM(sis.quantity) AS sold_qty
    FROM sale_item_stocks sis
    JOIN sales s ON sis._sale_id = s._sale_id
    GROUP BY DATE(s.sale_date)
),
daily_net AS (
    SELECT 
        c.date,
        COALESCE(dp.purchased_qty, 0) AS total_purchased,
        COALESCE(ds.sold_qty, 0) AS total_sold
    FROM calendar c
    LEFT JOIN daily_purchases dp ON dp.purchase_date = c.date
    LEFT JOIN daily_sales ds     ON ds.sold_date = c.date
),
running_inventory AS (
    SELECT 
        dn.date,
        dn.total_purchased,
        dn.total_sold,
        SUM(dn.total_purchased - dn.total_sold) 
           OVER (ORDER BY dn.date) AS total_inventory
    FROM daily_net dn
)
SELECT 
    date,
    total_inventory,
    total_purchased,
    total_sold
FROM running_inventory
ORDER BY date;
