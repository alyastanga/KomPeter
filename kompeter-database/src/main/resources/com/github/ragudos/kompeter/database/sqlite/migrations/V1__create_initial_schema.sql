-- ========================================================= --
-- =====                                             ======= --
-- =====                  SCHEMAS                    ======= --
-- =====                                             ======= --
-- ========================================================= --

CREATE TABLE
  roles (
    _role_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role_name TEXT NOT NULL UNIQUE,
    description TEXT
  );

CREATE TABLE
  users (
    _user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    display_name TEXT NOT NULL UNIQUE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    display_image TEXT
  );

CREATE TABLE
  accounts (
    _account_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _user_id INTEGER NOT NULL,
    password_hash TEXT NOT NULL,
    password_salt TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    FOREIGN KEY (_user_id) REFERENCES users (_user_id) ON DELETE CASCADE
  );

CREATE TABLE
  user_roles (
    _user_role_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _user_id INTEGER,
    _role_id INTEGER,
    FOREIGN KEY (_user_id) REFERENCES users (_user_id) ON DELETE CASCADE,
    FOREIGN KEY (_role_id) REFERENCES roles (_role_id) ON DELETE CASCADE,
    UNIQUE(_user_id, _role_id)
  );

CREATE TABLE
  sessions (
    _session_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _user_id INTEGER NOT NULL,
    expires_at TIMESTAMP NOT NULL DEFAULT (DATETIME('now', '+1 hour')),
    session_token TEXT NOT NULL UNIQUE,
    ip_address TEXT,
    FOREIGN KEY (_user_id) REFERENCES users (_user_id) ON DELETE CASCADE
  );

CREATE TABLE 
	storage_locations(
		_storage_location_id INTEGER PRIMARY KEY AUTOINCREMENT,
        _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        name TEXT NOT NULL UNIQUE,
        description TEXT
	);

CREATE TABLE
  item_categories (
    _item_category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE,
    description TEXT
  );

CREATE TABLE
  item_brands (
    _item_brand_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE,
    description TEXT
  );

CREATE TABLE
  items (
    _item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE,
    description TEXT,
    display_image TEXT
  );

CREATE TABLE
  item_category_assignments (
    _item_category_assignment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _item_id INTEGER,
    _item_category_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (_item_id) REFERENCES items (_item_id) ON DELETE CASCADE,
    FOREIGN KEY (_item_category_id) REFERENCES item_categories (_item_category_id) ON DELETE CASCADE,
    UNIQUE(_item_id, _item_category_id)
  );

CREATE TABLE
  item_stocks (
    _item_stock_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _item_id INTEGER NOT NULL,
    _item_brand_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status TEXT CHECK(status IN ('active', 'inactive', 'archived')) DEFAULT 'active',
    unit_price_php REAL NOT NULL,
    minimum_quantity INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (_item_id) REFERENCES items (_item_id) ON DELETE CASCADE,
    FOREIGN KEY (_item_brand_id) REFERENCES item_brands (_item_brand_id) ON DELETE CASCADE,
    UNIQUE(_item_id, _item_brand_id)
  );

CREATE TABLE
	item_stock_storage_locations(
		_item_stock_storage_location_id INTEGER PRIMARY KEY AUTOINCREMENT,
        _item_stock_id INTEGER NOT NULL,
        _storage_location_id INTEGER NOT NULL,
        _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        quantity INTEGER NOT NULL DEFAULT 0,
        FOREIGN KEY (_item_stock_id) REFERENCES item_stocks (_item_stock_id) ON DELETE CASCADE,
    	FOREIGN KEY (_storage_location_id) REFERENCES storage_locations (_storage_location_id) ON DELETE CASCADE,
        UNIQUE(_item_stock_id, _storage_location_id)
	);
    
CREATE TABLE
  item_restocks (
    _item_restock_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _item_stock_storage_location_id INTEGER NOT NULL,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity_before INTEGER NOT NULL,
    quantity_after INTEGER NOT NULL,
    quantity_added INTEGER NOT NULL,
    FOREIGN KEY (_item_stock_storage_location_id) REFERENCES item_stock_storage_locations (_item_stock_storage_location_id) ON DELETE CASCADE
  );

CREATE TABLE
  sales (
    _sale_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sale_date TIMESTAMP NOT NULL,
    sale_code TEXT NOT NULL UNIQUE,
    customer_name TEXT,
    vat_percent REAL NOT NULL,
    discount_value REAL,
    discount_type TEXT CHECK (discount_type IN ('percentage', 'fixed'))
  );

CREATE TABLE
  sale_payments (
    _sale_payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _sale_id INTEGER NOT NULL,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_date TIMESTAMP NOT NULL,
    reference_number TEXT,
    payment_method TEXT NOT NULL CHECK (
    payment_method IN ('cash', 'gcash', 'bank_transfer')
    ),
    amount_php REAL NOT NULL,
    FOREIGN KEY (_sale_id) REFERENCES sales (_sale_id) ON DELETE CASCADE
  );

CREATE TABLE
  sale_item_stocks (
    _sale_item_stock_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _sale_id INTEGER NOT NULL,
    _item_stock_id INTEGER NOT NULL,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL,
    unit_price_php REAL NOT NULL,
    FOREIGN KEY (_sale_id) REFERENCES sales (_sale_id) ON DELETE CASCADE,
    FOREIGN KEY (_item_stock_id) REFERENCES item_stocks (_item_stock_id) ON DELETE CASCADE
  );

CREATE TABLE IF NOT EXISTS audit_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    table_name TEXT NOT NULL,
    action TEXT NOT NULL,         -- 'INSERT', 'UPDATE', 'DELETE'
    row_id INTEGER,
    old_data TEXT,
    new_data TEXT,
    changed_at TEXT DEFAULT (datetime('now'))
);

CREATE VIEW
    user_metadata
AS
    SELECT
        u._user_id
            AS _user_id,
        a.email,
        u.display_name,
        u.first_name,
        u.last_name,
        u._created_at AS _created_at,
        GROUP_CONCAT(r.role_name, ',') AS roles
    FROM
        users u
    LEFT JOIN
        accounts a
        ON
            a._user_id = u._user_id
    LEFT JOIN
        user_roles ur
        ON
            ur._user_id = u._user_id
    LEFT JOIN
        roles r
        ON
            r._role_id = ur._role_id
    GROUP BY
        u._user_id, a.email, u.display_name, u.first_name, u.last_name, u._created_at;

CREATE VIEW
    inventory_metadata
AS
    SELECT 
        item._item_id,
        item_stock._item_stock_id,
        item_stock._created_at,
        item.name AS name,
        item.description AS description,
        item.display_image,
        COALESCE(GROUP_CONCAT(DISTINCT item_category.name), '') AS categories,
        item_brand.name AS brand,
        item_stock.unit_price_php,
        item_stock.minimum_quantity,
        item_stock.status,
        json_group_array(
            json_object(
                '_itemStockStorageLocationId', item_storage_location._item_stock_storage_location_id,
                '_storageLocationId', storage_location._storage_location_id,
                '_createdAt', item_storage_location._created_at, 
                'name', storage_location.name,
                'description', storage_location.description,
                'quantity', COALESCE(item_storage_location.quantity, 0),
                'isInitialized', CASE WHEN item_storage_location._item_stock_storage_location_id IS NULL THEN 0 ELSE 1 END
            )
        ) AS item_storage_locations
    FROM
        items AS item
    INNER JOIN
        item_stocks AS item_stock
        ON
            item._item_id = item_stock._item_id
    INNER JOIN
        item_category_assignments AS item_category_assignment
        ON
            item._item_id = item_category_assignment._item_id
    INNER JOIN
        item_categories AS item_category
        ON
            item_category_assignment._item_category_id = item_category._item_category_id
    INNER JOIN
        item_brands AS item_brand
        ON
            item_stock._item_brand_id = item_brand._item_brand_id
    LEFT JOIN
        storage_locations AS storage_location
        ON 1 = 1  -- join all storage locations for each item_stock
    LEFT JOIN
        item_stock_storage_locations AS item_storage_location
        ON item_storage_location._item_stock_id = item_stock._item_stock_id
        AND item_storage_location._storage_location_id = storage_location._storage_location_id
    GROUP BY
        item._item_id,
        item_stock._item_stock_id,
        item_stock._created_at,
        item.name,
        item.description,
        item.display_image,
        item_brand.name,
        item_stock.unit_price_php,
        item_stock.minimum_quantity,
        item_stock.status
    ORDER BY
        item._item_id;

CREATE VIEW item_stock_locations_view
AS
    SELECT
        item_stock_storage_locations._item_stock_storage_location_id,
        item_stock_storage_locations._item_stock_id,
        item_stock_storage_locations._storage_location_id,
        storage_locations.name AS name,
        storage_locations.description AS description,
        item_stock_storage_locations.quantity AS quantity,
        item_stock_storage_locations._created_at AS _created_at
    FROM
        item_stock_storage_locations
    INNER JOIN
        storage_locations 
        ON
        item_stock_storage_locations._storage_location_id = storage_locations._storage_location_id
    ORDER BY
        item_stock_storage_locations._item_stock_storage_location_id;

-- ========================================================= --
-- =====                                             ======= --
-- =====             TRIGGERS & AUDITING             ======= --
-- =====                                             ======= --
-- ========================================================= --

DELIMITER $$

CREATE TRIGGER trg_item_stock_storage_location_restock
AFTER UPDATE OF quantity ON item_stock_storage_locations
FOR EACH ROW
WHEN NEW.quantity > OLD.quantity
BEGIN
    INSERT INTO item_restocks (
        _item_stock_storage_location_id,
        quantity_before,
        quantity_after,
        quantity_added
    )
    VALUES (
        NEW._item_stock_storage_location_id,
        OLD.quantity,
        NEW.quantity,
        NEW.quantity - OLD.quantity
    );
END;

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
            json_object(
                'role_name', CASE WHEN OLD.role_name IS NOT NEW.role_name THEN OLD.role_name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN OLD.description ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                'role_name', CASE WHEN OLD.role_name IS NOT NEW.role_name THEN NEW.role_name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN NEW.description ELSE NULL END
            )
        )
    );
END;


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
            json_object(
                'display_name', CASE WHEN OLD.display_name IS NOT NEW.display_name THEN OLD.display_name ELSE NULL END,
                'first_name', CASE WHEN OLD.first_name IS NOT NEW.first_name THEN OLD.first_name ELSE NULL END,
                'last_name', CASE WHEN OLD.last_name IS NOT NEW.last_name THEN OLD.last_name ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                'display_name', CASE WHEN OLD.display_name IS NOT NEW.display_name THEN NEW.display_name ELSE NULL END,
                'first_name', CASE WHEN OLD.first_name IS NOT NEW.first_name THEN NEW.first_name ELSE NULL END,
                'last_name', CASE WHEN OLD.last_name IS NOT NEW.last_name THEN NEW.last_name ELSE NULL END
            )
        )
    );
END;


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
            json_object(
                '_user_id', CASE WHEN OLD._user_id IS NOT NEW._user_id THEN OLD._user_id ELSE NULL END,
                'password_hash', CASE WHEN OLD.password_hash IS NOT NEW.password_hash THEN OLD.password_hash ELSE NULL END,
                'password_salt', CASE WHEN OLD.password_salt IS NOT NEW.password_salt THEN OLD.password_salt ELSE NULL END,
                'email', CASE WHEN OLD.email IS NOT NEW.email THEN OLD.email ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_user_id', CASE WHEN OLD._user_id IS NOT NEW._user_id THEN NEW._user_id ELSE NULL END,
                'password_hash', CASE WHEN OLD.password_hash IS NOT NEW.password_hash THEN NEW.password_hash ELSE NULL END,
                'password_salt', CASE WHEN OLD.password_salt IS NOT NEW.password_salt THEN NEW.password_salt ELSE NULL END,
                'email', CASE WHEN OLD.email IS NOT NEW.email THEN NEW.email ELSE NULL END
            )
        )
    );
END;


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
            json_object(
                '_user_id', CASE WHEN OLD._user_id IS NOT NEW._user_id THEN OLD._user_id ELSE NULL END,
                '_role_id', CASE WHEN OLD._role_id IS NOT NEW._role_id THEN OLD._role_id ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_user_id', CASE WHEN OLD._user_id IS NOT NEW._user_id THEN NEW._user_id ELSE NULL END,
                '_role_id', CASE WHEN OLD._role_id IS NOT NEW._role_id THEN NEW._role_id ELSE NULL END
            )
        )
    );
END;


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
            json_object(
                '_user_id', CASE WHEN OLD._user_id IS NOT NEW._user_id THEN OLD._user_id ELSE NULL END,
                'session_token', CASE WHEN OLD.session_token IS NOT NEW.session_token THEN OLD.session_token ELSE NULL END,
                'ip_address', CASE WHEN OLD.ip_address IS NOT NEW.ip_address THEN OLD.ip_address ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_user_id', CASE WHEN OLD._user_id IS NOT NEW._user_id THEN NEW._user_id ELSE NULL END,
                'session_token', CASE WHEN OLD.session_token IS NOT NEW.session_token THEN NEW.session_token ELSE NULL END,
                'ip_address', CASE WHEN OLD.ip_address IS NOT NEW.ip_address THEN NEW.ip_address ELSE NULL END
            )
        )
    );
END;


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
            json_object(
                'name', CASE WHEN OLD.name IS NOT NEW.name THEN OLD.name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN OLD.description ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                'name', CASE WHEN OLD.name IS NOT NEW.name THEN NEW.name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN NEW.description ELSE NULL END
            )
        )
    );
END;


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
    VALUES (
        'item_categories', 'UPDATE', OLD._item_category_id,
        json_patch('{}',
            json_object(
                'name', CASE WHEN OLD.name IS NOT NEW.name THEN OLD.name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN OLD.description ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                'name', CASE WHEN OLD.name IS NOT NEW.name THEN NEW.name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN NEW.description ELSE NULL END
            )
        )
    );
END;


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
    VALUES (
        'item_brands', 'UPDATE', OLD._item_brand_id,
        json_patch('{}',
            json_object(
                'name', CASE WHEN OLD.name IS NOT NEW.name THEN OLD.name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN OLD.description ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                'name', CASE WHEN OLD.name IS NOT NEW.name THEN NEW.name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN NEW.description ELSE NULL END
            )
        )
    );
END;


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
    VALUES (
        'items', 'UPDATE', OLD._item_id,
        json_patch('{}',
            json_object(
                'name', CASE WHEN OLD.name IS NOT NEW.name THEN OLD.name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN OLD.description ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                'name', CASE WHEN OLD.name IS NOT NEW.name THEN NEW.name ELSE NULL END,
                'description', CASE WHEN OLD.description IS NOT NEW.description THEN NEW.description ELSE NULL END
            )
        )
    );
END;


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
    VALUES (
        'item_category_assignments', 'UPDATE', OLD._item_category_assignment_id,
        json_patch('{}',
            json_object(
                '_item_id', CASE WHEN OLD._item_id IS NOT NEW._item_id THEN OLD._item_id ELSE NULL END,
                '_item_category_id', CASE WHEN OLD._item_category_id IS NOT NEW._item_category_id THEN OLD._item_category_id ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_item_id', CASE WHEN OLD._item_id IS NOT NEW._item_id THEN NEW._item_id ELSE NULL END,
                '_item_category_id', CASE WHEN OLD._item_category_id IS NOT NEW._item_category_id THEN NEW._item_category_id ELSE NULL END
            )
        )
    );
END;


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
    VALUES (
        'item_stocks', 'UPDATE', OLD._item_stock_id,
        json_patch('{}',
            json_object(
                '_item_id', CASE WHEN OLD._item_id IS NOT NEW._item_id THEN OLD._item_id ELSE NULL END,
                '_item_brand_id', CASE WHEN OLD._item_brand_id IS NOT NEW._item_brand_id THEN OLD._item_brand_id ELSE NULL END,
                'unit_price_php', CASE WHEN OLD.unit_price_php IS NOT NEW.unit_price_php THEN OLD.unit_price_php ELSE NULL END,
                'minimum_quantity', CASE WHEN OLD.minimum_quantity IS NOT NEW.minimum_quantity THEN OLD.minimum_quantity ELSE NULL END,
                'status', CASE WHEN OLD.status IS NOT NEW.status THEN OLD.status ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_item_id', CASE WHEN OLD._item_id IS NOT NEW._item_id THEN NEW._item_id ELSE NULL END,
                '_item_brand_id', CASE WHEN OLD._item_brand_id IS NOT NEW._item_brand_id THEN NEW._item_brand_id ELSE NULL END,
                'unit_price_php', CASE WHEN OLD.unit_price_php IS NOT NEW.unit_price_php THEN NEW.unit_price_php ELSE NULL END,
                'minimum_quantity', CASE WHEN OLD.minimum_quantity IS NOT NEW.minimum_quantity THEN NEW.minimum_quantity ELSE NULL END,
                'status', CASE WHEN OLD.status IS NOT NEW.STATUS THEN NEW.status ELSE NULL END
            )
        )
    );
END;


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
    VALUES (
        'item_stock_storage_locations',
        'UPDATE',
        OLD._item_stock_storage_location_id,
        json_patch('{}',
            json_object(
                '_item_stock_id', CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN OLD._item_stock_id ELSE NULL END,
                '_storage_location_id', CASE WHEN OLD._storage_location_id IS NOT NEW._storage_location_id THEN OLD._storage_location_id ELSE NULL END,
                'quantity', CASE WHEN OLD.quantity IS NOT NEW.quantity THEN OLD.quantity ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_item_stock_id', CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN NEW._item_stock_id ELSE NULL END,
                '_storage_location_id', CASE WHEN OLD._storage_location_id IS NOT NEW._storage_location_id THEN NEW._storage_location_id ELSE NULL END,
                'quantity', CASE WHEN OLD.quantity IS NOT NEW.quantity THEN NEW.quantity ELSE NULL END
            )
        )
    );
END;


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
            '_item_stock_storage_location_id', NEW._item_stock_storage_location_id,
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
    VALUES (
        'item_restocks', 'UPDATE', OLD._item_restock_id,
        json_patch('{}',
            json_object(
                '_item_stock_storage_location_id', CASE WHEN OLD._item_stock_storage_location_id IS NOT NEW._item_stock_storage_location_id THEN OLD._item_stock_storage_location_id ELSE NULL END,
                'quantity_before', CASE WHEN OLD.quantity_before IS NOT NEW.quantity_before THEN OLD.quantity_before ELSE NULL END,
                'quantity_after', CASE WHEN OLD.quantity_after IS NOT NEW.quantity_after THEN OLD.quantity_after ELSE NULL END,
                'quantity_added', CASE WHEN OLD.quantity_added IS NOT NEW.quantity_added THEN OLD.quantity_added ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_item_stock_storage_location_id', CASE WHEN OLD._item_stock_storage_location_id IS NOT NEW._item_stock_storage_location_id THEN NEW._item_stock_storage_location_id ELSE NULL END,
                'quantity_before', CASE WHEN OLD.quantity_before IS NOT NEW.quantity_before THEN NEW.quantity_before ELSE NULL END,
                'quantity_after', CASE WHEN OLD.quantity_after IS NOT NEW.quantity_after THEN NEW.quantity_after ELSE NULL END,
                'quantity_added', CASE WHEN OLD.quantity_added IS NOT NEW.quantity_added THEN NEW.quantity_added ELSE NULL END
            )
        )
    );
END;


CREATE TRIGGER IF NOT EXISTS item_restocks_audit_delete
AFTER DELETE ON item_restocks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, row_id, old_data)
    VALUES ('item_restocks', 'DELETE', OLD._item_restock_id,
        json_object(
            '_item_restock_id', OLD._item_restock_id,
            '_item_stock_storage_location_id', OLD._item_stock_storage_location_id,
            '_created_at', OLD._created_at,
            'quantity_before', OLD.quantity_before,
            'quantity_after', OLD.quantity_after,
            'quantity_added', OLD.quantity_added
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
        'sales',
        'UPDATE',
        OLD._sale_id,
        json_patch('{}',
            json_object(
                'sale_date', CASE WHEN OLD.sale_date IS NOT NEW.sale_date THEN OLD.sale_date ELSE NULL END,
                'sale_code', CASE WHEN OLD.sale_code IS NOT NEW.sale_code THEN OLD.sale_code ELSE NULL END,
                'customer_name', CASE WHEN OLD.customer_name IS NOT NEW.customer_name THEN OLD.customer_name ELSE NULL END,
                'vat_percent', CASE WHEN OLD.vat_percent IS NOT NEW.vat_percent THEN OLD.vat_percent ELSE NULL END,
                'discount_value', CASE WHEN OLD.discount_value IS NOT NEW.discount_value THEN OLD.discount_value ELSE NULL END,
                'discount_type', CASE WHEN OLD.discount_type IS NOT NEW.discount_type THEN OLD.discount_type ELSE NULL END
            )
        ),

        json_patch('{}',
            json_object(
                'sale_date', CASE WHEN OLD.sale_date IS NOT NEW.sale_date THEN NEW.sale_date ELSE NULL END,
                'sale_code', CASE WHEN OLD.sale_code IS NOT NEW.sale_code THEN NEW.sale_code ELSE NULL END,
                'customer_name', CASE WHEN OLD.customer_name IS NOT NEW.customer_name THEN NEW.customer_name ELSE NULL END,
                'vat_percent', CASE WHEN OLD.vat_percent IS NOT NEW.vat_percent THEN NEW.vat_percent ELSE NULL END,
                'discount_value', CASE WHEN OLD.discount_value IS NOT NEW.discount_value THEN NEW.discount_value ELSE NULL END,
                'discount_type', CASE WHEN OLD.discount_type IS NOT NEW.discount_type THEN NEW.discount_type ELSE NULL END
            )
        )
    );
END;


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
            json_object(
                '_sale_id', CASE WHEN OLD._sale_id IS NOT NEW._sale_id THEN OLD._sale_id ELSE NULL END,
                'payment_date', CASE WHEN OLD.payment_date IS NOT NEW.payment_date THEN OLD.payment_date ELSE NULL END,
                'reference_number', CASE WHEN OLD.reference_number IS NOT NEW.reference_number THEN OLD.reference_number ELSE NULL END,
                'payment_method', CASE WHEN OLD.payment_method IS NOT NEW.payment_method THEN OLD.payment_method ELSE NULL END,
                'amount_php', CASE WHEN OLD.amount_php IS NOT NEW.amount_php THEN OLD.amount_php ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_sale_id', CASE WHEN OLD._sale_id IS NOT NEW._sale_id THEN NEW._sale_id ELSE NULL END,
                'payment_date', CASE WHEN OLD.payment_date IS NOT NEW.payment_date THEN NEW.payment_date ELSE NULL END,
                'reference_number', CASE WHEN OLD.reference_number IS NOT NEW.reference_number THEN NEW.reference_number ELSE NULL END,
                'payment_method', CASE WHEN OLD.payment_method IS NOT NEW.payment_method THEN NEW.payment_method ELSE NULL END,
                'amount_php', CASE WHEN OLD.amount_php IS NOT NEW.amount_php THEN NEW.amount_php ELSE NULL END
            )
        )
    );
END;


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
            json_object(
                '_sale_id', CASE WHEN OLD._sale_id IS NOT NEW._sale_id THEN OLD._sale_id ELSE NULL END,
                '_item_stock_id', CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN OLD._item_stock_id ELSE NULL END,
                'quantity', CASE WHEN OLD.quantity IS NOT NEW.quantity THEN OLD.quantity ELSE NULL END,
                'unit_price_php', CASE WHEN OLD.unit_price_php IS NOT NEW.unit_price_php THEN OLD.unit_price_php ELSE NULL END
            )
        ),
        json_patch('{}',
            json_object(
                '_sale_id', CASE WHEN OLD._sale_id IS NOT NEW._sale_id THEN NEW._sale_id ELSE NULL END,
                '_item_stock_id', CASE WHEN OLD._item_stock_id IS NOT NEW._item_stock_id THEN NEW._item_stock_id ELSE NULL END,
                'quantity', CASE WHEN OLD.quantity IS NOT NEW.quantity THEN NEW.quantity ELSE NULL END,
                'unit_price_php', CASE WHEN OLD.unit_price_php IS NOT NEW.unit_price_php THEN NEW.unit_price_php ELSE NULL END
            )
        )
    );
END;


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

