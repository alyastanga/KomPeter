INSERT INTO sales (
    sale_date,
    sale_code,
    vat_percent,
    discount_type,
    discount_value,
    customer_name
) VALUES (
    :sale_date,
    :sale_code,
    :vat_percent,
    :discount_type,
    :discount_value,
    :customer_name
);
