WITH RECURSIVE calendar(date) AS (
  SELECT DATE(?)              -- start date
  UNION ALL
  SELECT DATE(date, '+1 day')
  FROM calendar
  WHERE date < DATE(?)        -- end date
),
daily_purchases AS (
  SELECT 
      DATE(item_purchase_datetime) AS purchase_date,
      SUM(item_purchase_quantity) AS purchased_qty
  FROM items_purchase
  GROUP BY DATE(item_purchase_datetime)
),
running_inventory AS (
  SELECT 
      c.date,
      COALESCE(SUM(dp.purchased_qty), 0) AS total_purchases
  FROM calendar c
  LEFT JOIN daily_purchases dp
         ON dp.purchase_date = c.date
  GROUP BY c.date
)
SELECT 
    date,
    total_purchases AS total_purchase_unit
FROM running_inventory
ORDER BY date;