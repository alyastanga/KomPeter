CREATE TABLE IF NOT EXISTS audit_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    table_name TEXT NOT NULL,
    action TEXT NOT NULL,         -- 'INSERT', 'UPDATE', 'DELETE'
    row_id INTEGER,
    old_data TEXT,
    new_data TEXT,
    changed_at TEXT DEFAULT (datetime('now'))
);

DELIMITER $$

CREATE TRIGGER IF NOT EXISTS roles_audit_insert
AFTER INSERT ON roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('roles', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS users_audit_insert
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('users', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS accounts_audit_insert
AFTER INSERT ON accounts
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('accounts', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS user_roles_audit_insert
AFTER INSERT ON user_roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('user_roles', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS sessions_audit_insert
AFTER INSERT ON sessions
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('sessions', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS storage_locations_audit_insert
AFTER INSERT ON storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('storage_locations', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_categories_audit_insert
AFTER INSERT ON item_categories
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_categories', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_brands_audit_insert
AFTER INSERT ON item_brands
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_brands', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS items_audit_insert
AFTER INSERT ON items
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('items', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_category_assignments_audit_insert
AFTER INSERT ON item_category_assignments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_category_assignments', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_stocks_audit_insert
AFTER INSERT ON item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_stocks', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_stock_storage_locations_audit_insert
AFTER INSERT ON item_stock_storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_stock_storage_locations', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_restocks_audit_insert
AFTER INSERT ON item_restocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_restocks', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS suppliers_audit_insert
AFTER INSERT ON suppliers
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('suppliers', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS purchases_audit_insert
AFTER INSERT ON purchases
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('purchases', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS purchase_payments_audit_insert
AFTER INSERT ON purchase_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('purchase_payments', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS purchase_item_stocks_audit_insert
AFTER INSERT ON purchase_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('purchase_item_stocks', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS sales_audit_insert
AFTER INSERT ON sales
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('sales', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS sale_payments_audit_insert
AFTER INSERT ON sale_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('sale_payments', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS sale_item_stocks_audit_insert
AFTER INSERT ON sale_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('sale_item_stocks', 'INSERT', NEW.rowid, json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS roles_audit_update
AFTER UPDATE ON roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('roles', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS users_audit_update
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('users', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS accounts_audit_update
AFTER UPDATE ON accounts
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('accounts', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS user_roles_audit_update
AFTER UPDATE ON user_roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('user_roles', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS sessions_audit_update
AFTER UPDATE ON sessions
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('sessions', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS storage_locations_audit_update
AFTER UPDATE ON storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('storage_locations', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_categories_audit_update
AFTER UPDATE ON item_categories
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_categories', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_brands_audit_update
AFTER UPDATE ON item_brands
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_brands', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS items_audit_update
AFTER UPDATE ON items
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('items', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_category_assignments_audit_update
AFTER UPDATE ON item_category_assignments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_category_assignments', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_stocks_audit_update
AFTER UPDATE ON item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_stocks', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_stock_storage_locations_audit_update
AFTER UPDATE ON item_stock_storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_stock_storage_locations', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS item_restocks_audit_update
AFTER UPDATE ON item_restocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_restocks', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS suppliers_audit_update
AFTER UPDATE ON suppliers
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('suppliers', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS purchases_audit_update
AFTER UPDATE ON purchases
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('purchases', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS purchase_payments_audit_update
AFTER UPDATE ON purchase_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('purchase_payments', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS purchase_item_stocks_audit_update
AFTER UPDATE ON purchase_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('purchase_item_stocks', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS sales_audit_update
AFTER UPDATE ON sales
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('sales', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS sale_payments_audit_update
AFTER UPDATE ON sale_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('sale_payments', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS sale_item_stocks_audit_update
AFTER UPDATE ON sale_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('sale_item_stocks', 'UPDATE', OLD.rowid, json(OLD), json(NEW));
END $$

CREATE TRIGGER IF NOT EXISTS roles_audit_delete
AFTER DELETE ON roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('roles', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS users_audit_delete
AFTER DELETE ON users
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('users', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS accounts_audit_delete
AFTER DELETE ON accounts
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('accounts', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS user_roles_audit_delete
AFTER DELETE ON user_roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('user_roles', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS sessions_audit_delete
AFTER DELETE ON sessions
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('sessions', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS storage_locations_audit_delete
AFTER DELETE ON storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('storage_locations', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS item_categories_audit_delete
AFTER DELETE ON item_categories
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_categories', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS item_brands_audit_delete
AFTER DELETE ON item_brands
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_brands', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS items_audit_delete
AFTER DELETE ON items
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('items', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS item_category_assignments_audit_delete
AFTER DELETE ON item_category_assignments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_category_assignments', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS item_stocks_audit_delete
AFTER DELETE ON item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_stocks', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS item_stock_storage_locations_audit_delete
AFTER DELETE ON item_stock_storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_stock_storage_locations', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS item_restocks_audit_delete
AFTER DELETE ON item_restocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_restocks', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS suppliers_audit_delete
AFTER DELETE ON suppliers
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('suppliers', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS purchases_audit_delete
AFTER DELETE ON purchases
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('purchases', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS purchase_payments_audit_delete
AFTER DELETE ON purchase_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('purchase_payments', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS purchase_item_stocks_audit_delete
AFTER DELETE ON purchase_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('purchase_item_stocks', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS sales_audit_delete
AFTER DELETE ON sales
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('sales', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS sale_payments_audit_delete
AFTER DELETE ON sale_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('sale_payments', 'DELETE', OLD.rowid, json(OLD));
END $$

CREATE TRIGGER IF NOT EXISTS sale_item_stocks_audit_delete
AFTER DELETE ON sale_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('sale_item_stocks', 'DELETE', OLD.rowid, json(OLD));
END $$

DELIMITER ;

