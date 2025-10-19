/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  jeric
 * Created: Oct 18, 2025
 */

SELECT quantity, unit_price_php
FROM sale_item_stocks
WHERE _sale_id = ? AND  _sale_item_stock_id = ?;
