WITH RECURSIVE calendar(date) AS (
    SELECT DATE(?)                  -- start date
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?)            -- end date (today)
),
daily_purchases AS (
    SELECT DATE(item_purchase_datetime) AS purchase_date,
           SUM(item_purchase_quantity) AS purchased_qty
    FROM items_purchase
    GROUP BY DATE(item_purchase_datetime)
),
daily_sales AS (
    SELECT DATE(item_sold_datetime) AS sold_date,
           SUM(item_sold_quantity) AS sold_qty
    FROM items_sold
    GROUP BY DATE(item_sold_datetime)
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
total_current_stock AS (
    SELECT SUM(item_quantity) AS current_total FROM items
),
back_propagated_inventory AS (
    SELECT
        dn.date,
        dn.total_purchased,
        dn.total_sold,
        (SELECT current_total FROM total_current_stock)
          - COALESCE(SUM(dn2.total_purchased - dn2.total_sold), 0) AS total_on_hand
    FROM daily_net dn
    LEFT JOIN daily_net dn2
           ON dn2.date > dn.date        -- future movements
    GROUP BY dn.date, dn.total_purchased, dn.total_sold
)
SELECT date, total_on_hand, total_purchased, total_sold
FROM back_propagated_inventory
ORDER BY date;