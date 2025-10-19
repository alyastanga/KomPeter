/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  jeric
 * Created: Oct 19, 2025
 */

INSERT INTO sale_payments (
    _sale_id,
    _created_at,
    payment_date,
    reference_number,
    payment_method,
    amount_php
)
VALUES (?, ?, ?, ?, ?, ?);
