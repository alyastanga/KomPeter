WITH RECURSIVE calendar(date) AS (
  SELECT DATE(?)                -- start date
  UNION ALL
  SELECT DATE(date, '+1 day')
  FROM calendar
  WHERE date < DATE(?)          -- end date
),
daily_sales AS (
  SELECT 
      DATE(s.sale_date) AS sales_date,
      SUM(sis.quantity) AS sales_qty
  FROM sales s
  JOIN sale_item_stocks sis 
      ON sis._sale_id = s._sale_id
  GROUP BY DATE(s.sale_date)
),
running_sales AS (
  SELECT 
      c.date,
      COALESCE(ds.sales_qty, 0) AS total_sales,
      SUM(COALESCE(ds.sales_qty, 0)) OVER (ORDER BY c.date) AS cumulative_sales_units
  FROM calendar c
  LEFT JOIN daily_sales ds
         ON ds.sales_date = c.date
)
SELECT 
    date,
    total_sales AS total_sales_unit,
    cumulative_sales_units
FROM running_sales
ORDER BY date;
