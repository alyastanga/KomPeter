/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  jeric
 * Created: Oct 19, 2025
 */

/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  jeric
 * Created: Oct 19, 2025
 */

SELECT
    _sale_item_stock_id,
    _sale_id,
    quantity,
    unit_price_php
 FROM
    sale_item_stocks
 WHERE 
    _created_at BETWEEN ? AND ?;
    