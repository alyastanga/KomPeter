WITH RECURSIVE calendar(date) AS (
  SELECT DATE(?)              -- start date
  UNION ALL
  SELECT DATE(date, '+1 day')
  FROM calendar
  WHERE date < DATE(?)        -- end date
),
daily_sales AS (
  SELECT 
      DATE(item_sold_datetime) AS sales_date,
      SUM(item_sold_quantity) AS sales_qty
  FROM items_sold
  GROUP BY DATE(item_sold_datetime)
),
running_inventory AS (
  SELECT 
      c.date,
      COALESCE(SUM(dp.sales_qty), 0) AS total_sales
  FROM calendar c
  LEFT JOIN daily_sales dp
         ON dp.sales_date = c.date
  GROUP BY c.date
)
SELECT 
    date,
    total_sales AS total_sales_unit
FROM running_inventory
ORDER BY date;