WITH RECURSIVE calendar(date) AS (
  SELECT DATE(?)                -- start date (bind as param 1)
  UNION ALL
  SELECT DATE(date, '+1 day')
  FROM calendar
  WHERE date < DATE('now')          -- end date (bind as param 2)
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
running_inventory AS (
  SELECT 
    c.date,
    COALESCE(dp.purchased_qty, 0) AS total_purchases,
    -- cumulative (running) total of purchased units up to that date
    SUM(COALESCE(dp.purchased_qty, 0)) OVER (ORDER BY c.date) AS cumulative_purchased_units
  FROM calendar c
  LEFT JOIN daily_purchases dp
    ON dp.purchase_date = c.date
)
SELECT 
  date,
  total_purchases AS total_purchase_unit,
  cumulative_purchased_units
FROM running_inventory
ORDER BY date;
