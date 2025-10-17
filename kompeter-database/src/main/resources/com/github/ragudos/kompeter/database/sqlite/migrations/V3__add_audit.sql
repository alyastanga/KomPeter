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
    VALUES (
        'roles',
        'INSERT',
        NEW._role_id,
        json_object(
            '_role_id', NEW._role_id,
            '_created_at', NEW._created_at,
            'role_name', NEW.role_name,
            'description', NEW.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS roles_audit_update
AFTER UPDATE ON roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'roles',
        'UPDATE',
        OLD._role_id,
        json_patch('{}',
            CASE WHEN OLD.role_name IS NOT NEW.role_name THEN json_object('role_name', OLD.role_name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', OLD.description) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD.role_name IS NOT NEW.role_name THEN json_object('role_name', NEW.role_name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', NEW.description) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS roles_audit_delete
AFTER DELETE ON roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'roles',
        'DELETE',
        OLD._role_id,
        json_object(
            '_role_id', OLD._role_id,
            '_created_at', OLD._created_at,
            'role_name', OLD.role_name,
            'description', OLD.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS users_audit_insert
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'users',
        'INSERT',
        NEW._user_id,
        json_object(
            '_user_id', NEW._user_id,
            '_created_at', NEW._created_at,
            'display_name', NEW.display_name,
            'first_name', NEW.first_name,
            'last_name', NEW.last_name
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS users_audit_update
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'users',
        'UPDATE',
        OLD._user_id,
        json_patch('{}',
            CASE WHEN OLD.display_name IS NOT NEW.display_name THEN json_object('display_name', OLD.display_name) ELSE '{}' END,
            CASE WHEN OLD.first_name IS NOT NEW.first_name THEN json_object('first_name', OLD.first_name) ELSE '{}' END,
            CASE WHEN OLD.last_name IS NOT NEW.last_name THEN json_object('last_name', OLD.last_name) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD.display_name IS NOT NEW.display_name THEN json_object('display_name', NEW.display_name) ELSE '{}' END,
            CASE WHEN OLD.first_name IS NOT NEW.first_name THEN json_object('first_name', NEW.first_name) ELSE '{}' END,
            CASE WHEN OLD.last_name IS NOT NEW.last_name THEN json_object('last_name', NEW.last_name) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS users_audit_delete
AFTER DELETE ON users
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'users',
        'DELETE',
        OLD._user_id,
        json_object(
            '_user_id', OLD._user_id,
            '_created_at', OLD._created_at,
            'display_name', OLD.display_name,
            'first_name', OLD.first_name,
            'last_name', OLD.last_name
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS accounts_audit_insert
AFTER INSERT ON accounts
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'accounts',
        'INSERT',
        NEW._account_id,
        json_object(
            '_account_id', NEW._account_id,
            '_created_at', NEW._created_at,
            '_user_id', NEW._user_id,
            'password_hash', NEW.password_hash,
            'password_salt', NEW.password_salt,
            'email', NEW.email
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS accounts_audit_update
AFTER UPDATE ON accounts
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'accounts',
        'UPDATE',
        OLD._account_id,
        json_patch('{}',
            CASE WHEN OLD._user_id IS NOT NEW._user_id THEN json_object('_user_id', OLD._user_id) ELSE '{}' END,
            CASE WHEN OLD.password_hash IS NOT NEW.password_hash THEN json_object('password_hash', OLD.password_hash) ELSE '{}' END,
            CASE WHEN OLD.password_salt IS NOT NEW.password_salt THEN json_object('password_salt', OLD.password_salt) ELSE '{}' END,
            CASE WHEN OLD.email IS NOT NEW.email THEN json_object('email', OLD.email) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._user_id IS NOT NEW._user_id THEN json_object('_user_id', NEW._user_id) ELSE '{}' END,
            CASE WHEN OLD.password_hash IS NOT NEW.password_hash THEN json_object('password_hash', NEW.password_hash) ELSE '{}' END,
            CASE WHEN OLD.password_salt IS NOT NEW.password_salt THEN json_object('password_salt', NEW.password_salt) ELSE '{}' END,
            CASE WHEN OLD.email IS NOT NEW.email THEN json_object('email', NEW.email) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS accounts_audit_delete
AFTER DELETE ON accounts
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'accounts',
        'DELETE',
        OLD._account_id,
        json_object(
            '_account_id', OLD._account_id,
            '_created_at', OLD._created_at,
            '_user_id', OLD._user_id,
            'password_hash', OLD.password_hash,
            'password_salt', OLD.password_salt,
            'email', OLD.email
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS user_roles_audit_insert
AFTER INSERT ON user_roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'user_roles',
        'INSERT',
        NEW._user_role_id,
        json_object(
            '_user_role_id', NEW._user_role_id,
            '_created_at', NEW._created_at,
            '_user_id', NEW._user_id,
            '_role_id', NEW._role_id
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS user_roles_audit_update
AFTER UPDATE ON user_roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'user_roles',
        'UPDATE',
        OLD._user_role_id,
        json_patch('{}',
            CASE WHEN OLD._user_id IS NOT NEW._user_id THEN json_object('_user_id', OLD._user_id) ELSE '{}' END,
            CASE WHEN OLD._role_id IS NOT NEW._role_id THEN json_object('_role_id', OLD._role_id) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._user_id IS NOT NEW._user_id THEN json_object('_user_id', NEW._user_id) ELSE '{}' END,
            CASE WHEN OLD._role_id IS NOT NEW._role_id THEN json_object('_role_id', NEW._role_id) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS user_roles_audit_delete
AFTER DELETE ON user_roles
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'user_roles',
        'DELETE',
        OLD._user_role_id,
        json_object(
            '_user_role_id', OLD._user_role_id,
            '_created_at', OLD._created_at,
            '_user_id', OLD._user_id,
            '_role_id', OLD._role_id
        )
    );
END $$


CREATE TRIGGER IF NOT EXISTS sessions_audit_insert
AFTER INSERT ON sessions
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'sessions',
        'INSERT',
        NEW._session_id,
        json_object(
            '_session_id', NEW._session_id,
            '_created_at', NEW._created_at,
            '_user_id', NEW._user_id,
            'session_token', NEW.session_token,
            'ip_address', NEW.ip_address
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sessions_audit_update
AFTER UPDATE ON sessions
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'sessions',
        'UPDATE',
        OLD._session_id,
        json_patch('{}',
            CASE WHEN OLD._user_id IS NOT NEW._user_id THEN json_object('_user_id', OLD._user_id) ELSE '{}' END,
            CASE WHEN OLD.session_token IS NOT NEW.session_token THEN json_object('session_token', OLD.session_token) ELSE '{}' END,
            CASE WHEN OLD.ip_address IS NOT NEW.ip_address THEN json_object('ip_address', OLD.ip_address) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._user_id IS NOT NEW._user_id THEN json_object('_user_id', NEW._user_id) ELSE '{}' END,
            CASE WHEN OLD.session_token IS NOT NEW.session_token THEN json_object('session_token', NEW.session_token) ELSE '{}' END,
            CASE WHEN OLD.ip_address IS NOT NEW.ip_address THEN json_object('ip_address', NEW.ip_address) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sessions_audit_delete
AFTER DELETE ON sessions
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'sessions',
        'DELETE',
        OLD._session_id,
        json_object(
            '_session_id', OLD._session_id,
            '_created_at', OLD._created_at,
            '_user_id', OLD._user_id,
            'session_token', OLD.session_token,
            'ip_address', OLD.ip_address
        )
    );
END $$


CREATE TRIGGER IF NOT EXISTS storage_locations_audit_insert
AFTER INSERT ON storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'storage_locations',
        'INSERT',
        NEW._storage_location_id,
        json_object(
            '_storage_location_id', NEW._storage_location_id,
            '_created_at', NEW._created_at,
            'name', NEW.name,
            'description', NEW.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS storage_locations_audit_update
AFTER UPDATE ON storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'storage_locations',
        'UPDATE',
        OLD._storage_location_id,
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', OLD.name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', OLD.description) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', NEW.name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', NEW.description) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS storage_locations_audit_delete
AFTER DELETE ON storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'storage_locations',
        'DELETE',
        OLD._storage_location_id,
        json_object(
            '_storage_location_id', OLD._storage_location_id,
            '_created_at', OLD._created_at,
            'name', OLD.name,
            'description', OLD.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_categories_audit_insert
AFTER INSERT ON item_categories
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_categories', 'INSERT', NEW._item_category_id,
        json_object(
            '_item_category_id', NEW._item_category_id,
            '_created_at', NEW._created_at,
            'name', NEW.name,
            'description', NEW.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_categories_audit_update
AFTER UPDATE ON item_categories
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_categories', 'UPDATE', OLD._item_category_id,
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', OLD.name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', OLD.description) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', NEW.name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', NEW.description) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_categories_audit_delete
AFTER DELETE ON item_categories
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_categories', 'DELETE', OLD._item_category_id,
        json_object(
            '_item_category_id', OLD._item_category_id,
            '_created_at', OLD._created_at,
            'name', OLD.name,
            'description', OLD.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_brands_audit_insert
AFTER INSERT ON item_brands
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_brands', 'INSERT', NEW._item_brand_id,
        json_object(
            '_item_brand_id', NEW._item_brand_id,
            '_created_at', NEW._created_at,
            'name', NEW.name,
            'description', NEW.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_brands_audit_update
AFTER UPDATE ON item_brands
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_brands', 'UPDATE', OLD._item_brand_id,
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', OLD.name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', OLD.description) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', NEW.name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', NEW.description) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_brands_audit_delete
AFTER DELETE ON item_brands
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_brands', 'DELETE', OLD._item_brand_id,
        json_object(
            '_item_brand_id', OLD._item_brand_id,
            '_created_at', OLD._created_at,
            'name', OLD.name,
            'description', OLD.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS items_audit_insert
AFTER INSERT ON items
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('items', 'INSERT', NEW._item_id,
        json_object(
            '_item_id', NEW._item_id,
            '_created_at', NEW._created_at,
            'name', NEW.name,
            'description', NEW.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS items_audit_update
AFTER UPDATE ON items
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('items', 'UPDATE', OLD._item_id,
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', OLD.name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', OLD.description) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', NEW.name) ELSE '{}' END,
            CASE WHEN OLD.description IS NOT NEW.description THEN json_object('description', NEW.description) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS items_audit_delete
AFTER DELETE ON items
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('items', 'DELETE', OLD._item_id,
        json_object(
            '_item_id', OLD._item_id,
            '_created_at', OLD._created_at,
            'name', OLD.name,
            'description', OLD.description
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_category_assignments_audit_insert
AFTER INSERT ON item_category_assignments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_category_assignments', 'INSERT', NEW._item_category_assignment_id,
        json_object(
            '_item_category_assignment_id', NEW._item_category_assignment_id,
            '_item_id', NEW._item_id,
            '_item_category_id', NEW._item_category_id,
            '_created_at', NEW._created_at
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_category_assignments_audit_update
AFTER UPDATE ON item_category_assignments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_category_assignments', 'UPDATE', OLD._item_category_assignment_id,
        json_patch('{}',
            CASE WHEN OLD._item_id IS NOT NEW._item_id THEN json_object('_item_id', OLD._item_id) ELSE '{}' END,
            CASE WHEN OLD._item_category_id IS NOT NEW._item_category_id THEN json_object('_item_category_id', OLD._item_category_id) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._item_id IS NOT NEW._item_id THEN json_object('_item_id', NEW._item_id) ELSE '{}' END,
            CASE WHEN OLD._item_category_id IS NOT NEW._item_category_id THEN json_object('_item_category_id', NEW._item_category_id) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_category_assignments_audit_delete
AFTER DELETE ON item_category_assignments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_category_assignments', 'DELETE', OLD._item_category_assignment_id,
        json_object(
            '_item_category_assignment_id', OLD._item_category_assignment_id,
            '_item_id', OLD._item_id,
            '_item_category_id', OLD._item_category_id,
            '_created_at', OLD._created_at
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_stocks_audit_insert
AFTER INSERT ON item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_stocks', 'INSERT', NEW._item_stock_id,
        json_object(
            '_item_stock_id', NEW._item_stock_id,
            '_item_id', NEW._item_id,
            '_item_brand_id', NEW._item_brand_id,
            '_created_at', NEW._created_at,
            'unit_price_php', NEW.unit_price_php,
            'minimum_quantity', NEW.minimum_quantity
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_stocks_audit_update
AFTER UPDATE ON item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_stocks', 'UPDATE', OLD._item_stock_id,
        json_patch('{}',
            CASE WHEN OLD._item_id IS NOT NEW._item_id THEN json_object('_item_id', OLD._item_id) ELSE '{}' END,
            CASE WHEN OLD._item_brand_id IS NOT NEW._item_brand_id THEN json_object('_item_brand_id', OLD._item_brand_id) ELSE '{}' END,
            CASE WHEN OLD.unit_price_php IS NOT NEW.unit_price_php THEN json_object('unit_price_php', OLD.unit_price_php) ELSE '{}' END,
            CASE WHEN OLD.minimum_quantity IS NOT NEW.minimum_quantity THEN json_object('minimum_quantity', OLD.minimum_quantity) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._item_id IS NOT NEW._item_id THEN json_object('_item_id', NEW._item_id) ELSE '{}' END,
            CASE WHEN OLD._item_brand_id IS NOT NEW._item_brand_id THEN json_object('_item_brand_id', NEW._item_brand_id) ELSE '{}' END,
            CASE WHEN OLD.unit_price_php IS NOT NEW.unit_price_php THEN json_object('unit_price_php', NEW.unit_price_php) ELSE '{}' END,
            CASE WHEN OLD.minimum_quantity IS NOT NEW.minimum_quantity THEN json_object('minimum_quantity', NEW.minimum_quantity) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_stocks_audit_delete
AFTER DELETE ON item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_stocks', 'DELETE', OLD._item_stock_id,
        json_object(
            '_item_stock_id', OLD._item_stock_id,
            '_item_id', OLD._item_id,
            '_item_brand_id', OLD._item_brand_id,
            '_created_at', OLD._created_at,
            'unit_price_php', OLD.unit_price_php,
            'minimum_quantity', OLD.minimum_quantity
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_stock_storage_locations_audit_insert
AFTER INSERT ON item_stock_storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_stock_storage_locations', 'INSERT', NEW._item_stock_storage_location_id,
        json_object(
            '_item_stock_storage_location_id', NEW._item_stock_storage_location_id,
            '_item_stock_id', NEW._item_stock_id,
            '_storage_location_id', NEW._storage_location_id,
            '_created_at', NEW._created_at,
            'quantity', NEW.quantity
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_stock_storage_locations_audit_update
AFTER UPDATE ON item_stock_storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_stock_storage_locations', 'UPDATE', OLD._item_stock_storage_location_id,
        json_patch('{}',
            CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN json_object('_item_stock_id', OLD._item_stock_id) ELSE '{}' END,
            CASE WHEN OLD._storage_location_id IS NOT NEW._storage_location_id THEN json_object('_storage_location_id', OLD._storage_location_id) ELSE '{}' END,
            CASE WHEN OLD.quantity IS NOT NEW.quantity THEN json_object('quantity', OLD.quantity) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN json_object('_item_stock_id', NEW._item_stock_id) ELSE '{}' END,
            CASE WHEN OLD._storage_location_id IS NOT NEW._storage_location_id THEN json_object('_storage_location_id', NEW._storage_location_id) ELSE '{}' END,
            CASE WHEN OLD.quantity IS NOT NEW.quantity THEN json_object('quantity', NEW.quantity) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_stock_storage_locations_audit_delete
AFTER DELETE ON item_stock_storage_locations
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_stock_storage_locations', 'DELETE', OLD._item_stock_storage_location_id,
        json_object(
            '_item_stock_storage_location_id', OLD._item_stock_storage_location_id,
            '_item_stock_id', OLD._item_stock_id,
            '_storage_location_id', OLD._storage_location_id,
            '_created_at', OLD._created_at,
            'quantity', OLD.quantity
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_restocks_audit_insert
AFTER INSERT ON item_restocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES ('item_restocks', 'INSERT', NEW._item_restock_id,
        json_object(
            '_item_restock_id', NEW._item_restock_id,
            '_item_stock_id', NEW._item_stock_id,
            '_created_at', NEW._created_at,
            'quantity_before', NEW.quantity_before,
            'quantity_after', NEW.quantity_after,
            'quantity_added', NEW.quantity_added
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_restocks_audit_update
AFTER UPDATE ON item_restocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES ('item_restocks', 'UPDATE', OLD._item_restock_id,
        json_patch('{}',
            CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN json_object('_item_stock_id', OLD._item_stock_id) ELSE '{}' END,
            CASE WHEN OLD.quantity_before IS NOT NEW.quantity_before THEN json_object('quantity_before', OLD.quantity_before) ELSE '{}' END,
            CASE WHEN OLD.quantity_after IS NOT NEW.quantity_after THEN json_object('quantity_after', OLD.quantity_after) ELSE '{}' END,
            CASE WHEN OLD.quantity_added IS NOT NEW.quantity_added THEN json_object('quantity_added', OLD.quantity_added) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN json_object('_item_stock_id', NEW._item_stock_id) ELSE '{}' END,
            CASE WHEN OLD.quantity_before IS NOT NEW.quantity_before THEN json_object('quantity_before', NEW.quantity_before) ELSE '{}' END,
            CASE WHEN OLD.quantity_after IS NOT NEW.quantity_after THEN json_object('quantity_after', NEW.quantity_after) ELSE '{}' END,
            CASE WHEN OLD.quantity_added IS NOT NEW.quantity_added THEN json_object('quantity_added', NEW.quantity_added) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS item_restocks_audit_delete
AFTER DELETE ON item_restocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_restocks', 'DELETE', OLD._item_restock_id,
        json_object(
            '_item_restock_id', OLD._item_restock_id,
            '_item_stock_id', OLD._item_stock_id,
            '_created_at', OLD._created_at,
            'quantity_before', OLD.quantity_before,
            'quantity_after', OLD.quantity_after,
            'quantity_added', OLD.quantity_added
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS suppliers_audit_insert
AFTER INSERT ON suppliers
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'suppliers', 'INSERT', NEW._supplier_id,
        json_object(
            '_supplier_id', NEW._supplier_id,
            '_created_at', NEW._created_at,
            'name', NEW.name,
            'email', NEW.email,
            'street', NEW.street,
            'city', NEW.city,
            'state', NEW.state,
            'postal_code', NEW.postal_code,
            'country', NEW.country
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS suppliers_audit_update
AFTER UPDATE ON suppliers
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'suppliers', 'UPDATE', OLD._supplier_id,
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', OLD.name) ELSE '{}' END,
            CASE WHEN OLD.email IS NOT NEW.email THEN json_object('email', OLD.email) ELSE '{}' END,
            CASE WHEN OLD.street IS NOT NEW.street THEN json_object('street', OLD.street) ELSE '{}' END,
            CASE WHEN OLD.city IS NOT NEW.city THEN json_object('city', OLD.city) ELSE '{}' END,
            CASE WHEN OLD.state IS NOT NEW.state THEN json_object('state', OLD.state) ELSE '{}' END,
            CASE WHEN OLD.postal_code IS NOT NEW.postal_code THEN json_object('postal_code', OLD.postal_code) ELSE '{}' END,
            CASE WHEN OLD.country IS NOT NEW.country THEN json_object('country', OLD.country) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD.name IS NOT NEW.name THEN json_object('name', NEW.name) ELSE '{}' END,
            CASE WHEN OLD.email IS NOT NEW.email THEN json_object('email', NEW.email) ELSE '{}' END,
            CASE WHEN OLD.street IS NOT NEW.street THEN json_object('street', NEW.street) ELSE '{}' END,
            CASE WHEN OLD.city IS NOT NEW.city THEN json_object('city', NEW.city) ELSE '{}' END,
            CASE WHEN OLD.state IS NOT NEW.state THEN json_object('state', NEW.state) ELSE '{}' END,
            CASE WHEN OLD.postal_code IS NOT NEW.postal_code THEN json_object('postal_code', NEW.postal_code) ELSE '{}' END,
            CASE WHEN OLD.country IS NOT NEW.country THEN json_object('country', NEW.country) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS suppliers_audit_delete
AFTER DELETE ON suppliers
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'suppliers', 'DELETE', OLD._supplier_id,
        json_object(
            '_supplier_id', OLD._supplier_id,
            '_created_at', OLD._created_at,
            'name', OLD.name,
            'email', OLD.email,
            'street', OLD.street,
            'city', OLD.city,
            'state', OLD.state,
            'postal_code', OLD.postal_code,
            'country', OLD.country
        )
    );
END $$


CREATE TRIGGER IF NOT EXISTS purchases_audit_insert
AFTER INSERT ON purchases
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'purchases', 'INSERT', NEW._purchase_id,
        json_object(
            '_purchase_id', NEW._purchase_id,
            '_supplier_id', NEW._supplier_id,
            '_created_at', NEW._created_at,
            'purchase_date', NEW.purchase_date,
            'purchase_code', NEW.purchase_code,
            'delivery_date', NEW.delivery_date,
            'vat_percent', NEW.vat_percent,
            'discount_value', NEW.discount_value,
            'discount_type', NEW.discount_type
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS purchases_audit_update
AFTER UPDATE ON purchases
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'purchases', 'UPDATE', OLD._purchase_id,
        json_patch('{}',
            CASE WHEN OLD._supplier_id IS NOT NEW._supplier_id THEN json_object('_supplier_id', OLD._supplier_id) ELSE '{}' END,
            CASE WHEN OLD.purchase_date IS NOT NEW.purchase_date THEN json_object('purchase_date', OLD.purchase_date) ELSE '{}' END,
            CASE WHEN OLD.purchase_code IS NOT NEW.purchase_code THEN json_object('purchase_code', OLD.purchase_code) ELSE '{}' END,
            CASE WHEN OLD.delivery_date IS NOT NEW.delivery_date THEN json_object('delivery_date', OLD.delivery_date) ELSE '{}' END,
            CASE WHEN OLD.vat_percent IS NOT NEW.vat_percent THEN json_object('vat_percent', OLD.vat_percent) ELSE '{}' END,
            CASE WHEN OLD.discount_value IS NOT NEW.discount_value THEN json_object('discount_value', OLD.discount_value) ELSE '{}' END,
            CASE WHEN OLD.discount_type IS NOT NEW.discount_type THEN json_object('discount_type', OLD.discount_type) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._supplier_id IS NOT NEW._supplier_id THEN json_object('_supplier_id', NEW._supplier_id) ELSE '{}' END,
            CASE WHEN OLD.purchase_date IS NOT NEW.purchase_date THEN json_object('purchase_date', NEW.purchase_date) ELSE '{}' END,
            CASE WHEN OLD.purchase_code IS NOT NEW.purchase_code THEN json_object('purchase_code', NEW.purchase_code) ELSE '{}' END,
            CASE WHEN OLD.delivery_date IS NOT NEW.delivery_date THEN json_object('delivery_date', NEW.delivery_date) ELSE '{}' END,
            CASE WHEN OLD.vat_percent IS NOT NEW.vat_percent THEN json_object('vat_percent', NEW.vat_percent) ELSE '{}' END,
            CASE WHEN OLD.discount_value IS NOT NEW.discount_value THEN json_object('discount_value', NEW.discount_value) ELSE '{}' END,
            CASE WHEN OLD.discount_type IS NOT NEW.discount_type THEN json_object('discount_type', NEW.discount_type) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS purchases_audit_delete
AFTER DELETE ON purchases
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'purchases', 'DELETE', OLD._purchase_id,
        json_object(
            '_purchase_id', OLD._purchase_id,
            '_supplier_id', OLD._supplier_id,
            '_created_at', OLD._created_at,
            'purchase_date', OLD.purchase_date,
            'purchase_code', OLD.purchase_code,
            'delivery_date', OLD.delivery_date,
            'vat_percent', OLD.vat_percent,
            'discount_value', OLD.discount_value,
            'discount_type', OLD.discount_type
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS purchase_payments_audit_insert
AFTER INSERT ON purchase_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'purchase_payments', 'INSERT', NEW._purchase_payment_id,
        json_object(
            '_purchase_payment_id', NEW._purchase_payment_id,
            '_purchse_id', NEW._purchse_id,
            '_created_at', NEW._created_at,
            'payment_date', NEW.payment_date,
            'reference_number', NEW.reference_number,
            'payment_method', NEW.payment_method,
            'amount_php', NEW.amount_php
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS purchase_payments_audit_update
AFTER UPDATE ON purchase_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'purchase_payments', 'UPDATE', OLD._purchase_payment_id,
        json_patch('{}',
            CASE WHEN OLD._purchse_id IS NOT NEW._purchse_id THEN json_object('_purchse_id', OLD._purchse_id) ELSE '{}' END,
            CASE WHEN OLD.payment_date IS NOT NEW.payment_date THEN json_object('payment_date', OLD.payment_date) ELSE '{}' END,
            CASE WHEN OLD.reference_number IS NOT NEW.reference_number THEN json_object('reference_number', OLD.reference_number) ELSE '{}' END,
            CASE WHEN OLD.payment_method IS NOT NEW.payment_method THEN json_object('payment_method', OLD.payment_method) ELSE '{}' END,
            CASE WHEN OLD.amount_php IS NOT NEW.amount_php THEN json_object('amount_php', OLD.amount_php) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._purchse_id IS NOT NEW._purchse_id THEN json_object('_purchse_id', NEW._purchse_id) ELSE '{}' END,
            CASE WHEN OLD.payment_date IS NOT NEW.payment_date THEN json_object('payment_date', NEW.payment_date) ELSE '{}' END,
            CASE WHEN OLD.reference_number IS NOT NEW.reference_number THEN json_object('reference_number', NEW.reference_number) ELSE '{}' END,
            CASE WHEN OLD.payment_method IS NOT NEW.payment_method THEN json_object('payment_method', NEW.payment_method) ELSE '{}' END,
            CASE WHEN OLD.amount_php IS NOT NEW.amount_php THEN json_object('amount_php', NEW.amount_php) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS purchase_payments_audit_delete
AFTER DELETE ON purchase_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'purchase_payments', 'DELETE', OLD._purchase_payment_id,
        json_object(
            '_purchase_payment_id', OLD._purchase_payment_id,
            '_purchse_id', OLD._purchse_id,
            '_created_at', OLD._created_at,
            'payment_date', OLD.payment_date,
            'reference_number', OLD.reference_number,
            'payment_method', OLD.payment_method,
            'amount_php', OLD.amount_php
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS purchase_item_stocks_audit_insert
AFTER INSERT ON purchase_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'purchase_item_stocks', 'INSERT', NEW._purchase_item_stock_id,
        json_object(
            '_purchase_item_stock_id', NEW._purchase_item_stock_id,
            '_purchase_id', NEW._purchase_id,
            '_item_stock_id', NEW._item_stock_id,
            '_created_at', NEW._created_at,
            'quantity_ordered', NEW.quantity_ordered,
            'quantity_received', NEW.quantity_received,
            'unit_cost_php', NEW.unit_cost_php
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS purchase_item_stocks_audit_update
AFTER UPDATE ON purchase_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'purchase_item_stocks', 'UPDATE', OLD._purchase_item_stock_id,
        json_patch('{}',
            CASE WHEN OLD._purchase_id IS NOT NEW._purchase_id THEN json_object('_purchase_id', OLD._purchase_id) ELSE '{}' END,
            CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN json_object('_item_stock_id', OLD._item_stock_id) ELSE '{}' END,
            CASE WHEN OLD.quantity_ordered IS NOT NEW.quantity_ordered THEN json_object('quantity_ordered', OLD.quantity_ordered) ELSE '{}' END,
            CASE WHEN OLD.quantity_received IS NOT NEW.quantity_received THEN json_object('quantity_received', OLD.quantity_received) ELSE '{}' END,
            CASE WHEN OLD.unit_cost_php IS NOT NEW.unit_cost_php THEN json_object('unit_cost_php', OLD.unit_cost_php) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._purchase_id IS NOT NEW._purchase_id THEN json_object('_purchase_id', NEW._purchase_id) ELSE '{}' END,
            CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN json_object('_item_stock_id', NEW._item_stock_id) ELSE '{}' END,
            CASE WHEN OLD.quantity_ordered IS NOT NEW.quantity_ordered THEN json_object('quantity_ordered', NEW.quantity_ordered) ELSE '{}' END,
            CASE WHEN OLD.quantity_received IS NOT NEW.quantity_received THEN json_object('quantity_received', NEW.quantity_received) ELSE '{}' END,
            CASE WHEN OLD.unit_cost_php IS NOT NEW.unit_cost_php THEN json_object('unit_cost_php', NEW.unit_cost_php) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS purchase_item_stocks_audit_delete
AFTER DELETE ON purchase_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'purchase_item_stocks', 'DELETE', OLD._purchase_item_stock_id,
        json_object(
            '_purchase_item_stock_id', OLD._purchase_item_stock_id,
            '_purchase_id', OLD._purchase_id,
            '_item_stock_id', OLD._item_stock_id,
            '_created_at', OLD._created_at,
            'quantity_ordered', OLD.quantity_ordered,
            'quantity_received', OLD.quantity_received,
            'unit_cost_php', OLD.unit_cost_php
        )
    );
END $$


CREATE TRIGGER IF NOT EXISTS sales_audit_insert
AFTER INSERT ON sales
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'sales', 'INSERT', NEW._sale_id,
        json_object(
            '_sale_id', NEW._sale_id,
            '_created_at', NEW._created_at,
            'sale_date', NEW.sale_date,
            'sale_code', NEW.sale_code,
            'customer_name', NEW.customer_name,
            'vat_percent', NEW.vat_percent,
            'discount_value', NEW.discount_value,
            'discount_type', NEW.discount_type
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sales_audit_update
AFTER UPDATE ON sales
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'sales', 'UPDATE', OLD._sale_id,
        json_patch('{}',
            CASE WHEN OLD.sale_date IS NOT NEW.sale_date THEN json_object('sale_date', OLD.sale_date) ELSE '{}' END,
            CASE WHEN OLD.sale_code IS NOT NEW.sale_code THEN json_object('sale_code', OLD.sale_code) ELSE '{}' END,
            CASE WHEN OLD.customer_name IS NOT NEW.customer_name THEN json_object('customer_name', OLD.customer_name) ELSE '{}' END,
            CASE WHEN OLD.vat_percent IS NOT NEW.vat_percent THEN json_object('vat_percent', OLD.vat_percent) ELSE '{}' END,
            CASE WHEN OLD.discount_value IS NOT NEW.discount_value THEN json_object('discount_value', OLD.discount_value) ELSE '{}' END,
            CASE WHEN OLD.discount_type IS NOT NEW.discount_type THEN json_object('discount_type', OLD.discount_type) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD.sale_date IS NOT NEW.sale_date THEN json_object('sale_date', NEW.sale_date) ELSE '{}' END,
            CASE WHEN OLD.sale_code IS NOT NEW.sale_code THEN json_object('sale_code', NEW.sale_code) ELSE '{}' END,
            CASE WHEN OLD.customer_name IS NOT NEW.customer_name THEN json_object('customer_name', NEW.customer_name) ELSE '{}' END,
            CASE WHEN OLD.vat_percent IS NOT NEW.vat_percent THEN json_object('vat_percent', NEW.vat_percent) ELSE '{}' END,
            CASE WHEN OLD.discount_value IS NOT NEW.discount_value THEN json_object('discount_value', NEW.discount_value) ELSE '{}' END,
            CASE WHEN OLD.discount_type IS NOT NEW.discount_type THEN json_object('discount_type', NEW.discount_type) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sales_audit_delete
AFTER DELETE ON sales
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'sales', 'DELETE', OLD._sale_id,
        json_object(
            '_sale_id', OLD._sale_id,
            '_created_at', OLD._created_at,
            'sale_date', OLD.sale_date,
            'sale_code', OLD.sale_code,
            'customer_name', OLD.customer_name,
            'vat_percent', OLD.vat_percent,
            'discount_value', OLD.discount_value,
            'discount_type', OLD.discount_type
        )
    );
END $$


CREATE TRIGGER IF NOT EXISTS sale_payments_audit_insert
AFTER INSERT ON sale_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'sale_payments', 'INSERT', NEW._sale_payment_id,
        json_object(
            '_sale_payment_id', NEW._sale_payment_id,
            '_sale_id', NEW._sale_id,
            '_created_at', NEW._created_at,
            'payment_date', NEW.payment_date,
            'reference_number', NEW.reference_number,
            'payment_method', NEW.payment_method,
            'amount_php', NEW.amount_php
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sale_payments_audit_update
AFTER UPDATE ON sale_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'sale_payments', 'UPDATE', OLD._sale_payment_id,
        json_patch('{}',
            CASE WHEN OLD._sale_id IS NOT NEW._sale_id THEN json_object('_sale_id', OLD._sale_id) ELSE '{}' END,
            CASE WHEN OLD.payment_date IS NOT NEW.payment_date THEN json_object('payment_date', OLD.payment_date) ELSE '{}' END,
            CASE WHEN OLD.reference_number IS NOT NEW.reference_number THEN json_object('reference_number', OLD.reference_number) ELSE '{}' END,
            CASE WHEN OLD.payment_method IS NOT NEW.payment_method THEN json_object('payment_method', OLD.payment_method) ELSE '{}' END,
            CASE WHEN OLD.amount_php IS NOT NEW.amount_php THEN json_object('amount_php', OLD.amount_php) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._sale_id IS NOT NEW._sale_id THEN json_object('_sale_id', NEW._sale_id) ELSE '{}' END,
            CASE WHEN OLD.payment_date IS NOT NEW.payment_date THEN json_object('payment_date', NEW.payment_date) ELSE '{}' END,
            CASE WHEN OLD.reference_number IS NOT NEW.reference_number THEN json_object('reference_number', NEW.reference_number) ELSE '{}' END,
            CASE WHEN OLD.payment_method IS NOT NEW.payment_method THEN json_object('payment_method', NEW.payment_method) ELSE '{}' END,
            CASE WHEN OLD.amount_php IS NOT NEW.amount_php THEN json_object('amount_php', NEW.amount_php) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sale_payments_audit_delete
AFTER DELETE ON sale_payments
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'sale_payments', 'DELETE', OLD._sale_payment_id,
        json_object(
            '_sale_payment_id', OLD._sale_payment_id,
            '_sale_id', OLD._sale_id,
            '_created_at', OLD._created_at,
            'payment_date', OLD.payment_date,
            'reference_number', OLD.reference_number,
            'payment_method', OLD.payment_method,
            'amount_php', OLD.amount_php
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sale_item_stocks_audit_insert
AFTER INSERT ON sale_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, new_data)
    VALUES (
        'sale_item_stocks', 'INSERT', NEW._sale_item_stock_id,
        json_object(
            '_sale_item_stock_id', NEW._sale_item_stock_id,
            '_sale_id', NEW._sale_id,
            '_item_stock_id', NEW._item_stock_id,
            '_created_at', NEW._created_at,
            'quantity', NEW.quantity,
            'unit_price_php', NEW.unit_price_php
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sale_item_stocks_audit_update
AFTER UPDATE ON sale_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data, new_data)
    VALUES (
        'sale_item_stocks', 'UPDATE', OLD._sale_item_stock_id,
        json_patch('{}',
            CASE WHEN OLD._sale_id IS NOT NEW._sale_id THEN json_object('_sale_id', OLD._sale_id) ELSE '{}' END,
            CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN json_object('_item_stock_id', OLD._item_stock_id) ELSE '{}' END,
            CASE WHEN OLD.quantity IS NOT NEW.quantity THEN json_object('quantity', OLD.quantity) ELSE '{}' END,
            CASE WHEN OLD.unit_price_php IS NOT NEW.unit_price_php THEN json_object('unit_price_php', OLD.unit_price_php) ELSE '{}' END
        ),
        json_patch('{}',
            CASE WHEN OLD._sale_id IS NOT NEW._sale_id THEN json_object('_sale_id', NEW._sale_id) ELSE '{}' END,
            CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN json_object('_item_stock_id', NEW._item_stock_id) ELSE '{}' END,
            CASE WHEN OLD.quantity IS NOT NEW.quantity THEN json_object('quantity', NEW.quantity) ELSE '{}' END,
            CASE WHEN OLD.unit_price_php IS NOT NEW.unit_price_php THEN json_object('unit_price_php', NEW.unit_price_php) ELSE '{}' END
        )
    );
END $$

CREATE TRIGGER IF NOT EXISTS sale_item_stocks_audit_delete
AFTER DELETE ON sale_item_stocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES (
        'sale_item_stocks', 'DELETE', OLD._sale_item_stock_id,
        json_object(
            '_sale_item_stock_id', OLD._sale_item_stock_id,
            '_sale_id', OLD._sale_id,
            '_item_stock_id', OLD._item_stock_id,
            '_created_at', OLD._created_at,
            'quantity', OLD.quantity,
            'unit_price_php', OLD.unit_price_php
        )
    );
END $$

DELIMITER ;

