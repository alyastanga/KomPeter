/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  jeric
 * Created: Oct 18, 2025
 */

SELECT 
    _sale_id, 
    _created_at, 
    sale_date, 
    sale_code, 
    customer_name, 
    vat_percent, 
    discount_value, 
    discount_type 
FROM sales 
WHERE _sale_id = ?;