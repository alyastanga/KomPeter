-- ENUM replacements
-- discount_types: 'percentage' | 'fixed'
-- payment_methods: 'cash' | 'gcash' | 'bank_transfer'
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
    last_name TEXT NOT NULL
  );

CREATE TABLE
  accounts (
    _account_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _user_id INTEGER NOT NULL,
    password_hash TEXT NOT NULL,
    password_salt TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    FOREIGN KEY (_user_id) REFERENCES users (_user_id)
  );

CREATE TABLE
  user_roles (
    _user_role_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _user_id INTEGER,
    _role_id INTEGER,
    FOREIGN KEY (_user_id) REFERENCES users (_user_id),
    FOREIGN KEY (_role_id) REFERENCES roles (_role_id)
  );

CREATE TABLE
  sessions (
    _session_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _user_id INTEGER NOT NULL,
    session_token TEXT NOT NULL UNIQUE,
    ip_address TEXT,
    FOREIGN KEY (_user_id) REFERENCES users (_user_id)
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
    name TEXT NOT NULL,
    description TEXT
  );

CREATE TABLE
  item_category_assignments (
    _item_category_assignment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _item_id INTEGER,
    _item_category_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (_item_id) REFERENCES items (_item_id),
    FOREIGN KEY (_item_category_id) REFERENCES item_categories (_item_category_id)
  );

CREATE TABLE
  item_stocks (
    _item_stock_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _item_id INTEGER NOT NULL,
    _item_brand_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unit_price_php REAL NOT NULL,
    quantity INTEGER NOT NULL,
    minimum_quantity INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (_item_id) REFERENCES items (_item_id),
    FOREIGN KEY (_item_brand_id) REFERENCES item_brands (_item_brand_id)
  );

CREATE TABLE
	item_stock_storage_locations(
		_item_stock_storage_location_id INTEGER PRIMARY KEY AUTOINCREMENT,
        _item_stock_id INTEGER NOT NULL,
        _storage_location_id INTEGER NOT NULL,
        _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        quantity INTEGER NOT NULL DEFAULT 0,
        FOREIGN KEY (_item_stock_id) REFERENCES item_stocks (_item_stock_id),
    	FOREIGN KEY (_storage_location_id) REFERENCES storage_locations (_storage_location_id)
	);
    
CREATE TABLE
  item_restocks (
    _item_restock_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _item_stock_id INTEGER NOT NULL,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity_before INTEGER NOT NULL,
    quantity_after INTEGER NOT NULL,
    quantity_added INTEGER NOT NULL,
    FOREIGN KEY (_item_stock_id) REFERENCES item_stocks (_item_stock_id)
  );
  
  

CREATE TABLE
  suppliers (
    _supplier_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    street TEXT,
    city TEXT,
    state TEXT,
    postal_code TEXT,
    country TEXT
  );

CREATE TABLE
  purchases (
    _purchase_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _supplier_id INTEGER NOT NULL,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    purchase_date TIMESTAMP NOT NULL,
    purchase_code TEXT NOT NULL UNIQUE,
    delivery_date TIMESTAMP,
    vat_percent REAL NOT NULL,
    discount_value REAL,
    discount_type TEXT CHECK (discount_type IN ('percentage', 'fixed')),
    FOREIGN KEY (_supplier_id) REFERENCES suppliers (_supplier_id)
  );

CREATE TABLE
  purchase_payments (
    _purchase_payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _purchase_id INTEGER NOT NULL,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_date TIMESTAMP NOT NULL,
    reference_number TEXT,
    payment_method TEXT NOT NULL CHECK (
      payment_method IN ('cash', 'gcash', 'bank_transfer')
    ),
    amount_php REAL NOT NULL,
    FOREIGN KEY (_purchase_id) REFERENCES purchases (_purchase_id)
  );

CREATE TABLE
  purchase_item_stocks (
    _purchase_item_stock_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _purchase_id INTEGER NOT NULL,
    _item_stock_id INTEGER NOT NULL,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity_ordered INTEGER NOT NULL,
    quantity_received INTEGER NOT NULL,
    unit_cost_php REAL NOT NULL,
    FOREIGN KEY (_purchase_id) REFERENCES purchases (_purchase_id),
    FOREIGN KEY (_item_stock_id) REFERENCES item_stocks (_item_stock_id)
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
    FOREIGN KEY (_sale_id) REFERENCES sales (_sale_id)
  );

CREATE TABLE
  sale_item_stocks (
    _sale_item_stock_id INTEGER PRIMARY KEY AUTOINCREMENT,
    _sale_id INTEGER NOT NULL,
    _item_stock_id INTEGER NOT NULL,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL,
    unit_price_php REAL NOT NULL,
    FOREIGN KEY (_sale_id) REFERENCES sales (_sale_id),
    FOREIGN KEY (_item_stock_id) REFERENCES item_stocks (_item_stock_id)
  );
