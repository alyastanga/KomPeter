SELECT 
    s.sale_date AS day,
    
    -- Total items revenue after discount
    COALESCE(SUM(
        CASE s.discount_type
            WHEN 'percentage' THEN sis.quantity * sis.unit_price_php * (1 - s.discount_value / 100.0) * (1 + s.vat_percent / 100.0)
            WHEN 'fixed' THEN ((sis.quantity * sis.unit_price_php) - s.discount_value) * (1 + s.vat_percent / 100.0)
            ELSE sis.quantity * sis.unit_price_php * (1 + s.vat_percent / 100.0)
        END
    ), 0) AS total_item_revenue,
    
    -- Total amount paid
    COALESCE(SUM(sp.amount_php), 0) AS total_paid
FROM sales s
LEFT JOIN sale_item_stocks sis 
    ON sis._sale_id = s._sale_id
LEFT JOIN sale_payments sp
    ON sp._sale_id = s._sale_id
GROUP BY strftime('%Y-%m-%d', s.sale_date)
ORDER BY strftime('%Y-%m-%d', s.sale_date);
