
CREATE DATABASE car_store_management;

CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,  
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role ENUM('admin', 'staff') NOT NULL DEFAULT 'staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    status ENUM('active', 'inactive') DEFAULT 'active'
);

-- Car Manufacturers
CREATE TABLE manufacturers (
    manufacturer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    country VARCHAR(50),
    website VARCHAR(255),
    contact_info VARCHAR(255)
);

-- Car Models
CREATE TABLE car_models (
    model_id INT AUTO_INCREMENT PRIMARY KEY,
    manufacturer_id INT NOT NULL,
    model_name VARCHAR(100) NOT NULL,
    year_introduced YEAR,
    category ENUM('sedan', 'suv', 'truck', 'sports', 'hatchback', 'van', 'luxury', 'electric') NOT NULL,
    FOREIGN KEY (manufacturer_id) REFERENCES manufacturers(manufacturer_id),
    UNIQUE KEY (manufacturer_id, model_name, year_introduced)
);

-- Car Specifications
CREATE TABLE specifications (
    spec_id INT AUTO_INCREMENT PRIMARY KEY,
    model_id INT NOT NULL,
    engine_type VARCHAR(50),
    transmission ENUM('automatic', 'manual', 'semi-automatic'),
    fuel_type ENUM('gasoline', 'diesel', 'electric', 'hybrid', 'plugin_hybrid'),
    horsepower INT,
    seats INT,
    fuel_economy DECIMAL(5,2),
    features TEXT,
    FOREIGN KEY (model_id) REFERENCES car_models(model_id)
);

-- Cars Inventory
CREATE TABLE cars (
    car_id INT AUTO_INCREMENT PRIMARY KEY,
    model_id INT NOT NULL,
    vin VARCHAR(17) NOT NULL UNIQUE,
    color VARCHAR(50) NOT NULL,
    manufacture_year YEAR NOT NULL,
    mileage INT DEFAULT 0,
    price DECIMAL(12,2) NOT NULL,
    cost DECIMAL(12,2) NOT NULL,
    status ENUM('available', 'sold', 'reserved', 'maintenance') DEFAULT 'available',
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    location VARCHAR(100),
    image_url VARCHAR(255),
    added_by INT,
    notes TEXT,
    FOREIGN KEY (model_id) REFERENCES car_models(model_id),
    FOREIGN KEY (added_by) REFERENCES staff(staff_id)
);

-- Coming Soon Cars
CREATE TABLE coming_soon_cars (
    coming_soon_id INT AUTO_INCREMENT PRIMARY KEY,
    model_id INT NOT NULL,
    color VARCHAR(50),
    manufacture_year YEAR NOT NULL,
    expected_price DECIMAL(12,2),
    expected_arrival_date DATE NOT NULL,
    status ENUM('ordered', 'in_production', 'in_transit') DEFAULT 'ordered',
    ordered_by INT,
    notes TEXT,
    FOREIGN KEY (model_id) REFERENCES car_models(model_id),
    FOREIGN KEY (ordered_by) REFERENCES staff(staff_id)
);

-- Sales
CREATE TABLE sales (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id INT NOT NULL,
    buyer_name VARCHAR(100) NOT NULL,
    buyer_contact VARCHAR(100) NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sale_price DECIMAL(12,2) NOT NULL,
    tax_amount DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    payment_method ENUM('cash', 'credit_card', 'bank_transfer', 'financing') NOT NULL,
    payment_status ENUM('pending', 'completed') NOT NULL DEFAULT 'completed',
    handled_by INT NOT NULL,
    sale_notes TEXT,
    FOREIGN KEY (car_id) REFERENCES cars(car_id),
    FOREIGN KEY (handled_by) REFERENCES staff(staff_id)
);

-- Expenses (for economic management)
CREATE TABLE expenses (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    expense_date DATE NOT NULL,
    expense_type ENUM('purchase', 'repair', 'marketing', 'utilities', 'rent', 'salary', 'insurance', 'tax', 'other') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    car_id INT NULL,  -- If expense is related to a specific car
    description TEXT NOT NULL,
    recorded_by INT NOT NULL,
    FOREIGN KEY (car_id) REFERENCES cars(car_id),
    FOREIGN KEY (recorded_by) REFERENCES staff(staff_id)
);

-- Economic Reports
CREATE TABLE economic_reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    report_date DATE NOT NULL,
    report_type ENUM('daily', 'weekly', 'monthly', 'quarterly', 'annual') NOT NULL,
    start_period DATE NOT NULL,
    end_period DATE NOT NULL,
    total_sales DECIMAL(12,2) NOT NULL,
    total_expenses DECIMAL(12,2) NOT NULL,
    profit DECIMAL(12,2) NOT NULL,
    cars_sold INT NOT NULL,
    average_sale_price DECIMAL(12,2) NOT NULL,
    generated_by INT NOT NULL,
    FOREIGN KEY (generated_by) REFERENCES staff(staff_id)
);

-- Audit Logs
CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_id INT,
    action_type ENUM('login', 'logout', 'create', 'update', 'delete', 'view', 'export') NOT NULL,
    table_affected VARCHAR(50),
    record_id INT,
    action_details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);




-- Create indexes for performance optimization
CREATE INDEX idx_cars_status ON cars(status);
CREATE INDEX idx_cars_date_added ON cars(date_added);
CREATE INDEX idx_sales_date ON sales(sale_date);
CREATE INDEX idx_coming_soon_arrival ON coming_soon_cars(expected_arrival_date);
CREATE INDEX idx_coming_soon_status ON coming_soon_cars(status);
CREATE INDEX idx_expenses_date ON expenses(expense_date);
CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp);