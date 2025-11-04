SELECT 
    sale._created_at,
    sale.sale_date,
    sale.customer_name,
    sale.sale_code,
    sale._sale_id,
    sale.vat_percent,
    sale.discount_type,
    sale.discount_value,
    json_group_array(
        json_object(
            '_paymentId', sale_payment._sale_payment_id,
            '_createdAt', sale_payment._created_at,
            'amountPhp', sale_payment.amount_php,
            'paymentMethod', sale_payment.payment_method,
            'referenceNumber', sale_payment.reference_number,
            'paymentDate', sale_payment.payment_date
        )
    ) AS payments,
    json_group_array(
        json_object(
            '_itemStockId', sale_item_stock._sale_item_stock_id,
            '_createdAt', sale_item_stock._created_at,
            'quantity', sale_item_stock.quantity,
            'unitPricePhp', sale_item_stock.unit_price_php
        )
    ) AS items
FROM
    sales as sale
INNER JOIN
    sale_payments AS sale_payment
    ON
        sale._sale_id = sale_payment._sale_id
INNER JOIN
    sale_item_stocks AS sale_item_stock
    ON
        sale._sale_id = sale_item_stock._sale_id
GROUP BY
    sale._sale_id,
    sale.sale_code
ORDER BY
    sale._sale_id;
