    WITH RECURSIVE calendar(date) AS (
        -- Generate a continuous sequence of dates between start and end
        SELECT DATE((SELECT MIN(purchase_date) FROM purchases)) AS date                -- start date (e.g. '2025-01-01')
        UNION ALL
        SELECT DATE(date, '+1 day')
        FROM calendar
        WHERE date < DATE(?)                  -- end date (e.g. '2025-12-31')
    ),

    -- DAILY PURCHASE VALUE
    daily_purchases AS (
        SELECT
            DATE(p.purchase_date) AS purchase_date,
            SUM(pis.quantity_received * pis.unit_cost_php) AS purchased_value
        FROM purchases p
        JOIN purchase_item_stocks pis ON pis._purchase_id = p._purchase_id
        GROUP BY DATE(p.purchase_date)
    ),

    -- DAILY SALES VALUE
    daily_sales AS (
        SELECT
            DATE(s.sale_date) AS sale_date,
            SUM(sis.quantity * sis.unit_price_php) AS sold_value
        FROM sales s
        JOIN sale_item_stocks sis ON sis._sale_id = s._sale_id
        GROUP BY DATE(s.sale_date)
    ),

    -- DAILY NET (combine purchases and sales per day)
    daily_net AS (
        SELECT 
            c.date,
            COALESCE(dp.purchased_value, 0) AS total_purchased_value,
            COALESCE(ds.sold_value, 0) AS total_sold_value
        FROM calendar c
        LEFT JOIN daily_purchases dp ON dp.purchase_date = c.date
        LEFT JOIN daily_sales ds     ON ds.sale_date = c.date
    ),

    -- RUNNING INVENTORY VALUE (cumulative)
    running_inventory AS (
        SELECT
            dn.date,
            dn.total_purchased_value,
            dn.total_sold_value,
            SUM(dn.total_purchased_value - dn.total_sold_value)
                OVER (ORDER BY dn.date) AS total_inventory_value
        FROM daily_net dn
    )

    -- FINAL OUTPUT
    SELECT
        date,
        total_inventory_value,
        total_purchased_value,
        total_sold_value
    FROM running_inventory
    ORDER BY date;
