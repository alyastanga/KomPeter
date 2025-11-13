-- USERS, ROLES, and ACCOUNTS
INSERT INTO roles (role_name, description) VALUES
('admin', 'Full system access and management privileges.'),
('manager', 'Manages staff, inventory, and purchases.'),
('cashier', 'Processes point-of-sale transactions.'),
('inventory clerk', 'Manages and updates inventory records.'),
('auditor', 'Performs financial and stock audits.'),
('supplier', 'External role for supplier data, not a direct system user.');

INSERT INTO users (display_name, first_name, last_name, display_image) VALUES
('Peter', 'Peter', 'Parker', '/com/github/ragudos/kompeter/app/desktop/assets/images/peter.png'),
('Hanz', 'Hanz', 'Zimmer', '/com/github/ragudos/kompeter/app/desktop/assets/images/peter.png'),
('Jerick', 'Jerick', 'Serrano', '/com/github/ragudos/kompeter/app/desktop/assets/images/peter.png'),
('Aaron', 'Aaron', 'Cruz', '/com/github/ragudos/kompeter/app/desktop/assets/images/peter.png'),
('Kurt', 'Kurt', 'Cobain', '/com/github/ragudos/kompeter/app/desktop/assets/images/peter.png');

INSERT INTO accounts (_user_id, password_hash, password_salt, email) VALUES
(1, 'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=', 'LGwkiq+nsgf+R7iRlpS3kQ==', 'peter.admin@example.com'),
(2, 'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=', 'LGwkiq+nsgf+R7iRlpS3kQ==', 'hanz.manager@example.com'),
(3, 'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=', 'LGwkiq+nsgf+R7iRlpS3kQ==', 'jerick.cashier@example.com'),
(4, 'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=', 'LGwkiq+nsgf+R7iRlpS3kQ==', 'aaron.clerk@example.com'),
(5, 'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=', 'LGwkiq+nsgf+R7iRlpS3kQ==', 'kurt.auditor@example.com');

INSERT INTO user_roles (_user_id, _role_id) VALUES
(1, 1), 
(2, 2), 
(3, 3), 
(4, 4), 
(5, 5); 

-- INVENTORY SETUP & STORAGE
INSERT INTO storage_locations (name, description) VALUES
('Main Display Floor', 'High visibility area for quick sales items.'),
('Warehouse A - Components', 'Primary storage for large components and bulk items.'),
('Warehouse B - Laptops & Peripherals', 'Secure storage for high-value and packaged units.'),
('Receiving Dock', 'Temporary location for newly arrived items.');

INSERT INTO item_categories (name, description) VALUES
('PC Components', 'Internal parts like CPU, GPU, RAM, SSD, etc.'),
('Peripherals', 'External devices like Keyboard, Mouse, Monitor, Speakers.'),
('Laptops', 'Complete portable computer systems.'),
('Accessories', 'Cables, cleaning kits, mousepads, software, etc.');

INSERT INTO item_brands (name, description) VALUES
('Logitech', 'Leading brand for computer peripherals.'),
('Kingston', 'Specializing in memory and storage products.'),
('AMD', 'Manufacturer of CPUs and GPUs.'),
('ASUS', 'Manufacturer of motherboards, laptops, and GPUs.'),
('Samsung', 'Brand for storage devices and monitors.'),
('Intel', 'Manufacturer of CPUs and chipsets.'),
('NVIDIA', 'Designer of GeForce GPUs.'),
('Cooler Master', 'Manufacturer of cases, cooling, and power supplies.'),
('HyperX', 'Gaming peripherals and memory.'),
('Dell', 'Major manufacturer of PCs, laptops, and monitors.');

INSERT INTO items (name, description) VALUES
('Ryzen 9 7950X3D CPU', 'High-end gaming/productivity processor.'), ('Ryzen 5 7600X CPU', 'Mid-range desktop processor.'),
('GeForce RTX 4070 GPU', 'High-performance graphics card.'), ('GeForce RTX 4090 GPU', 'Flagship graphics card.'),
('HyperX Fury 16GB RAM', '16GB DDR4 RAM stick.'), ('Corsair Vengeance 32GB RAM', '32GB DDR5 high-speed memory kit.'),
('Samsung 990 Pro 2TB SSD', 'High-speed 2TB NVMe M.2 drive.'), ('Crucial P5 Plus 1TB SSD', '1TB NVMe M.2 drive.'),
('WD Blue 4TB HDD', '4TB internal Hard Disk Drive.'), ('Seagate Barracuda 1TB HDD', 'Standard 1TB internal Hard Disk Drive.'),
('ROG STRIX B650 Motherboard', 'ATX Motherboard for AMD Ryzen CPUs.'), ('MSI MAG B760 Motherboard', 'mATX Motherboard for Intel CPUs.'),
('Corsair 750W Power Supply', 'Modular 750 Watt Gold-rated PSU.'), ('EVGA 1000W Power Supply', 'Modular 1000 Watt Platinum-rated PSU.'),
('Lian Li Lancool 205 Case', 'Mid-tower PC case with tempered glass.'), ('NZXT H7 Flow Case', 'Mid-tower case optimized for airflow.'),
('Noctua NH-U12S CPU Cooler', 'Premium single-tower CPU air cooler.'), ('Corsair iCUE H150i AIO Cooler', '360mm All-in-One Liquid CPU Cooler.'),
('Arctic P12 PWM Fan 5-Pack', 'Five 120mm case cooling fans.'), ('PCIe Wi-Fi 6 Adapter', 'Internal Wi-Fi and Bluetooth card.'),
('G Pro X Mechanical Keyboard', 'Tenkeyless mechanical gaming keyboard.'), ('Razer BlackWidow V3 Keyboard', 'Full-size mechanical keyboard.'),
('Razer Viper Mini Mouse', 'Ultra-lightweight gaming mouse.'), ('Logitech G502 Hero Mouse', 'High-performance wired gaming mouse.'),
('LG UltraGear 27 Monitor', '27-inch 1440p 144Hz gaming monitor.'), ('Samsung Odyssey G9 Monitor', '49-inch ultrawide curved gaming monitor.'),
('Logitech C920 Webcam', 'Full HD 1080p desktop webcam.'), ('Elgato Facecam', 'Professional streaming webcam.'),
('HyperX Cloud II Headset', 'Wired gaming headset with 7.1 surround sound.'), ('SteelSeries Arctis Nova Pro', 'Wireless high-fidelity gaming headset.'),
('Logitech Z623 Speakers', '2.1 Speaker system with subwoofer.'), ('Creative Pebble Speakers', 'Compact USB-powered desktop speakers.'),
('Blue Yeti Microphone', 'USB condenser microphone for streaming/podcasting.'), ('FIFINE K669B Microphone', 'Budget USB microphone.'),
('Wacom Intuos S Tablet', 'Small digital drawing tablet.'),
('ZenBook 14 Laptop (i7)', 'Slim 14-inch productivity laptop with Core i7.'), ('ZenBook 14 Laptop (i5)', 'Slim 14-inch productivity laptop with Core i5.'),
('ROG Zephyrus G14 Laptop (4070)', 'High-end gaming laptop with RTX 4070.'), ('ROG Zephyrus G14 Laptop (4060)', 'Mid-range gaming laptop with RTX 4060.'),
('TUF Gaming A15 Laptop', 'Durable mid-range gaming laptop.'), ('Acer Aspire 5 Laptop (i5)', 'Budget-friendly 15-inch productivity laptop.'),
('Acer Nitro 5 Laptop', 'Entry-level gaming laptop.'), ('Dell XPS 13 (2024)', 'Premium 13-inch ultraportable laptop.'),
('Dell Inspiron 15', 'Standard 15-inch budget laptop.'), ('HP Spectre x360 14', 'Convertible 2-in-1 premium laptop.'),
('HP Pavilion Aero 13', 'Lightweight 13-inch laptop.'), ('Lenovo Legion 5 Pro', 'Powerful 16-inch gaming laptop.'),
('Lenovo IdeaPad Gaming 3', 'Affordable gaming laptop.'), ('MacBook Air M3 13-inch (8GB)', 'Apple M3 chip ultraportable.'),
('MacBook Pro M3 Pro 14-inch (18GB)', 'Apple M3 Pro chip workstation.'), ('Microsoft Surface Laptop 5 13.5', 'Sleek 13.5-inch Windows laptop.'),
('MSI Stealth 16 Studio', 'Thin and powerful professional/gaming laptop.'), ('Gigabyte Aero 16 OLED', 'High-resolution OLED screen for creative work.'),
('Alienware m18 Gaming Laptop', 'Large 18-inch high-end gaming laptop.'), ('Chromebook Duet 5', 'Budget 2-in-1 Chrome OS tablet/laptop.'),
('USB-C to HDMI Adapter', 'Adapter for connecting modern laptops to HDMI displays.'), ('Arctic MX-4 Thermal Paste', 'High-performance thermal compound for CPU/GPU.'),
('SteelSeries Mousepad XXL', 'Large cloth gaming mousepad.'), ('Logitech G240 Mousepad', 'Standard cloth gaming mousepad.'),
('HDMI 2.1 Cable 2m', 'High-speed cable for 4K/120Hz displays.'), ('DisplayPort 1.4 Cable 3m', 'High-speed cable for PC monitors.'),
('Velcro Cable Ties 50-pack', 'Reusable straps for cable management.'), ('Compressed Air Canister', 'For dusting PC components.'),
('Screen Cleaning Kit', 'Microfiber cloth and cleaning solution.'), ('Windows 11 Home License', 'Operating System license key.'),
('Microsoft Office 365 License', 'Annual subscription for Office suite.'), ('Surge Protector 8-Outlet', 'Power strip for protection.'),
('External Webcam Tripod', 'Small tripod for external webcams.'), ('USB-A to USB-C Hub 4-port', 'Hub for expanding port access.'),
('Laptop Backpack 15-inch', 'Padded backpack for safely carrying laptops.');

INSERT INTO item_category_assignments (_item_id, _item_category_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1), (9, 1), (10, 1), 
(11, 1), (12, 1), (13, 1), (14, 1), (15, 1), (16, 1), (17, 1), (18, 1), (19, 1), (20, 1), 
(21, 2), (22, 2), (23, 2), (24, 2), (25, 2), (26, 2), (27, 2), (28, 2), (29, 2), (30, 2), 
(31, 2), (32, 2), (33, 2), (34, 2), (35, 2),                                                      
(36, 3), (37, 3), (38, 3), (39, 3), (40, 3), (41, 3), (42, 3), (43, 3), (44, 3), (45, 3), 
(46, 3), (47, 3), (48, 3), (49, 3), (50, 3), (51, 3), (52, 3), (53, 3), (54, 3), (55, 3), 
(56, 4), (57, 4), (58, 4), (59, 4), (60, 4), (61, 4), (62, 4), (63, 4), (64, 4), (65, 4), 
(66, 4), (67, 4), (68, 4), (69, 4), (70, 4);                                                     

INSERT INTO item_stocks (_item_stock_id, _item_id, _item_brand_id, unit_price_php, minimum_quantity) VALUES
-- PC COMPONENTS: minimum_quantity = 10
(1, 1, 3, 22000.00, 10), (2, 2, 3, 12000.00, 10), (3, 3, 7, 35000.00, 10), (4, 4, 7, 75000.00, 10), 
(5, 5, 2, 3500.00, 10), (6, 6, 2, 6000.00, 10), (7, 7, 5, 12500.00, 10), (8, 8, 8, 6000.00, 10), 
(9, 9, 2, 4000.00, 10), (10, 10, 2, 2500.00, 10), (11, 11, 4, 9000.00, 10), (12, 12, 6, 6000.00, 10), 
(13, 13, 8, 4500.00, 10), (14, 14, 8, 7000.00, 10), (15, 15, 8, 4500.00, 10), (16, 16, 8, 5000.00, 10), 
(17, 17, 8, 4500.00, 10), (18, 18, 8, 8000.00, 10), (19, 19, 8, 1500.00, 10), (20, 20, 4, 1000.00, 10), 
-- PERIPHERALS: minimum_quantity = 15
(21, 21, 1, 8500.00, 15), (22, 22, 9, 6000.00, 15), (23, 23, 9, 2500.00, 15), (24, 24, 1, 3500.00, 15), 
(25, 25, 5, 18000.00, 15), (26, 26, 5, 60000.00, 15), (27, 27, 1, 3500.00, 15), (28, 28, 4, 6500.00, 15), 
(29, 29, 9, 4000.00, 15), (30, 30, 9, 12000.00, 15), (31, 31, 1, 6000.00, 15), (32, 32, 1, 2500.00, 15), 
(33, 33, 1, 7000.00, 15), (34, 34, 1, 3000.00, 15), (35, 35, 4, 8000.00, 15), 
-- LAPTOPS: minimum_quantity = 10
(36, 36, 4, 55000.00, 10), (37, 37, 4, 45000.00, 10), (38, 38, 4, 85000.00, 10), (39, 39, 4, 65000.00, 10), 
(40, 40, 4, 40000.00, 10), (41, 41, 4, 25000.00, 10), (42, 42, 4, 35000.00, 10), (43, 43, 10, 70000.00, 10), 
(44, 44, 10, 30000.00, 10), (45, 45, 4, 60000.00, 10), (46, 46, 4, 45000.00, 10), (47, 47, 4, 80000.00, 10), 
(48, 48, 4, 38000.00, 10), (49, 49, 4, 50000.00, 10), (50, 50, 4, 95000.00, 10), (51, 51, 4, 65000.00, 10), 
(52, 52, 4, 90000.00, 10), (53, 53, 4, 80000.00, 10), (54, 54, 10, 120000.00, 10), (55, 55, 4, 20000.00, 10), 
-- ACCESSORIES: minimum_quantity = 20
(56, 56, 4, 500.00, 20), (57, 57, 8, 350.00, 20), (58, 58, 9, 750.00, 20), (59, 59, 1, 500.00, 20), 
(60, 60, 4, 1000.00, 20), (61, 61, 4, 1200.00, 20), (62, 62, 4, 300.00, 20), (63, 63, 8, 450.00, 20), 
(64, 64, 4, 600.00, 20), (65, 65, 6, 7000.00, 20), (66, 66, 6, 10000.00, 20), (67, 67, 8, 1500.00, 20), 
(68, 68, 4, 400.00, 20), (69, 69, 4, 800.00, 20), (70, 70, 4, 2000.00, 20);

-- INSERT statements
INSERT INTO item_stock_storage_locations (_item_stock_id, _created_at, _storage_location_id, quantity)
VALUES
(1, '2025-11-08 09:16:52', 1, 113),
(2, '2025-10-17 08:45:01', 3, 119),
(3, '2025-11-01 21:23:44', 2, 147),
(4, '2025-11-04 04:18:31', 3, 23),
(5, '2025-11-07 02:30:07', 1, 9),
(6, '2025-10-23 18:35:39', 3, 146),
(7, '2025-10-21 08:46:43', 1, 83),
(8, '2025-10-31 07:34:22', 3, 139),
(9, '2025-10-31 13:58:59', 1, 17),
(10, '2025-10-26 21:16:21', 2, 90),
(11, '2025-11-05 05:10:42', 1, 129),
(12, '2025-10-20 15:50:03', 2, 92),
(13, '2025-10-31 07:09:45', 1, 49),
(14, '2025-10-23 22:03:13', 3, 116),
(15, '2025-11-06 20:27:36', 3, 22),
(16, '2025-10-29 06:08:39', 2, 93),
(17, '2025-11-03 22:34:32', 3, 32),
(18, '2025-10-30 20:06:30', 2, 84),
(19, '2025-10-14 23:28:13', 3, 36),
(20, '2025-10-26 07:57:52', 3, 9),
(21, '2025-11-12 10:04:25', 1, 33),
(22, '2025-10-26 00:04:34', 1, 120),
(23, '2025-10-18 04:07:38', 2, 141),
(24, '2025-10-21 14:12:45', 3, 123),
(25, '2025-10-16 23:25:54', 1, 11),
(26, '2025-11-02 09:01:34', 2, 147),
(27, '2025-11-02 20:45:36', 1, 31),
(28, '2025-10-20 21:06:39', 2, 112),
(29, '2025-11-12 14:23:05', 3, 23),
(30, '2025-10-26 19:42:30', 1, 80),
(31, '2025-11-08 10:45:36', 2, 106),
(32, '2025-10-20 05:32:28', 3, 82),
(33, '2025-10-31 06:44:29', 2, 18),
(34, '2025-11-07 09:37:52', 2, 20),
(35, '2025-11-09 10:16:36', 3, 18),
(36, '2025-10-30 05:47:45', 1, 17),
(37, '2025-10-17 01:04:33', 3, 145),
(38, '2025-10-24 00:43:02', 1, 37),
(39, '2025-10-17 12:44:34', 3, 31),
(40, '2025-10-22 23:52:48', 3, 67),
(41, '2025-10-21 12:46:27', 3, 99),
(42, '2025-10-26 10:45:48', 1, 82),
(43, '2025-10-25 08:47:47', 1, 14),
(44, '2025-11-05 16:59:17', 1, 130),
(45, '2025-11-11 23:16:02', 1, 62),
(46, '2025-10-29 05:40:58', 1, 24),
(47, '2025-11-11 06:26:35', 3, 98),
(48, '2025-11-12 00:42:12', 3, 103),
(49, '2025-10-29 00:37:04', 1, 65),
(50, '2025-10-27 10:20:07', 1, 80),
(51, '2025-11-01 21:03:21', 2, 54),
(52, '2025-10-22 05:28:06', 3, 83),
(53, '2025-11-11 09:07:41', 2, 106),
(54, '2025-11-12 15:39:04', 2, 86),
(55, '2025-11-10 05:37:53', 2, 31),
(56, '2025-10-17 16:20:39', 3, 72),
(57, '2025-10-15 20:31:39', 2, 9),
(58, '2025-10-22 10:45:16', 3, 138),
(59, '2025-10-28 14:04:41', 2, 19),
(60, '2025-11-12 22:21:03', 2, 49),
(61, '2025-10-29 08:32:07', 2, 67),
(62, '2025-10-17 02:36:13', 3, 102),
(63, '2025-10-25 00:32:39', 3, 68),
(64, '2025-10-18 18:22:58', 1, 141),
(65, '2025-10-23 00:43:41', 3, 102),
(66, '2025-10-26 01:06:32', 3, 83),
(67, '2025-10-25 14:50:39', 3, 95),
(68, '2025-11-07 15:31:58', 2, 45),
(69, '2025-11-05 10:41:46', 3, 95),
(70, '2025-10-16 21:06:17', 1, 12);

-- UPDATE statements for restocks
UPDATE item_stock_storage_locations SET quantity = 122, _restock_date = '2025-11-12 10:05:00' WHERE _item_stock_storage_location_id = 1;
UPDATE item_stock_storage_locations SET quantity = 130, _restock_date = '2025-10-18 09:20:00' WHERE _item_stock_storage_location_id = 2;
UPDATE item_stock_storage_locations SET quantity = 155, _restock_date = '2025-11-04 14:30:00' WHERE _item_stock_storage_location_id = 3;
UPDATE item_stock_storage_locations SET quantity = 50, _restock_date = '2025-11-06 11:15:00' WHERE _item_stock_storage_location_id = 4;
UPDATE item_stock_storage_locations SET quantity = 25, _restock_date = '2025-11-08 08:45:00' WHERE _item_stock_storage_location_id = 5;
UPDATE item_stock_storage_locations SET quantity = 160, _restock_date = '2025-10-25 18:50:00' WHERE _item_stock_storage_location_id = 6;
UPDATE item_stock_storage_locations SET quantity = 95, _restock_date = '2025-10-22 12:10:00' WHERE _item_stock_storage_location_id = 7;
UPDATE item_stock_storage_locations SET quantity = 145, _restock_date = '2025-11-02 09:40:00' WHERE _item_stock_storage_location_id = 8;
UPDATE item_stock_storage_locations SET quantity = 35, _restock_date = '2025-11-01 07:25:00' WHERE _item_stock_storage_location_id = 9;
UPDATE item_stock_storage_locations SET quantity = 100, _restock_date = '2025-10-27 19:05:00' WHERE _item_stock_storage_location_id = 10;
UPDATE item_stock_storage_locations SET quantity = 140, _restock_date = '2025-11-06 14:55:00' WHERE _item_stock_storage_location_id = 11;
UPDATE item_stock_storage_locations SET quantity = 105, _restock_date = '2025-10-21 10:15:00' WHERE _item_stock_storage_location_id = 12;
UPDATE item_stock_storage_locations SET quantity = 60, _restock_date = '2025-11-02 16:30:00' WHERE _item_stock_storage_location_id = 13;
UPDATE item_stock_storage_locations SET quantity = 120, _restock_date = '2025-10-25 13:45:00' WHERE _item_stock_storage_location_id = 14;
UPDATE item_stock_storage_locations SET quantity = 45, _restock_date = '2025-11-08 11:20:00' WHERE _item_stock_storage_location_id = 15;
UPDATE item_stock_storage_locations SET quantity = 85, _restock_date = '2025-10-30 14:10:00' WHERE _item_stock_storage_location_id = 16;
UPDATE item_stock_storage_locations SET quantity = 55, _restock_date = '2025-11-04 18:00:00' WHERE _item_stock_storage_location_id = 17;
UPDATE item_stock_storage_locations SET quantity = 40, _restock_date = '2025-10-31 12:30:00' WHERE _item_stock_storage_location_id = 18;
UPDATE item_stock_storage_locations SET quantity = 90, _restock_date = '2025-10-15 20:20:00' WHERE _item_stock_storage_location_id = 19;
UPDATE item_stock_storage_locations SET quantity = 70, _restock_date = '2025-10-27 09:45:00' WHERE _item_stock_storage_location_id = 20;
UPDATE item_stock_storage_locations SET quantity = 65, _restock_date = '2025-11-12 14:15:00' WHERE _item_stock_storage_location_id = 21;
UPDATE item_stock_storage_locations SET quantity = 125, _restock_date = '2025-10-27 11:35:00' WHERE _item_stock_storage_location_id = 22;
UPDATE item_stock_storage_locations SET quantity = 140, _restock_date = '2025-10-19 08:50:00' WHERE _item_stock_storage_location_id = 23;
UPDATE item_stock_storage_locations SET quantity = 110, _restock_date = '2025-10-22 16:40:00' WHERE _item_stock_storage_location_id = 24;
UPDATE item_stock_storage_locations SET quantity = 20, _restock_date = '2025-10-17 23:10:00' WHERE _item_stock_storage_location_id = 25;
UPDATE item_stock_storage_locations SET quantity = 150, _restock_date = '2025-11-03 12:20:00' WHERE _item_stock_storage_location_id = 26;
UPDATE item_stock_storage_locations SET quantity = 35, _restock_date = '2025-11-03 20:55:00' WHERE _item_stock_storage_location_id = 27;
UPDATE item_stock_storage_locations SET quantity = 95, _restock_date = '2025-10-21 10:10:00' WHERE _item_stock_storage_location_id = 28;
UPDATE item_stock_storage_locations SET quantity = 60, _restock_date = '2025-11-12 16:45:00' WHERE _item_stock_storage_location_id = 29;
UPDATE item_stock_storage_locations SET quantity = 90, _restock_date = '2025-10-27 21:10:00' WHERE _item_stock_storage_location_id = 30;
UPDATE item_stock_storage_locations SET quantity = 110, _restock_date = '2025-11-09 09:05:00' WHERE _item_stock_storage_location_id = 31;
UPDATE item_stock_storage_locations SET quantity = 85, _restock_date = '2025-10-21 15:30:00' WHERE _item_stock_storage_location_id = 32;
UPDATE item_stock_storage_locations SET quantity = 25, _restock_date = '2025-11-01 08:40:00' WHERE _item_stock_storage_location_id = 33;
UPDATE item_stock_storage_locations SET quantity = 30, _restock_date = '2025-11-08 17:20:00' WHERE _item_stock_storage_location_id = 34;
UPDATE item_stock_storage_locations SET quantity = 20, _restock_date = '2025-11-10 13:50:00' WHERE _item_stock_storage_location_id = 35;
UPDATE item_stock_storage_locations SET quantity = 19, _restock_date = '2025-10-30 14:00:00' WHERE _item_stock_storage_location_id = 36;
UPDATE item_stock_storage_locations SET quantity = 155, _restock_date = '2025-10-17 15:05:00' WHERE _item_stock_storage_location_id = 37;
UPDATE item_stock_storage_locations SET quantity = 45, _restock_date = '2025-10-25 09:20:00' WHERE _item_stock_storage_location_id = 38;
UPDATE item_stock_storage_locations SET quantity = 35, _restock_date = '2025-10-17 20:15:00' WHERE _item_stock_storage_location_id = 39;
UPDATE item_stock_storage_locations SET quantity = 70, _restock_date = '2025-10-23 19:10:00' WHERE _item_stock_storage_location_id = 40;
UPDATE item_stock_storage_locations SET quantity = 100, _restock_date = '2025-10-21 18:05:00' WHERE _item_stock_storage_location_id = 41;
UPDATE item_stock_storage_locations SET quantity = 90, _restock_date = '2025-10-27 12:45:00' WHERE _item_stock_storage_location_id = 42;
UPDATE item_stock_storage_locations SET quantity = 20, _restock_date = '2025-10-25 11:55:00' WHERE _item_stock_storage_location_id = 43;
UPDATE item_stock_storage_locations SET quantity = 140, _restock_date = '2025-11-06 10:40:00' WHERE _item_stock_storage_location_id = 44;
UPDATE item_stock_storage_locations SET quantity = 70, _restock_date = '2025-11-12 08:20:00' WHERE _item_stock_storage_location_id = 45;
UPDATE item_stock_storage_locations SET quantity = 40, _restock_date = '2025-10-30 17:30:00' WHERE _item_stock_storage_location_id = 46;
UPDATE item_stock_storage_locations SET quantity = 105, _restock_date = '2025-11-11 12:10:00' WHERE _item_stock_storage_location_id = 47;
UPDATE item_stock_storage_locations SET quantity = 110, _restock_date = '2025-11-12 09:25:00' WHERE _item_stock_storage_location_id = 48;
UPDATE item_stock_storage_locations SET quantity = 75, _restock_date = '2025-10-30 08:50:00' WHERE _item_stock_storage_location_id = 49;
UPDATE item_stock_storage_locations SET quantity = 90, _restock_date = '2025-10-28 15:15:00' WHERE _item_stock_storage_location_id = 50;
UPDATE item_stock_storage_locations SET quantity = 65, _restock_date = '2025-11-01 17:35:00' WHERE _item_stock_storage_location_id = 51;
UPDATE item_stock_storage_locations SET quantity = 90, _restock_date = '2025-10-22 09:45:00' WHERE _item_stock_storage_location_id = 52;
UPDATE item_stock_storage_locations SET quantity = 115, _restock_date = '2025-11-11 15:55:00' WHERE _item_stock_storage_location_id = 53;
UPDATE item_stock_storage_locations SET quantity = 100, _restock_date = '2025-11-12 18:20:00' WHERE _item_stock_storage_location_id = 54;
UPDATE item_stock_storage_locations SET quantity = 50, _restock_date = '2025-11-10 14:30:00' WHERE _item_stock_storage_location_id = 55;
UPDATE item_stock_storage_locations SET quantity = 105, _restock_date = '2025-10-18 20:50:00' WHERE _item_stock_storage_location_id = 56;
UPDATE item_stock_storage_locations SET quantity = 30, _restock_date = '2025-10-16 18:20:00' WHERE _item_stock_storage_location_id = 57;
UPDATE item_stock_storage_locations SET quantity = 140, _restock_date = '2025-10-23 14:10:00' WHERE _item_stock_storage_location_id = 58;
UPDATE item_stock_storage_locations SET quantity = 35, _restock_date = '2025-10-29 16:35:00' WHERE _item_stock_storage_location_id = 59;
UPDATE item_stock_storage_locations SET quantity = 85, _restock_date = '2025-11-12 20:10:00' WHERE _item_stock_storage_location_id = 60;
UPDATE item_stock_storage_locations SET quantity = 100, _restock_date = '2025-10-29 12:20:00' WHERE _item_stock_storage_location_id = 61;
UPDATE item_stock_storage_locations SET quantity = 150, _restock_date = '2025-10-17 08:55:00' WHERE _item_stock_storage_location_id = 62;
UPDATE item_stock_storage_locations SET quantity = 90, _restock_date = '2025-10-25 13:05:00' WHERE _item_stock_storage_location_id = 63;
UPDATE item_stock_storage_locations SET quantity = 160, _restock_date = '2025-10-18 22:20:00' WHERE _item_stock_storage_location_id = 64;
UPDATE item_stock_storage_locations SET quantity = 105, _restock_date = '2025-10-23 07:50:00' WHERE _item_stock_storage_location_id = 65;
UPDATE item_stock_storage_locations SET quantity = 95, _restock_date = '2025-10-26 16:25:00' WHERE _item_stock_storage_location_id = 66;
UPDATE item_stock_storage_locations SET quantity = 110, _restock_date = '2025-10-25 18:15:00' WHERE _item_stock_storage_location_id = 67;
UPDATE item_stock_storage_locations SET quantity = 60, _restock_date = '2025-11-08 12:30:00' WHERE _item_stock_storage_location_id = 68;
UPDATE item_stock_storage_locations SET quantity = 120, _restock_date = '2025-11-05 18:50:00' WHERE _item_stock_storage_location_id = 69;
UPDATE item_stock_storage_locations SET quantity = 30, _restock_date = '2025-10-17 13:25:00' WHERE _item_stock_storage_location_id = 70;

INSERT INTO purchases (_supplier_id, purchase_date, purchase_code, delivery_date, vat_percent, discount_value, discount_type) VALUES
(1, DATETIME('now', '-3 days'), 'PO-CDI-2025-001', DATETIME('now', '-2 days'), 0.12, 500.00, 'fixed'),
(2, DATETIME('now', '-4 days'), 'PO-PW-2025-002', DATETIME('now', '-3 days'), 0.12, 0.05, 'percentage'),
(3, DATETIME('now', '-2 days'), 'PO-APP-2025-003', NULL, 0.12, NULL, NULL),
(4, DATETIME('now', '-1 day'), 'PO-SS-2025-004', DATETIME('now'), 0.12, 0, 'fixed');

INSERT INTO purchase_payments (_purchase_id, payment_date, reference_number, payment_method, amount_php) VALUES
(1, DATETIME('now', '-2 days', '+1 hour'), 'REF-PP-98765', 'bank_transfer', 77840.00),
(2, DATETIME('now', '-3 days', '+2 hours'), 'REF-PP-65432', 'cash', 106400.00),
(3, DATETIME('now', '-2 days', '+3 hours'), 'REF-PP-32109', 'gcash', 44800.00),
(4, DATETIME('now'), 'REF-PP-09876', 'bank_transfer', 140000.00);

INSERT INTO purchase_item_stocks (_purchase_id, _item_stock_id, quantity_ordered, quantity_received, unit_cost_php) VALUES
(1, 2, 5, 5, 10000.00),
(1, 5, 20, 20, 1000.00),
(2, 21, 10, 10, 8000.00),
(2, 23, 10, 10, 2000.00),
(3, 37, 1, 1, 40000.00),
(4, 7, 10, 10, 12500.00);

-- SALES and TRANSACTIONS
INSERT INTO sales (sale_date, sale_code, customer_name, vat_percent, discount_value, discount_type) VALUES
(DATETIME('now', '-14 days', '10:00:00'), 'SALE-2025-0001', 'Client A', 0.12, 0, 'fixed'),
(DATETIME('now', '-14 days', '14:30:00'), 'SALE-2025-0002', 'Client B', 0.12, 0.10, 'percentage'),
(DATETIME('now', '-13 days', '09:15:00'), 'SALE-2025-0003', 'Walk-in', 0.12, 100.00, 'fixed'),
(DATETIME('now', '-13 days', '16:00:00'), 'SALE-2025-0004', 'Client D', 0.12, NULL, NULL),
(DATETIME('now', '-12 days', '11:20:00'), 'SALE-2025-0005', 'Jane Smith', 0.12, 0.05, 'percentage'),
(DATETIME('now', '-11 days', '15:45:00'), 'SALE-2025-0006', 'Technology Hub', 0.12, 500.00, 'fixed'),
(DATETIME('now', '-10 days', '13:00:00'), 'SALE-2025-0007', 'Michael Lee', 0.12, 0, 'fixed'),
(DATETIME('now', '-9 days', '10:30:00'), 'SALE-2025-0008', 'Gamer Corp', 0.12, 0.15, 'percentage'),
(DATETIME('now', '-8 days', '17:00:00'), 'SALE-2025-0009', 'Local University', 0.12, 2000.00, 'fixed'),
(DATETIME('now', '-7 days', '12:00:00'), 'SALE-2025-0010', 'Walk-in', 0.12, NULL, NULL),
(DATETIME('now', '-6 days', '14:10:00'), 'SALE-2025-0011', 'Elias Vance', 0.12, 0.03, 'percentage'),
(DATETIME('now', '-5 days', '09:40:00'), 'SALE-2025-0012', 'Quick Fix IT', 0.12, 1000.00, 'fixed'),
(DATETIME('now', '-4 days', '18:00:00'), 'SALE-2025-0013', 'Sarah Connor', 0.12, 0, 'fixed'),
(DATETIME('now', '-3 days', '11:55:00'), 'SALE-2025-0014', 'Tech Enthusiast', 0.12, 0.05, 'percentage'),
(DATETIME('now', '-2 days', '16:30:00'), 'SALE-2025-0015', 'Mark Johnson', 0.12, 0, 'fixed'),
(DATETIME('now', '-1 day', '10:45:00'), 'SALE-2025-0016', 'Corporate Client X', 0.12, 0.05, 'percentage'),
(DATETIME('now', '-1 day', '15:10:00'), 'SALE-2025-0017', 'Walk-in', 0.12, 0, 'fixed'),
(DATETIME('now', '02:30:00'), 'SALE-2025-0018', 'Tech Solutions Inc.', 0.12, 1500.00, 'fixed'),
(DATETIME('now', '03:45:00'), 'SALE-2025-0019', 'Client Y', 0.12, 0.10, 'percentage'),
(DATETIME('now', '07:20:00'), 'SALE-2025-0020', 'Walk-in', 0.12, NULL, NULL);

INSERT INTO sale_item_stocks (_sale_id, _item_stock_id, quantity, unit_price_php) VALUES
(1, 5, 2, 3500.00), (1, 21, 1, 8500.00),
(2, 2, 1, 12000.00), (2, 57, 1, 350.00),
(3, 23, 1, 2500.00), (3, 67, 2, 1500.00),
(4, 25, 1, 18000.00), (4, 56, 2, 500.00),
(5, 37, 1, 45000.00), (5, 69, 1, 800.00),
(6, 38, 1, 85000.00), (6, 7, 1, 12500.00),
(7, 7, 1, 12500.00), (7, 29, 1, 4000.00),
(8, 43, 1, 70000.00), (8, 24, 1, 3500.00),
(9, 54, 1, 120000.00), (9, 65, 1, 7000.00),
(10, 11, 2, 9000.00), (10, 13, 1, 4500.00),
(11, 27, 1, 3500.00), (11, 32, 1, 2500.00),
(12, 56, 5, 500.00), (12, 63, 5, 450.00),
(13, 51, 1, 65000.00), (13, 70, 1, 2000.00),
(14, 33, 1, 7000.00), (14, 59, 1, 500.00),
(15, 3, 1, 35000.00), (15, 66, 1, 10000.00),
(16, 24, 2, 3200.00), (16, 28, 1, 7500.00),
(17, 13, 1, 4500.00), (17, 56, 2, 600.00),
(18, 38, 1, 90000.00), (18, 7, 1, 13000.00),
(19, 54, 1, 125000.00), (19, 61, 1, 4500.00),
(20, 25, 1, 20000.00), (20, 65, 2, 8000.00);


INSERT INTO sale_payments (_sale_id, payment_date, reference_number, payment_method, amount_php) VALUES
(1, DATETIME('now', '-14 days', '10:05:00'), NULL, 'cash', 17360.00), 
(2, DATETIME('now', '-14 days', '14:35:00'), 'GCASH-0914', 'gcash', 12448.80), 
(3, DATETIME('now', '-13 days', '09:20:00'), NULL, 'cash', 6048.00), 
(4, DATETIME('now', '-13 days', '16:05:00'), 'BTRANS-0913', 'bank_transfer', 21280.00),
(5, DATETIME('now', '-12 days', '11:25:00'), 'GCASH-0912', 'gcash', 48731.20),
(6, DATETIME('now', '-11 days', '15:50:00'), 'BTRANS-0911', 'bank_transfer', 108640.00),
(7, DATETIME('now', '-10 days', '13:05:00'), NULL, 'cash', 18480.00),
(8, DATETIME('now', '-9 days', '10:35:00'), 'BTRANS-0909', 'bank_transfer', 69972.00),
(9, DATETIME('now', '-8 days', '17:05:00'), 'BTRANS-0908', 'bank_transfer', 140000.00),
(10, DATETIME('now', '-7 days', '12:05:00'), NULL, 'cash', 25200.00),
(11, DATETIME('now', '-6 days', '14:15:00'), 'GCASH-0906', 'gcash', 6518.40),
(12, DATETIME('now', '-5 days', '09:45:00'), NULL, 'cash', 4480.00),
(13, DATETIME('now', '-4 days', '18:05:00'), 'BTRANS-0904', 'bank_transfer', 75040.00),
(14, DATETIME('now', '-3 days', '12:00:00'), 'GCASH-0903', 'gcash', 7980.00),
(15, DATETIME('now', '-2 days', '16:35:00'), NULL, 'cash', 50400.00),
(16, DATETIME('now', '-1 day', '10:50:00'), 'BTRANS-0902', 'bank_transfer', 11232.00),
(17, DATETIME('now', '-1 day', '15:15:00'), NULL, 'cash', 6160.00),
(18, DATETIME('now', '02:35:00'), 'BTRANS-0901', 'bank_transfer', 14240.00),
(19, DATETIME('now', '03:50:00'), 'GCASH-0901', 'gcash', 25136.00),
(20, DATETIME('now', '07:25:00'), NULL, 'cash', 11360.00);

UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Ryzen 9 7950X3D CPU.png' WHERE name = 'Ryzen 9 7950X3D CPU';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Ryzen 5 7600X CPU.png' WHERE name = 'Ryzen 5 7600X CPU';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/GeForce RTX 4070 GPU.png' WHERE name = 'GeForce RTX 4070 GPU';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/GeForce RTX 4090 GPU.png' WHERE name = 'GeForce RTX 4090 GPU';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/HyperX Fury 16GB RAM.png' WHERE name = 'HyperX Fury 16GB RAM';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Corsair Vengeance 32GB RAM.png' WHERE name = 'Corsair Vengeance 32GB RAM';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Samsung 990 Pro 2TB SSD.png' WHERE name = 'Samsung 990 Pro 2TB SSD';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Crucial P5 Plus 1TB SSD.png' WHERE name = 'Crucial P5 Plus 1TB SSD';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/WD Blue 4TB HDD.png' WHERE name = 'WD Blue 4TB HDD';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Seagate Barracuda 1TB HDD.png' WHERE name = 'Seagate Barracuda 1TB HDD';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/ROG STRIX B650 Motherboard.png' WHERE name = 'ROG STRIX B650 Motherboard';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/MSI MAG B760 Motherboard.png' WHERE name = 'MSI MAG B760 Motherboard';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Corsair 750W Power Supply.png' WHERE name = 'Corsair 750W Power Supply';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/EVGA 1000W Power Supply.png' WHERE name = 'EVGA 1000W Power Supply';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Lian Li Lancool 205 Case.png' WHERE name = 'Lian Li Lancool 205 Case';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/NZXT H7 Flow Case.png' WHERE name = 'NZXT H7 Flow Case';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Noctua NH-U12S CPU Cooler.png' WHERE name = 'Noctua NH-U12S CPU Cooler';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Corsair iCUE H150i AIO Cooler.png' WHERE name = 'Corsair iCUE H150i AIO Cooler';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Arctic P12 PWM Fan 5-Pack.png' WHERE name = 'Arctic P12 PWM Fan 5-Pack';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/PCIe Wi-Fi 6 Adapter.png' WHERE name = 'PCIe Wi-Fi 6 Adapter';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/G Pro X Mechanical Keyboard.png' WHERE name = 'G Pro X Mechanical Keyboard';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Razer BlackWidow V3 Keyboard.png' WHERE name = 'Razer BlackWidow V3 Keyboard';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Razer Viper Mini Mouse.png' WHERE name = 'Razer Viper Mini Mouse';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Logitech G502 Hero Mouse.png' WHERE name = 'Logitech G502 Hero Mouse';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/LG UltraGear 27 Monitor.png' WHERE name = 'LG UltraGear 27 Monitor';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Samsung Odyssey G9 Monitor.png' WHERE name = 'Samsung Odyssey G9 Monitor';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Logitech C920 Webcam.png' WHERE name = 'Logitech C920 Webcam';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Elgato Facecam.png' WHERE name = 'Elgato Facecam';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/HyperX Cloud II Headset.png' WHERE name = 'HyperX Cloud II Headset';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/SteelSeries Arctis Nova Pro.png' WHERE name = 'SteelSeries Arctis Nova Pro';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Logitech Z623 Speakers.png' WHERE name = 'Logitech Z623 Speakers';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Creative Pebble Speakers.png' WHERE name = 'Creative Pebble Speakers';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Blue Yeti Microphone.png' WHERE name = 'Blue Yeti Microphone';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/FIFINE K669B Microphone.png' WHERE name = 'FIFINE K669B Microphone';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Wacom Intuos S Tablet.png' WHERE name = 'Wacom Intuos S Tablet';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/ZenBook 14 Laptop (i7).png' WHERE name = 'ZenBook 14 Laptop (i7)';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/ZenBook 14 Laptop (i5).png' WHERE name = 'ZenBook 14 Laptop (i5)';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/ROG Zephyrus G14 Laptop (4070).png' WHERE name = 'ROG Zephyrus G14 Laptop (4070)';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/ROG Zephyrus G14 Laptop (4060).png' WHERE name = 'ROG Zephyrus G14 Laptop (4060)';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/TUF Gaming A15 Laptop.png' WHERE name = 'TUF Gaming A15 Laptop';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Acer Aspire 5 Laptop (i5).png' WHERE name = 'Acer Aspire 5 Laptop (i5)';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Acer Nitro 5 Laptop.png' WHERE name = 'Acer Nitro 5 Laptop';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Dell XPS 13 (2024).png' WHERE name = 'Dell XPS 13 (2024)';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Dell Inspiron 15.png' WHERE name = 'Dell Inspiron 15';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/HP Spectre x360 14.png' WHERE name = 'HP Spectre x360 14';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/HP Pavilion Aero 13.png' WHERE name = 'HP Pavilion Aero 13';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Lenovo Legion 5 Pro.png' WHERE name = 'Lenovo Legion 5 Pro';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Lenovo IdeaPad Gaming 3.png' WHERE name = 'Lenovo IdeaPad Gaming 3';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/MacBook Air M3 13-inch (8GB).png' WHERE name = 'MacBook Air M3 13-inch (8GB)';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/MacBook Pro M3 Pro 14-inch (18GB).png' WHERE name = 'MacBook Pro M3 Pro 14-inch (18GB)';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Microsoft Surface Laptop 5 13.5.png' WHERE name = 'Microsoft Surface Laptop 5 13.5';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/MSI Stealth 16 Studio.png' WHERE name = 'MSI Stealth 16 Studio';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Gigabyte Aero 16 OLED.png' WHERE name = 'Gigabyte Aero 16 OLED';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Alienware m18 Gaming Laptop.png' WHERE name = 'Alienware m18 Gaming Laptop';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Chromebook Duet 5.png' WHERE name = 'Chromebook Duet 5';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/USB-C to HDMI Adapter.png' WHERE name = 'USB-C to HDMI Adapter';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Arctic MX-4 Thermal Paste.png' WHERE name = 'Arctic MX-4 Thermal Paste';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/SteelSeries Mousepad XXL.png' WHERE name = 'SteelSeries Mousepad XXL';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Logitech G240 Mousepad.png' WHERE name = 'Logitech G240 Mousepad';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/HDMI 2.1 Cable 2m.png' WHERE name = 'HDMI 2.1 Cable 2m';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/DisplayPort 1.4 Cable 3m.png' WHERE name = 'DisplayPort 1.4 Cable 3m';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Velcro Cable Ties 50-pack.png' WHERE name = 'Velcro Cable Ties 50-pack';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Compressed Air Canister.png' WHERE name = 'Compressed Air Canister';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Screen Cleaning Kit.png' WHERE name = 'Screen Cleaning Kit';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Windows 11 Home License.png' WHERE name = 'Windows 11 Home License';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Microsoft Office 365 License.png' WHERE name = 'Microsoft Office 365 License';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Surge Protector 8-Outlet.png' WHERE name = 'Surge Protector 8-Outlet';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/External Webcam Tripod.png' WHERE name = 'External Webcam Tripod';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/USB-A to USB-C Hub 4-port.png' WHERE name = 'USB-A to USB-C Hub 4-port';
UPDATE items SET display_image = '/com/github/ragudos/kompeter/app/desktop/assets/images/Laptop Backpack 15-inch.png' WHERE name = 'Laptop Backpack 15-inch';
