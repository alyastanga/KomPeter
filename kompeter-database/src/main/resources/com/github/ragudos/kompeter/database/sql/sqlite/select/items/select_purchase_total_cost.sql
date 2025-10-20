SELECT
    p._purchase_id,
    SUM(pis.quantity_ordered * pis.unit_cost_php) AS total_base_cost,
    
    CASE MAX(p.discount_type)
        WHEN 'fixed' THEN COALESCE(MAX(p.discount_value), 0.00)
        WHEN 'percentage' THEN SUM(pis.quantity_ordered * pis.unit_cost_php) * COALESCE(MAX(p.discount_value), 0.00)
        ELSE 0.00
    END AS total_discount_amount,
    (
        SUM(pis.quantity_ordered * pis.unit_cost_php) 
        - 
        CASE MAX(p.discount_type)
            WHEN 'fixed' THEN COALESCE(MAX(p.discount_value), 0.00) 
            WHEN 'percentage' THEN SUM(pis.quantity_ordered * pis.unit_cost_php) * COALESCE(MAX(p.discount_value), 0.00)
            ELSE 0.00
        END
    ) * (1.00 + COALESCE(MAX(p.vat_percent), 0.00)) AS total_cost

FROM 
    purchase_item_stocks AS pis
INNER JOIN 
    purchases AS p ON pis._purchase_id = p._purchase_id
WHERE p._purchase_id = ?;
GROUP BY 
    p._purchase_id;