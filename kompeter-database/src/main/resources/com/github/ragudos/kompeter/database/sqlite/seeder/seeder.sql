-- SQLite Seeding Script (Final Comprehensive Version - Updated for Storage & Inventory Split)
-- ===============================================
-- 1. USERS, ROLES, and ACCOUNTS
-- ===============================================

-- Roles (6 rows)
INSERT INTO roles (_role_id, role_name, description) VALUES
(1, 'admin', 'Full system access and management privileges.'),
(2, 'manager', 'Manages staff, inventory, and purchases.'),
(3, 'cashier', 'Processes point-of-sale transactions.'),
(4, 'inventory clerk', 'Manages and updates inventory records.'),
(5, 'auditor', 'Performs financial and stock audits.'),
(6, 'supplier', 'External role for supplier data, not a direct system user.');

-- Users (5 rows)
INSERT INTO users (_user_id, display_name, first_name, last_name) VALUES
(1, 'Peter', 'Peter', 'Parker'),
(2, 'Hanz', 'Hanz', 'Zimmer'),
(3, 'Jerick', 'Jerick', 'Serrano'),
(4, 'Aaron', 'Aaron', 'Cruz'),
(5, 'Kurt', 'Kurt', 'Cobain');

-- Accounts (5 rows)
INSERT INTO accounts (_account_id, _user_id, password_hash, password_salt, email) VALUES
(1, 1, '5uvOnZhz4SAcX6boOpKHhST84HgcLB3ShFHlK9WpsTI=', 'JssptbrdHjp0sRP9Hup0cw==', 'peter.admin@example.com'),
(2, 2, '5uvOnZhz4SAcX6boOpKHhST84HgcLB3ShFHlK9WpsTI=', 'JssptbrdHjp0sRP9Hup0cw==', 'hanz.manager@example.com'),
(3, 3, '5uvOnZhz4SAcX6boOpKHhST84HgcLB3ShFHlK9WpsTI=', 'JssptbrdHjp0sRP9Hup0cw==', 'jerick.cashier@example.com'),
(4, 4, '5uvOnZhz4SAcX6boOpKHhST84HgcLB3ShFHlK9WpsTI=', 'JssptbrdHjp0sRP9Hup0cw==', 'aaron.clerk@example.com'),
(5, 5, '5uvOnZhz4SAcX6boOpKHhST84HgcLB3ShFHlK9WpsTI=', 'JssptbrdHjp0sRP9Hup0cw==', 'kurt.auditor@example.com');

-- User Roles (5 rows)
INSERT INTO user_roles (_user_role_id, _user_id, _role_id) VALUES
(1, 1, 1), 
(2, 2, 2), 
(3, 3, 3), 
(4, 4, 4), 
(5, 5, 5); 

-- Sessions (4 rows)
INSERT INTO sessions (_user_id, session_token, ip_address) VALUES
(1, 'token-peter-admin-001', '192.168.1.10'),
(2, 'token-hanz-manager-002', '192.168.1.11'),
(3, 'token-jerick-cashier-003', '10.0.0.5'),
(4, 'token-aaron-clerk-004', '10.0.0.6');

-- ===============================================
-- 2. INVENTORY SETUP & STORAGE (70 Items, 10 Brands, 4 Locations)
-- ===============================================

-- Storage Locations (4 rows)
INSERT INTO storage_locations (_storage_location_id, name, description) VALUES
(1, 'Kompeter Display Floor', 'High visibility area for quick sales items.'),
(2, 'Warehouse A', 'PC components - Primary storage for large components and bulk items.'),
(3, 'Warehouse B', 'Laptops and Peripherals - Secure storage for high-value and packaged units.'),
(4, 'Receiving Dock', 'Temporary location for newly arrived items.');

-- Item Categories (4 rows)
INSERT INTO item_categories (_item_category_id, name, description) VALUES
(1, 'PC Components', 'Internal parts like CPU, GPU, RAM, SSD, etc.'),
(2, 'Peripherals', 'External devices like Keyboard, Mouse, Monitor, Speakers.'),
(3, 'Laptops', 'Complete portable computer systems.'),
(4, 'Accessories', 'Cables, cleaning kits, mousepads, software, etc.');

-- Item Brands (10 rows)
INSERT INTO item_brands (_item_brand_id, name, description) VALUES
(1, 'Logitech', 'Leading brand for computer peripherals.'),
(2, 'Kingston', 'Specializing in memory and storage products.'),
(3, 'AMD', 'Manufacturer of CPUs and GPUs.'),
(4, 'ASUS', 'Manufacturer of motherboards, laptops, and GPUs.'),
(5, 'Samsung', 'Brand for storage devices and monitors.'),
(6, 'Intel', 'Manufacturer of CPUs and chipsets.'),
(7, 'NVIDIA', 'Designer of GeForce GPUs.'),
(8, 'Cooler Master', 'Manufacturer of cases, cooling, and power supplies.'),
(9, 'HyperX', 'Gaming peripherals and memory.'),
(10, 'Dell', 'Major manufacturer of PCs, laptops, and monitors.');

-- Items (70 rows)
INSERT INTO items (_item_id, name, description) VALUES
(1, 'Ryzen 9 7950X3D CPU', 'High-end gaming/productivity processor.'), (2, 'Ryzen 5 7600X CPU', 'Mid-range desktop processor.'),
(3, 'GeForce RTX 4070 GPU', 'High-performance graphics card.'), (4, 'GeForce RTX 4090 GPU', 'Flagship graphics card.'),
(5, 'HyperX Fury 16GB RAM', '16GB DDR4 RAM stick.'), (6, 'Corsair Vengeance 32GB RAM', '32GB DDR5 high-speed memory kit.'),
(7, 'Samsung 990 Pro 2TB SSD', 'High-speed 2TB NVMe M.2 drive.'), (8, 'Crucial P5 Plus 1TB SSD', '1TB NVMe M.2 drive.'),
(9, 'WD Blue 4TB HDD', '4TB internal Hard Disk Drive.'), (10, 'Seagate Barracuda 1TB HDD', 'Standard 1TB internal Hard Disk Drive.'),
(11, 'ROG STRIX B650 Motherboard', 'ATX Motherboard for AMD Ryzen CPUs.'), (12, 'MSI MAG B760 Motherboard', 'mATX Motherboard for Intel CPUs.'),
(13, 'Corsair 750W Power Supply', 'Modular 750 Watt Gold-rated PSU.'), (14, 'EVGA 1000W Power Supply', 'Modular 1000 Watt Platinum-rated PSU.'),
(15, 'Lian Li Lancool 205 Case', 'Mid-tower PC case with tempered glass.'), (16, 'NZXT H7 Flow Case', 'Mid-tower case optimized for airflow.'),
(17, 'Noctua NH-U12S CPU Cooler', 'Premium single-tower CPU air cooler.'), (18, 'Corsair iCUE H150i AIO Cooler', '360mm All-in-One Liquid CPU Cooler.'),
(19, 'Arctic P12 PWM Fan 5-Pack', 'Five 120mm case cooling fans.'), (20, 'PCIe Wi-Fi 6 Adapter', 'Internal Wi-Fi and Bluetooth card.'),
(21, 'G Pro X Mechanical Keyboard', 'Tenkeyless mechanical gaming keyboard.'), (22, 'Razer BlackWidow V3 Keyboard', 'Full-size mechanical keyboard.'),
(23, 'Razer Viper Mini Mouse', 'Ultra-lightweight gaming mouse.'), (24, 'Logitech G502 Hero Mouse', 'High-performance wired gaming mouse.'),
(25, 'LG UltraGear 27 Monitor', '27-inch 1440p 144Hz gaming monitor.'), (26, 'Samsung Odyssey G9 Monitor', '49-inch ultrawide curved gaming monitor.'),
(27, 'Logitech C920 Webcam', 'Full HD 1080p desktop webcam.'), (28, 'Elgato Facecam', 'Professional streaming webcam.'),
(29, 'HyperX Cloud II Headset', 'Wired gaming headset with 7.1 surround sound.'), (30, 'SteelSeries Arctis Nova Pro', 'Wireless high-fidelity gaming headset.'),
(31, 'Logitech Z623 Speakers', '2.1 Speaker system with subwoofer.'), (32, 'Creative Pebble Speakers', 'Compact USB-powered desktop speakers.'),
(33, 'Blue Yeti Microphone', 'USB condenser microphone for streaming/podcasting.'), (34, 'FIFINE K669B Microphone', 'Budget USB microphone.'),
(35, 'Wacom Intuos S Tablet', 'Small digital drawing tablet.'),
(36, 'ZenBook 14 Laptop (i7)', 'Slim 14-inch productivity laptop with Core i7.'), (37, 'ZenBook 14 Laptop (i5)', 'Slim 14-inch productivity laptop with Core i5.'),
(38, 'ROG Zephyrus G14 Laptop (4070)', 'High-end gaming laptop with RTX 4070.'), (39, 'ROG Zephyrus G14 Laptop (4060)', 'Mid-range gaming laptop with RTX 4060.'),
(40, 'TUF Gaming A15 Laptop', 'Durable mid-range gaming laptop.'), (41, 'Acer Aspire 5 Laptop (i5)', 'Budget-friendly 15-inch productivity laptop.'),
(42, 'Acer Nitro 5 Laptop', 'Entry-level gaming laptop.'), (43, 'Dell XPS 13 (2024)', 'Premium 13-inch ultraportable laptop.'),
(44, 'Dell Inspiron 15', 'Standard 15-inch budget laptop.'), (45, 'HP Spectre x360 14', 'Convertible 2-in-1 premium laptop.'),
(46, 'HP Pavilion Aero 13', 'Lightweight 13-inch laptop.'), (47, 'Lenovo Legion 5 Pro', 'Powerful 16-inch gaming laptop.'),
(48, 'Lenovo IdeaPad Gaming 3', 'Affordable gaming laptop.'), (49, 'MacBook Air M3 13-inch (8GB)', 'Apple M3 chip ultraportable.'),
(50, 'MacBook Pro M3 Pro 14-inch (18GB)', 'Apple M3 Pro chip workstation.'), (51, 'Microsoft Surface Laptop 5 13.5', 'Sleek 13.5-inch Windows laptop.'),
(52, 'MSI Stealth 16 Studio', 'Thin and powerful professional/gaming laptop.'), (53, 'Gigabyte Aero 16 OLED', 'High-resolution OLED screen for creative work.'),
(54, 'Alienware m18 Gaming Laptop', 'Large 18-inch high-end gaming laptop.'), (55, 'Chromebook Duet 5', 'Budget 2-in-1 Chrome OS tablet/laptop.'),
(56, 'USB-C to HDMI Adapter', 'Adapter for connecting modern laptops to HDMI displays.'), (57, 'Arctic MX-4 Thermal Paste', 'High-performance thermal compound for CPU/GPU.'),
(58, 'SteelSeries Mousepad XXL', 'Large cloth gaming mousepad.'), (59, 'Logitech G240 Mousepad', 'Standard cloth gaming mousepad.'),
(60, 'HDMI 2.1 Cable 2m', 'High-speed cable for 4K/120Hz displays.'), (61, 'DisplayPort 1.4 Cable 3m', 'High-speed cable for PC monitors.'),
(62, 'Velcro Cable Ties 50-pack', 'Reusable straps for cable management.'), (63, 'Compressed Air Canister', 'For dusting PC components.'),
(64, 'Screen Cleaning Kit', 'Microfiber cloth and cleaning solution.'), (65, 'Windows 11 Home License', 'Operating System license key.'),
(66, 'Microsoft Office 365 License', 'Annual subscription for Office suite.'), (67, 'Surge Protector 8-Outlet', 'Power strip for protection.'),
(68, 'External Webcam Tripod', 'Small tripod for external webcams.'), (69, 'USB-A to USB-C Hub 4-port', 'Hub for expanding port access.'),
(70, 'Laptop Backpack 15-inch', 'Padded backpack for safely carrying laptops.');


-- Item Category Assignments (70 rows - No Change)
INSERT INTO item_category_assignments (_item_category_assignment_id, _item_id, _item_category_id) VALUES
(1, 1, 1), (2, 2, 1), (3, 3, 1), (4, 4, 1), (5, 5, 1), (6, 6, 1), (7, 7, 1), (8, 8, 1), (9, 9, 1), (10, 10, 1), 
(11, 11, 1), (12, 12, 1), (13, 13, 1), (14, 14, 1), (15, 15, 1), (16, 16, 1), (17, 17, 1), (18, 18, 1), (19, 19, 1), (20, 20, 1), 
(21, 21, 2), (22, 22, 2), (23, 23, 2), (24, 24, 2), (25, 25, 2), (26, 26, 2), (27, 27, 2), (28, 28, 2), (29, 29, 2), (30, 30, 2), 
(31, 31, 2), (32, 32, 2), (33, 33, 2), (34, 34, 2), (35, 35, 2),                                                      
(36, 36, 3), (37, 37, 3), (38, 38, 3), (39, 39, 3), (40, 40, 3), (41, 41, 3), (42, 42, 3), (43, 43, 3), (44, 44, 3), (45, 45, 3), 
(46, 46, 3), (47, 47, 3), (48, 48, 3), (49, 49, 3), (50, 50, 3), (51, 51, 3), (52, 52, 3), (53, 53, 3), (54, 54, 3), (55, 55, 3), 
(56, 56, 4), (57, 57, 4), (58, 58, 4), (59, 59, 4), (60, 60, 4), (61, 61, 4), (62, 62, 4), (63, 63, 4), (64, 64, 4), (65, 65, 4), 
(66, 66, 4), (67, 67, 4), (68, 68, 4), (69, 69, 4), (70, 70, 4);                                                     

-- Item Stocks (70 rows - Metadata for items)
INSERT INTO item_stocks (_item_stock_id, _item_id, _item_brand_id, unit_price_php, minimum_quantity) VALUES
(1, 1, 3, 22000.00, 5), (2, 2, 3, 12000.00, 10), (3, 3, 7, 35000.00, 5), (4, 4, 7, 75000.00, 2), 
(5, 5, 9, 3500.00, 20), (6, 6, 2, 6000.00, 10), (7, 7, 5, 12500.00, 8), (8, 8, 2, 6000.00, 7), 
(9, 9, 2, 4000.00, 15), (10, 10, 2, 2500.00, 15), (11, 11, 4, 9000.00, 10), (12, 12, 6, 6000.00, 8), 
(13, 13, 8, 4500.00, 15), (14, 14, 8, 7000.00, 7), (15, 15, 8, 3000.00, 10), (16, 16, 8, 5000.00, 8), 
(17, 17, 8, 4500.00, 8), (18, 18, 8, 8000.00, 5), (19, 19, 8, 1500.00, 25), (20, 20, 4, 1000.00, 20), 
(21, 21, 1, 8500.00, 15), (22, 22, 9, 6000.00, 10), (23, 23, 9, 2500.00, 15), (24, 24, 1, 3500.00, 10), 
(25, 25, 5, 18000.00, 7), (26, 26, 5, 60000.00, 3), (27, 27, 1, 3500.00, 10), (28, 28, 4, 6500.00, 7), 
(29, 29, 9, 4000.00, 12), (30, 30, 9, 12000.00, 5), (31, 31, 1, 6000.00, 8), (32, 32, 1, 2500.00, 10), 
(33, 33, 1, 7000.00, 7), (34, 34, 1, 3000.00, 10), (35, 35, 4, 8000.00, 5), 
(36, 36, 4, 55000.00, 4), (37, 37, 4, 45000.00, 5), (38, 38, 4, 85000.00, 3), (39, 39, 4, 65000.00, 3), 
(40, 40, 4, 40000.00, 6), (41, 41, 4, 25000.00, 7), (42, 42, 4, 35000.00, 5), (43, 43, 10, 70000.00, 3), 
(44, 44, 10, 30000.00, 8), (45, 45, 4, 60000.00, 3), (46, 46, 4, 45000.00, 4), (47, 47, 4, 80000.00, 2), 
(48, 48, 4, 38000.00, 5), (49, 49, 4, 50000.00, 4), (50, 50, 4, 95000.00, 2), (51, 51, 4, 65000.00, 3), 
(52, 52, 4, 90000.00, 2), (53, 53, 4, 80000.00, 2), (54, 54, 10, 120000.00, 1), (55, 55, 4, 20000.00, 8), 
(56, 56, 4, 500.00, 30), (57, 57, 8, 350.00, 50), (58, 58, 9, 750.00, 20), (59, 59, 1, 500.00, 25), 
(60, 60, 4, 1000.00, 30), (61, 61, 4, 1200.00, 25), (62, 62, 4, 300.00, 75), (63, 63, 8, 450.00, 40), 
(64, 64, 4, 600.00, 25), (65, 65, 6, 7000.00, 10), (66, 66, 6, 10000.00, 8), (67, 67, 8, 1500.00, 15), 
(68, 68, 4, 400.00, 15), (69, 69, 4, 800.00, 20), (70, 70, 4, 2000.00, 10);

-- Item Stock Storage Locations (120 rows - Actual quantity by location)
INSERT INTO item_stock_storage_locations (_item_stock_storage_location_id, _item_stock_id, _storage_location_id, quantity) VALUES
-- Components (1-20) -> Warehouse A (2) + Display (1). Total QTY = 10, 50, 15, 5, 100, 40, 40, 30, 50, 60, 45, 30, 50, 25, 40, 35, 30, 20, 100, 80
(1, 1, 2, 8), (2, 1, 1, 2), (3, 2, 2, 40), (4, 2, 1, 10), (5, 3, 2, 12), (6, 3, 1, 3), (7, 4, 2, 4), (8, 4, 1, 1), 
(9, 5, 2, 80), (10, 5, 1, 20), (11, 6, 2, 35), (12, 6, 1, 5), (13, 7, 2, 32), (14, 7, 1, 8), (15, 8, 2, 25), (16, 8, 1, 5), 
(17, 9, 2, 40), (18, 9, 1, 10), (19, 10, 2, 50), (20, 10, 1, 10), (21, 11, 2, 35), (22, 11, 1, 10), (23, 12, 2, 25), (24, 12, 1, 5), 
(25, 13, 2, 40), (26, 13, 1, 10), (27, 14, 2, 20), (28, 14, 1, 5), (29, 15, 2, 30), (30, 15, 1, 10), (31, 16, 2, 28), (32, 16, 1, 7), 
(33, 17, 2, 25), (34, 17, 1, 5), (35, 18, 2, 15), (36, 18, 1, 5), (37, 19, 2, 80), (38, 19, 1, 20), (39, 20, 2, 60), (40, 20, 1, 20), 
-- Peripherals (21-35) -> Warehouse B (3) + Display (1). Total QTY = 75, 50, 60, 40, 30, 10, 55, 30, 65, 20, 40, 50, 35, 45, 25
(41, 21, 3, 60), (42, 21, 1, 15), (43, 22, 3, 40), (44, 22, 1, 10), (45, 23, 3, 50), (46, 23, 1, 10), (47, 24, 3, 30), (48, 24, 1, 10), 
(49, 25, 3, 25), (50, 25, 1, 5), (51, 26, 3, 8), (52, 26, 1, 2), (53, 27, 3, 45), (54, 27, 1, 10), (55, 28, 3, 25), (56, 28, 1, 5), 
(57, 29, 3, 50), (58, 29, 1, 15), (59, 30, 3, 15), (60, 30, 1, 5), (61, 31, 3, 30), (62, 31, 1, 10), (63, 32, 3, 40), (64, 32, 1, 10), 
(65, 33, 3, 28), (66, 33, 1, 7), (67, 34, 3, 35), (68, 34, 1, 10), (69, 35, 3, 20), (70, 35, 1, 5), 
-- Laptops (36-55) -> Warehouse B (3) only. Total QTY = 15, 20, 10, 12, 25, 35, 25, 10, 30, 10, 15, 8, 20, 15, 5, 10, 5, 6, 4, 30
(71, 36, 3, 15), (72, 37, 3, 20), (73, 38, 3, 10), (74, 39, 3, 12), (75, 40, 3, 25), (76, 41, 3, 35), (77, 42, 3, 25), (78, 43, 3, 10), 
(79, 44, 3, 30), (80, 45, 3, 10), (81, 46, 3, 15), (82, 47, 3, 8), (83, 48, 3, 20), (84, 49, 3, 15), (85, 50, 3, 5), (86, 51, 3, 10), 
(87, 52, 3, 5), (88, 53, 3, 6), (89, 54, 3, 4), (90, 55, 3, 30), 
-- Accessories (56-70) -> Display (1) + Warehouse A (2). Total QTY = 150, 200, 80, 90, 120, 110, 300, 150, 100, 50, 40, 60, 70, 90, 50
(91, 56, 1, 100), (92, 56, 2, 50), (93, 57, 1, 150), (94, 57, 2, 50), (95, 58, 1, 50), (96, 58, 2, 30), (97, 59, 1, 70), (98, 59, 2, 20), 
(99, 60, 1, 80), (100, 60, 2, 40), (101, 61, 1, 70), (102, 61, 2, 40), (103, 62, 1, 200), (104, 62, 2, 100), (105, 63, 1, 100), (106, 63, 2, 50), 
(107, 64, 1, 75), (108, 64, 2, 25), (109, 65, 1, 20), (110, 65, 2, 30), (111, 66, 1, 15), (112, 66, 2, 25), (113, 67, 1, 40), (114, 67, 2, 20), 
(115, 68, 1, 50), (116, 68, 2, 20), (117, 69, 1, 60), (118, 69, 2, 30), (119, 70, 1, 25), (120, 70, 3, 25);

-- Item Restocks (4 rows - Initial stock added)
INSERT INTO item_restocks (_item_restock_id, _item_stock_id, quantity_before, quantity_after, quantity_added) VALUES
(1, 5, 0, 100, 100), -- RAM (100 added)
(2, 2, 0, 50, 50),   -- CPU (50 added)
(3, 21, 0, 75, 75),  -- Keyboard (75 added)
(4, 37, 0, 20, 20);  -- ZenBook (20 added)

-- ===============================================
-- 3. PURCHASES (Restocking)
-- ===============================================

-- Suppliers (4 rows)
INSERT INTO suppliers (_supplier_id, name, email, street, city, state, postal_code, country) VALUES
(1, 'Component Distributors Inc.', 'contact@compdist.com', '123 Tech St', 'Makati', 'Metro Manila', '1209', 'Philippines'),
(2, 'Peripheral World', 'sales@periworld.net', '456 Gadget Ave', 'Quezon City', 'Metro Manila', '1103', 'Philippines'),
(3, 'ASUS PH Partner', 'orders@asusph.com', '789 Laptop Blvd', 'Manila', 'Metro Manila', '1008', 'Philippines'),
(4, 'Storage Solutions', 'support@storage.com', '101 Drive Rd', 'Cebu City', 'Cebu', '6000', 'Philippines');

-- Purchases (4 rows)
INSERT INTO purchases (_purchase_id, _supplier_id, purchase_date, purchase_code, delivery_date, vat_percent, discount_value, discount_type) VALUES
(1, 1, DATETIME('now', '-3 days'), 'PO-CDI-2025-001', DATETIME('now', '-2 days'), 0.12, 500.00, 'fixed'),
(2, 2, DATETIME('now', '-4 days'), 'PO-PW-2025-002', DATETIME('now', '-3 days'), 0.12, 0.05, 'percentage'),
(3, 3, DATETIME('now', '-2 days'), 'PO-APP-2025-003', NULL, 0.12, NULL, NULL), 
(4, 4, DATETIME('now', '-1 day'), 'PO-SS-2025-004', DATETIME('now'), 0.12, 0, 'fixed');

-- Purchase Payments (4 rows)
INSERT INTO purchase_payments (_purchase_payment_id, _purchse_id, payment_date, reference_number, payment_method, amount_php) VALUES
(1, 1, DATETIME('now', '-2 days', '+1 hour'), 'REF-PP-98765', 'bank_transfer', 69500.00), 
(2, 2, DATETIME('now', '-3 days', '+2 hours'), 'REF-PP-65432', 'cash', 85500.00), 
(3, 3, DATETIME('now', '-2 days', '+3 hours'), 'REF-PP-32109', 'gcash', 45000.00), 
(4, 4, DATETIME('now'), 'REF-PP-09876', 'bank_transfer', 125000.00);

-- Purchase Item Stocks (6 rows)
INSERT INTO purchase_item_stocks (_purchase_item_stock_id, _purchase_id, _item_stock_id, quantity_ordered, quantity_received, unit_cost_php) VALUES
(1, 1, 2, 5, 5, 10000.00),  -- 5 CPUs (Stock ID 2)
(2, 1, 5, 20, 20, 1000.00), -- 20 RAMs (Stock ID 5)
(3, 2, 21, 10, 10, 8000.00), -- 10 Keyboards (Stock ID 21)
(4, 2, 23, 10, 10, 2000.00),-- 10 Mice (Stock ID 23)
(5, 3, 37, 1, 0, 40000.00), -- 1 ZenBook (Stock ID 37) - Not yet received
(6, 4, 7, 10, 10, 12500.00); -- 10 SSDs (Stock ID 7)


-- ===============================================
-- 4. SALES (15 Rows over Different Days)
-- ===============================================

-- Sales (15 rows spanning multiple days)
INSERT INTO sales (_sale_id, sale_date, sale_code, customer_name, vat_percent, discount_value, discount_type) VALUES
(1,  DATETIME('now', '-14 days', '10:00:00'), 'SALE-2025-0001', 'Client A', 0.12, 0, 'fixed'),
(2,  DATETIME('now', '-14 days', '14:30:00'), 'SALE-2025-0002', 'Client B', 0.12, 0.10, 'percentage'),
(3,  DATETIME('now', '-13 days', '09:15:00'), 'SALE-2025-0003', 'Walk-in', 0.12, 100.00, 'fixed'),
(4,  DATETIME('now', '-13 days', '16:00:00'), 'SALE-2025-0004', 'Client D', 0.12, NULL, NULL),
(5,  DATETIME('now', '-12 days', '11:20:00'), 'SALE-2025-0005', 'Jane Smith', 0.12, 0.05, 'percentage'),
(6,  DATETIME('now', '-11 days', '15:45:00'), 'SALE-2025-0006', 'Technology Hub', 0.12, 500.00, 'fixed'),
(7,  DATETIME('now', '-10 days', '13:00:00'), 'SALE-2025-0007', 'Michael Lee', 0.12, 0, 'fixed'),
(8,  DATETIME('now', '-9 days', '10:30:00'), 'SALE-2025-0008', 'Gamer Corp', 0.12, 0.15, 'percentage'),
(9,  DATETIME('now', '-8 days', '17:00:00'), 'SALE-2025-0009', 'Local University', 0.12, 2000.00, 'fixed'),
(10, DATETIME('now', '-7 days', '12:00:00'), 'SALE-2025-0010', 'Walk-in', 0.12, NULL, NULL),
(11, DATETIME('now', '-6 days', '14:10:00'), 'SALE-2025-0011', 'Elias Vance', 0.12, 0.03, 'percentage'),
(12, DATETIME('now', '-5 days', '09:40:00'), 'SALE-2025-0012', 'Quick Fix IT', 0.12, 1000.00, 'fixed'),
(13, DATETIME('now', '-4 days', '18:00:00'), 'SALE-2025-0013', 'Sarah Connor', 0.12, 0, 'fixed'),
(14, DATETIME('now', '-3 days', '11:55:00'), 'SALE-2025-0014', 'Tech Enthusiast', 0.12, 0.05, 'percentage'),
(15, DATETIME('now', '-2 days', '16:30:00'), 'SALE-2025-0015', 'Mark Johnson', 0.12, 0, 'fixed');


-- Sale Item Stocks (30 rows)
INSERT INTO sale_item_stocks (_sale_item_stock_id, _sale_id, _item_stock_id, quantity, unit_price_php) VALUES
(1, 1, 5, 2, 3500.00), (2, 1, 21, 1, 8500.00),
(3, 2, 2, 1, 12000.00), (4, 2, 57, 1, 350.00),
(5, 3, 23, 1, 2500.00), (6, 3, 67, 2, 1500.00),
(7, 4, 25, 1, 18000.00), (8, 4, 56, 2, 500.00),
(9, 5, 37, 1, 45000.00), (10, 5, 69, 1, 800.00),
(11, 6, 38, 1, 85000.00), (12, 6, 7, 1, 12500.00),
(13, 7, 7, 1, 12500.00), (14, 7, 29, 1, 4000.00),
(15, 8, 43, 1, 70000.00), (16, 8, 24, 1, 3500.00),
(17, 9, 54, 1, 120000.00), (18, 9, 65, 1, 7000.00),
(19, 10, 11, 2, 9000.00), (20, 10, 13, 1, 4500.00),
(21, 11, 27, 1, 3500.00), (22, 11, 32, 1, 2500.00),
(23, 12, 56, 5, 500.00), (24, 12, 63, 5, 450.00),
(25, 13, 51, 1, 65000.00), (26, 13, 70, 1, 2000.00),
(27, 14, 33, 1, 7000.00), (28, 14, 59, 1, 500.00),
(29, 15, 3, 1, 35000.00), (30, 15, 66, 1, 10000.00);


-- Sale Payments (15 rows)
INSERT INTO sale_payments (_sale_payment_id, _sale_id, payment_date, reference_number, payment_method, amount_php) VALUES
(1, 1, DATETIME('now', '-14 days', '10:05:00'), NULL, 'cash', 17360.00), 
(2, 2, DATETIME('now', '-14 days', '14:35:00'), 'GCASH-0914', 'gcash', 12448.80), 
(3, 3, DATETIME('now', '-13 days', '09:20:00'), NULL, 'cash', 6048.00), 
(4, 4, DATETIME('now', '-13 days', '16:05:00'), 'BTRANS-0913', 'bank_transfer', 21280.00),
(5, 5, DATETIME('now', '-12 days', '11:25:00'), 'GCASH-0912', 'gcash', 48731.20),
(6, 6, DATETIME('now', '-11 days', '15:50:00'), 'BTRANS-0911', 'bank_transfer', 108640.00),
(7, 7, DATETIME('now', '-10 days', '13:05:00'), NULL, 'cash', 18480.00),
(8, 8, DATETIME('now', '-9 days', '10:35:00'), 'BTRANS-0909', 'bank_transfer', 69972.00),
(9, 9, DATETIME('now', '-8 days', '17:05:00'), 'BTRANS-0908', 'bank_transfer', 140000.00),
(10, 10, DATETIME('now', '-7 days', '12:05:00'), NULL, 'cash', 25200.00),
(11, 11, DATETIME('now', '-6 days', '14:15:00'), 'GCASH-0906', 'gcash', 6518.40),
(12, 12, DATETIME('now', '-5 days', '09:45:00'), NULL, 'cash', 4480.00),
(13, 13, DATETIME('now', '-4 days', '18:05:00'), 'BTRANS-0904', 'bank_transfer', 75040.00),
(14, 14, DATETIME('now', '-3 days', '12:00:00'), 'GCASH-0903', 'gcash', 7980.00),
(15, 15, DATETIME('now', '-2 days', '16:35:00'), NULL, 'cash', 50400.00); 
