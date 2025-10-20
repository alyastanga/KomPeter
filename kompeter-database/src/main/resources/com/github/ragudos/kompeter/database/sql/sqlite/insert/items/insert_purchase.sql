INSERT INTO 
purchases(
    _supplier_id, 
    purchase_date, 
    purchase_code,
    delivery_date, 
    vat_percent, 
    discount_value, 
    discount_type)
VALUES(
    :_supplier_id, 
    :purchase_date, 
    :purchase_code, 
    :delivery_date, 
    :vat_percent, 
    :discount_value,
    :discount_type);
