WITH purchase_totals AS (
    SELECT 
        ip.item_id,
        DATE(ip.item_purchase_datetime) AS purchase_date,
        SUM(ip.item_purchase_quantity) AS purchased_qty
    FROM items_purchase ip
    GROUP BY ip.item_id, DATE(ip.item_purchase_datetime)
),
sold_totals AS (
    SELECT 
        isd.item_id,
        DATE(isd.item_sold_datetime) AS sold_date,
        SUM(isd.item_sold_quantity) AS sold_qty
    FROM items_sold isd
    GROUP BY isd.item_id, DATE(isd.item_sold_datetime)
),
net_movements AS (
    SELECT 
        p.item_id,
        p.purchase_date,
        p.purchased_qty - COALESCE(SUM(s.sold_qty),0) AS remaining_qty
    FROM purchase_totals p
    LEFT JOIN sold_totals s
      ON s.item_id = p.item_id
     AND s.sold_date >= p.purchase_date   -- sales consume later stock
    GROUP BY p.item_id, p.purchase_date, p.purchased_qty
),
remaining_stock AS (
    SELECT 
        i.item_id,
        i.item_name,
        n.purchase_date,
        CASE 
            WHEN n.remaining_qty < 0 THEN 0
            ELSE n.remaining_qty
        END AS remaining_qty
    FROM net_movements n
    JOIN items i ON i.item_id = n.item_id
)
SELECT item_id, item_name, purchase_date, remaining_qty
FROM remaining_stock
WHERE remaining_qty > 0
ORDER BY purchase_date ASC, item_id;