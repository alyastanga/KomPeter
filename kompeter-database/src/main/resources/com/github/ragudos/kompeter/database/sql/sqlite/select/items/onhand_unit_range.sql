WITH RECURSIVE calendar(date) AS (
    SELECT DATE(?) AS date              -- start date
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?)                -- end date
),
daily_purchases AS (
    SELECT 
        DATE(p.purchase_date) AS purchase_date,
        SUM(pis.quantity_received) AS purchased_qty
    FROM purchases p
    JOIN purchase_item_stocks pis 
        ON pis._purchase_id = p._purchase_id
    GROUP BY DATE(p.purchase_date)
),
daily_sales AS (
    SELECT 
        DATE(s.sale_date) AS sold_date,
        SUM(sis.quantity) AS sold_qty
    FROM sales s
    JOIN sale_item_stocks sis 
        ON sis._sale_id = s._sale_id
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
        date,
        total_purchased,
        total_sold,
        (total_purchased - total_sold) AS total_on_hand
    FROM daily_net
    WHERE date = (SELECT MIN(date) FROM daily_net)
    
    UNION ALL
    
    SELECT 
        dn.date,
        dn.total_purchased,
        dn.total_sold,
        ri.total_on_hand + (dn.total_purchased - dn.total_sold) AS total_on_hand
    FROM daily_net dn
    JOIN running_inventory ri
        ON dn.date = DATE(ri.date, '+1 day')
)
SELECT 
    date,
    total_purchased,
    total_sold,
    total_on_hand
FROM running_inventory
ORDER BY date;
