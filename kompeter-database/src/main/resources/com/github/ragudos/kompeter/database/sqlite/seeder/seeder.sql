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

INSERT INTO item_stock_storage_locations (_item_stock_id, _storage_location_id, quantity) VALUES
(1, 2, 8), (1, 1, 2), (2, 2, 40), (2, 1, 10), (3, 2, 12), (3, 1, 3), (4, 2, 4), (4, 1, 1), 
(5, 2, 80), (5, 1, 20), (6, 2, 35), (6, 1, 5), (7, 2, 32), (7, 1, 8), (8, 2, 25), (8, 1, 5), 
(9, 2, 40), (9, 1, 10), (10, 2, 50), (10, 1, 10), (11, 2, 35), (11, 1, 10), (12, 2, 25), (12, 1, 5), 
(13, 2, 40), (13, 1, 10), (14, 2, 20), (14, 1, 5), (15, 2, 30), (15, 1, 10), (16, 2, 28), (16, 1, 7), 
(17, 2, 25), (17, 1, 5), (18, 2, 15), (18, 1, 5), (19, 2, 80), (19, 1, 20), (20, 2, 60), (20, 1, 20), 
(21, 3, 60), (21, 1, 15), (22, 3, 40), (22, 1, 10), (23, 3, 50), (23, 1, 10), (24, 3, 30), (24, 1, 10), 
(25, 3, 25), (25, 1, 5), (26, 3, 8), (26, 1, 2), (27, 3, 45), (27, 1, 10), (28, 3, 25), (28, 1, 5), 
(29, 3, 50), (29, 1, 15), (30, 3, 15), (30, 1, 5), (31, 3, 30), (31, 1, 10), (32, 3, 40), (32, 1, 10), 
(33, 3, 28), (33, 1, 7), (34, 3, 35), (34, 1, 10), (35, 3, 20), (35, 1, 5), 
(36, 3, 15), (37, 3, 20), (38, 3, 10), (39, 3, 12), (40, 3, 25), (41, 3, 35), (42, 3, 25), (43, 3, 10), 
(44, 3, 30), (45, 3, 10), (46, 3, 15), (47, 3, 8), (48, 3, 20), (49, 3, 15), (50, 3, 5), (51, 3, 10), 
(52, 3, 5), (53, 3, 6), (54, 3, 4), (55, 3, 30), 
(56, 1, 100), (56, 2, 50), (57, 1, 150), (57, 2, 50), (58, 1, 50), (58, 2, 30), (59, 1, 70), (59, 2, 20), 
(60, 1, 80), (60, 2, 40), (61, 1, 70), (61, 2, 40), (62, 1, 200), (62, 2, 100), (63, 1, 100), (63, 2, 50), 
(64, 1, 75), (64, 2, 25), (65, 1, 20), (65, 2, 30), (66, 1, 15), (66, 2, 25), (67, 1, 40), (67, 2, 20), 
(68, 1, 50), (68, 2, 20), (69, 1, 60), (69, 2, 30), (70, 1, 25), (70, 3, 25);

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
