-- DawaSure Backend - Dummy Data Script
-- This script creates sample data for all tables in the DawaSure database

-- Enable UUID extension if needed
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Start transaction
BEGIN;

-- Insert Users (Admin, Customers, Pharmacy, Delivery)
INSERT INTO users (phone_number, email, full_name, date_of_birth, gender, role, account_status, email_verified, phone_verified, password_hash, failed_login_attempts, marketing_consent, terms_accepted_at, terms_version, created_at, updated_at) VALUES
-- Admin User (password: Garvit223 - hashed with BCrypt)
('9876543210', 'admin@dawasure.com', 'Admin User', '1990-05-15', 'MALE', 'ADMIN', 'ACTIVE', true, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, false, NOW(), '1.0', NOW(), NOW()),

-- Customers
('9876543211', 'john.doe@gmail.com', 'John Doe', '1985-03-20', 'MALE', 'CUSTOMER', 'ACTIVE', true, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, true, NOW(), '1.0', NOW(), NOW()),
('9876543212', 'jane.smith@gmail.com', 'Jane Smith', '1992-07-10', 'FEMALE', 'CUSTOMER', 'ACTIVE', true, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, true, NOW(), '1.0', NOW(), NOW()),
('9876543213', 'robert.wilson@gmail.com', 'Robert Wilson', '1978-12-05', 'MALE', 'CUSTOMER', 'ACTIVE', false, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, false, NOW(), '1.0', NOW(), NOW()),
('9876543214', 'sarah.johnson@gmail.com', 'Sarah Johnson', '1988-09-25', 'FEMALE', 'CUSTOMER', 'ACTIVE', true, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, true, NOW(), '1.0', NOW(), NOW()),

-- Pharmacy Users
('9876543215', 'apollo@dawasure.com', 'Apollo Pharmacy', '1980-01-01', 'OTHER', 'PHARMACY', 'ACTIVE', true, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, false, NOW(), '1.0', NOW(), NOW()),
('9876543216', 'medplus@dawasure.com', 'MedPlus Pharmacy', '1982-01-01', 'OTHER', 'PHARMACY', 'ACTIVE', true, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, false, NOW(), '1.0', NOW(), NOW()),

-- Delivery Partners
('9876543217', 'delivery1@dawasure.com', 'Raj Kumar', '1990-04-18', 'MALE', 'DELIVERY', 'ACTIVE', true, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, false, NOW(), '1.0', NOW(), NOW()),
('9876543218', 'delivery2@dawasure.com', 'Priya Sharma', '1987-11-30', 'FEMALE', 'DELIVERY', 'ACTIVE', true, true, '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 0, false, NOW(), '1.0', NOW(), NOW());

-- Insert User Addresses
INSERT INTO user_addresses (user_id, address_type, full_name, phone_number, address_line_1, address_line_2, landmark, city, state, postal_code, country, latitude, longitude, is_default, is_active, delivery_instructions, created_at, updated_at) VALUES
-- Addresses for John Doe (user_id = 2)
(2, 'HOME', 'John Doe', '9876543211', '123 MG Road', 'Apartment 4B', 'Near City Mall', 'Bangalore', 'Karnataka', '560001', 'India', 12.9716, 77.5946, true, true, 'Ring the bell twice', NOW(), NOW()),
(2, 'WORK', 'John Doe', '9876543211', '45 Tech Park', 'Building A, Floor 3', 'Opposite Metro Station', 'Bangalore', 'Karnataka', '560037', 'India', 12.9352, 77.6245, false, true, 'Call before delivery', NOW(), NOW()),

-- Addresses for Jane Smith (user_id = 3)
(3, 'HOME', 'Jane Smith', '9876543212', '78 Residency Road', 'Flat 2A', 'Near Government Hospital', 'Mumbai', 'Maharashtra', '400001', 'India', 19.0760, 72.8777, true, true, 'Security gate entry required', NOW(), NOW()),
(3, 'HOME', 'Jane Smith', '9876543212', '102 Marine Drive', '', 'Opposite Chowpatty Beach', 'Mumbai', 'Maharashtra', '400020', 'India', 18.9441, 72.8240, false, true, 'Delivery between 6-8 PM only', NOW(), NOW()),

-- Addresses for Robert Wilson (user_id = 4)
(4, 'HOME', 'Robert Wilson', '9876543213', '56 Park Street', 'House No. 12', 'Near Central Park', 'Delhi', 'Delhi', '110001', 'India', 28.6139, 77.2090, true, true, 'Please call on arrival', NOW(), NOW()),

-- Addresses for Sarah Johnson (user_id = 5)
(5, 'HOME', 'Sarah Johnson', '9876543214', '89 Anna Salai', 'Block C, Unit 301', 'Near Express Avenue Mall', 'Chennai', 'Tamil Nadu', '600002', 'India', 13.0827, 80.2707, true, true, 'Lift available', NOW(), NOW());

-- Insert Medicines
INSERT INTO medicines (generic_name, brand_name, manufacturer, composition, medicine_form, strength, pack_size, pack_type, mrp, discount_percentage, selling_price, prescription_required, schedule_type, hsn_code, therapeutic_category, drug_category, storage_instructions, contraindications, side_effects, usage_instructions, dosage_instructions, age_restrictions, pregnancy_category, image_urls, tags, status, is_featured, search_keywords, min_order_quantity, max_order_quantity, expiry_date, batch_number, created_at, updated_at) VALUES

-- Pain Relief & Fever
('Paracetamol', 'Crocin Advance', 'GSK', 'Paracetamol 500mg', 'TABLET', '500mg', '15 tablets', 'Strip', 45.00, 10.00, 40.50, false, NULL, '30049099', 'Analgesic', 'OTC', 'Store below 30°C', 'Hypersensitivity to paracetamol', 'Nausea, skin rash', 'Take with water after meals', '1-2 tablets every 4-6 hours', 'Not for children below 6 years', 'A', '["https://example.com/crocin1.jpg"]', '["fever", "pain", "headache", "paracetamol"]', 'ACTIVE', true, 'crocin paracetamol fever pain headache', 1, 5, '2025-12-31', 'CR240001', NOW(), NOW()),

('Ibuprofen', 'Brufen', 'Abbott', 'Ibuprofen 400mg', 'TABLET', '400mg', '10 tablets', 'Strip', 85.00, 15.00, 72.25, false, NULL, '30049099', 'Anti-inflammatory', 'OTC', 'Store in cool dry place', 'Peptic ulcer, severe heart failure', 'Stomach upset, dizziness', 'Take with food', '1 tablet 2-3 times daily', 'Above 12 years', 'C', '["https://example.com/brufen1.jpg"]', '["pain", "inflammation", "ibuprofen"]', 'ACTIVE', true, 'brufen ibuprofen pain inflammation', 1, 3, '2025-11-30', 'BF240002', NOW(), NOW()),

-- Antibiotics
('Amoxicillin', 'Amoxil', 'GSK', 'Amoxicillin 500mg', 'CAPSULE', '500mg', '10 capsules', 'Strip', 120.00, 5.00, 114.00, true, NULL, '30041000', 'Antibiotic', 'Prescription', 'Store below 25°C', 'Penicillin allergy', 'Diarrhea, nausea, skin rash', 'Complete the full course', '1 capsule 3 times daily for 7 days', 'All ages as per doctor', 'B', '["https://example.com/amoxil1.jpg"]', '["antibiotic", "infection", "amoxicillin"]', 'ACTIVE', false, 'amoxil amoxicillin antibiotic infection', 1, 2, '2025-10-15', 'AX240003', NOW(), NOW()),

('Azithromycin', 'Azithral', 'Alkem', 'Azithromycin 500mg', 'TABLET', '500mg', '3 tablets', 'Strip', 95.00, 8.00, 87.40, true, NULL, '30041000', 'Antibiotic', 'Prescription', 'Store at room temperature', 'Macrolide allergy', 'Stomach pain, diarrhea', 'Take on empty stomach', '1 tablet daily for 3 days', 'Above 6 months', 'B', '["https://example.com/azithral1.jpg"]', '["antibiotic", "azithromycin", "respiratory"]', 'ACTIVE', false, 'azithral azithromycin antibiotic', 1, 2, '2025-09-20', 'AZ240004', NOW(), NOW()),

-- Vitamins & Supplements
('Multivitamin', 'Revital', 'Ranbaxy', 'Multivitamin with Minerals', 'CAPSULE', 'Standard', '30 capsules', 'Bottle', 180.00, 20.00, 144.00, false, NULL, '21069090', 'Vitamin Supplement', 'OTC', 'Store in cool dry place', 'Hypervitaminosis', 'Mild stomach upset', 'Take after breakfast', '1 capsule daily', 'Above 12 years', 'A', '["https://example.com/revital1.jpg"]', '["vitamin", "supplement", "energy", "multivitamin"]', 'ACTIVE', true, 'revital multivitamin supplement energy', 1, 3, '2026-01-31', 'RV240005', NOW(), NOW()),

('Vitamin D3', 'Calcirol', 'Cadila', 'Cholecalciferol 60000 IU', 'CAPSULE', '60000 IU', '4 capsules', 'Strip', 45.00, 12.00, 39.60, false, NULL, '29362900', 'Vitamin', 'OTC', 'Store away from light', 'Hypercalcemia', 'Nausea, weakness', 'Take with milk', '1 capsule weekly', 'All ages', 'A', '["https://example.com/calcirol1.jpg"]', '["vitamin d", "calcium", "bones"]', 'ACTIVE', false, 'calcirol vitamin d3 calcium bones', 1, 2, '2025-08-15', 'CA240006', NOW(), NOW()),

-- Digestive Health
('Pantoprazole', 'Pantop', 'Aristo', 'Pantoprazole 40mg', 'TABLET', '40mg', '10 tablets', 'Strip', 65.00, 18.00, 53.30, false, NULL, '30049099', 'Proton Pump Inhibitor', 'OTC', 'Store below 30°C', 'Hypersensitivity', 'Headache, dizziness', 'Take before meals', '1 tablet daily before breakfast', 'Above 18 years', 'B', '["https://example.com/pantop1.jpg"]', '["acidity", "gastric", "pantoprazole"]', 'ACTIVE', false, 'pantop pantoprazole acidity gastric', 1, 3, '2025-07-30', 'PA240007', NOW(), NOW()),

('Omeprazole', 'Omez', 'Dr. Reddy''s', 'Omeprazole 20mg', 'CAPSULE', '20mg', '10 capsules', 'Strip', 55.00, 10.00, 49.50, false, NULL, '30049099', 'Proton Pump Inhibitor', 'OTC', 'Store in cool dry place', 'Liver disease', 'Nausea, abdominal pain', 'Take before food', '1 capsule daily', 'Above 16 years', 'C', '["https://example.com/omez1.jpg"]', '["acidity", "ulcer", "omeprazole"]', 'ACTIVE', true, 'omez omeprazole acidity ulcer', 1, 2, '2025-06-25', 'OM240008', NOW(), NOW()),

-- Respiratory
('Cetirizine', 'Zyrtec', 'UCB', 'Cetirizine 10mg', 'TABLET', '10mg', '10 tablets', 'Strip', 40.00, 15.00, 34.00, false, NULL, '30049099', 'Antihistamine', 'OTC', 'Store at room temperature', 'Severe kidney disease', 'Drowsiness, dry mouth', 'Take with water', '1 tablet at bedtime', 'Above 6 years', 'B', '["https://example.com/zyrtec1.jpg"]', '["allergy", "antihistamine", "cetirizine"]', 'ACTIVE', false, 'zyrtec cetirizine allergy antihistamine', 1, 2, '2025-05-20', 'ZY240009', NOW(), NOW()),

('Salbutamol', 'Asthalin Inhaler', 'Cipla', 'Salbutamol 100mcg', 'INHALER', '100mcg/puff', '200 puffs', 'Inhaler', 95.00, 8.00, 87.40, true, NULL, '30049099', 'Bronchodilator', 'Prescription', 'Store below 30°C', 'Cardiac arrhythmias', 'Tremor, palpitations', 'Shake before use', '1-2 puffs when needed', 'Above 4 years', 'C', '["https://example.com/asthalin1.jpg"]', '["asthma", "inhaler", "salbutamol", "breathing"]', 'ACTIVE', true, 'asthalin salbutamol asthma inhaler breathing', 1, 2, '2025-04-15', 'AS240010', NOW(), NOW()),

-- Diabetes
('Metformin', 'Glycomet', 'USV', 'Metformin 500mg', 'TABLET', '500mg', '20 tablets', 'Strip', 25.00, 5.00, 23.75, true, NULL, '30049099', 'Antidiabetic', 'Prescription', 'Store in cool dry place', 'Diabetic ketoacidosis', 'Nausea, diarrhea', 'Take with meals', '1 tablet twice daily', 'Above 18 years', 'B', '["https://example.com/glycomet1.jpg"]', '["diabetes", "metformin", "blood sugar"]', 'ACTIVE', false, 'glycomet metformin diabetes sugar', 1, 3, '2025-12-20', 'GL240011', NOW(), NOW()),

-- Heart Health
('Atorvastatin', 'Atorva', 'Zydus Cadila', 'Atorvastatin 10mg', 'TABLET', '10mg', '10 tablets', 'Strip', 85.00, 12.00, 74.80, true, NULL, '30049099', 'Statin', 'Prescription', 'Store below 30°C', 'Active liver disease', 'Muscle pain, headache', 'Take at bedtime', '1 tablet daily at night', 'Above 18 years', 'X', '["https://example.com/atorva1.jpg"]', '["cholesterol", "statin", "heart"]', 'ACTIVE', false, 'atorva atorvastatin cholesterol heart', 1, 2, '2025-11-10', 'AT240012', NOW(), NOW()),

-- Topical/External Use
('Diclofenac Gel', 'Voveran Gel', 'Novartis', 'Diclofenac Diethylamine 1.16%', 'CREAM', '1.16%', '30g', 'Tube', 125.00, 10.00, 112.50, false, NULL, '30049099', 'Topical Anti-inflammatory', 'OTC', 'Store at room temperature', 'Open wounds', 'Skin irritation', 'Apply thin layer', 'Apply 2-3 times daily', 'Above 14 years', 'C', '["https://example.com/voveran1.jpg"]', '["pain relief", "gel", "topical", "muscle pain"]', 'ACTIVE', false, 'voveran diclofenac gel pain relief', 1, 2, '2025-10-05', 'VO240013', NOW(), NOW()),

-- Cough & Cold
('Dextromethorphan', 'Benadryl Cough Syrup', 'Johnson & Johnson', 'Dextromethorphan 10mg/5ml', 'SYRUP', '10mg/5ml', '100ml', 'Bottle', 75.00, 8.00, 69.00, false, NULL, '30049099', 'Cough Suppressant', 'OTC', 'Store in cool place', 'Respiratory depression', 'Drowsiness, nausea', 'Shake well before use', '5-10ml three times daily', 'Above 6 years', 'C', '["https://example.com/benadryl1.jpg"]', '["cough", "syrup", "cold"]', 'ACTIVE', true, 'benadryl cough syrup cold', 1, 2, '2025-09-15', 'BE240014', NOW(), NOW()),

-- Women's Health
('Iron + Folic Acid', 'Fefol', 'GSK', 'Ferrous Sulphate 200mg + Folic Acid 0.5mg', 'CAPSULE', '200mg+0.5mg', '30 capsules', 'Strip', 45.00, 15.00, 38.25, false, NULL, '30049099', 'Hematinic', 'OTC', 'Store away from children', 'Hemochromatosis', 'Constipation, dark stools', 'Take on empty stomach', '1 capsule daily', 'Pregnant women', 'A', '["https://example.com/fefol1.jpg"]', '["iron", "pregnancy", "anemia", "folic acid"]', 'ACTIVE', false, 'fefol iron folic acid pregnancy anemia', 1, 2, '2025-08-30', 'FE240015', NOW(), NOW());

-- Insert Orders
INSERT INTO orders (order_number, customer_id, delivery_address_id, order_status, delivery_type, scheduled_delivery_time, subtotal, delivery_charges, service_charges, discount_amount, tax_amount, total_amount, payment_method, payment_status, payment_id, prescription_required, prescription_uploaded, estimated_delivery_time, delivery_otp, created_at, updated_at) VALUES
('DS240001001', 2, 1, 'DELIVERED', 'STANDARD', NULL, 125.50, 20.00, 6.28, 0.00, 7.58, 159.36, 'UPI', 'COMPLETED', 'pay_123456789', false, false, NOW() + INTERVAL '2 hours', '123456', NOW() - INTERVAL '2 days', NOW()),
('DS240002001', 3, 3, 'OUT_FOR_DELIVERY', 'EXPRESS', NULL, 87.40, 50.00, 6.87, 10.00, 6.71, 141.98, 'CREDIT_CARD', 'COMPLETED', 'pay_987654321', true, true, NOW() + INTERVAL '1 hour', '789012', NOW() - INTERVAL '1 day', NOW()),
('DS240003001', 4, 5, 'CONFIRMED', 'STANDARD', NULL, 74.80, 20.00, 4.74, 0.00, 4.98, 104.52, 'COD', 'PENDING', NULL, true, false, NOW() + INTERVAL '3 hours', '345678', NOW() - INTERVAL '3 hours', NOW()),
('DS240004001', 5, 6, 'PLACED', 'STANDARD', NULL, 144.00, 20.00, 8.20, 20.00, 7.61, 159.81, 'DEBIT_CARD', 'PROCESSING', 'pay_555666777', false, false, NOW() + INTERVAL '4 hours', '901234', NOW() - INTERVAL '30 minutes', NOW());

-- Insert Order Items
INSERT INTO order_items (order_id, medicine_id, quantity, unit_price, total_price, discount_percentage, discount_amount, tax_percentage, tax_amount, final_price, batch_number, expiry_date, is_prescription_required, created_at, updated_at) VALUES
-- Order 1 Items (Delivered)
(1, 1, 2, 40.50, 81.00, 0.00, 0.00, 5.00, 4.05, 85.05, 'CR240001', '2025-12-31', false, NOW() - INTERVAL '2 days', NOW()),
(1, 8, 1, 49.50, 49.50, 10.00, 4.95, 5.00, 2.23, 46.78, 'OM240008', '2025-06-25', false, NOW() - INTERVAL '2 days', NOW()),

-- Order 2 Items (Out for Delivery - Antibiotic)
(2, 4, 1, 87.40, 87.40, 0.00, 0.00, 5.00, 4.37, 91.77, 'AZ240004', '2025-09-20', true, NOW() - INTERVAL '1 day', NOW()),

-- Order 3 Items (Confirmed - Prescription required)
(3, 12, 1, 74.80, 74.80, 0.00, 0.00, 5.00, 3.74, 78.54, 'AT240012', '2025-11-10', true, NOW() - INTERVAL '3 hours', NOW()),

-- Order 4 Items (Placed)
(4, 5, 1, 144.00, 144.00, 15.00, 21.60, 5.00, 6.12, 128.52, 'RV240005', '2026-01-31', false, NOW() - INTERVAL '30 minutes', NOW());

COMMIT;

-- Display summary
SELECT 'Data insertion completed successfully!' as message;
SELECT 'Users inserted: ' || COUNT(*) as user_count FROM users;
SELECT 'Addresses inserted: ' || COUNT(*) as address_count FROM user_addresses;
SELECT 'Medicines inserted: ' || COUNT(*) as medicine_count FROM medicines;
SELECT 'Orders inserted: ' || COUNT(*) as order_count FROM orders;
SELECT 'Order items inserted: ' || COUNT(*) as order_item_count FROM order_items;

-- Show admin user details
SELECT 'Admin User Details:' as info;
SELECT user_id, phone_number, email, full_name, role, account_status 
FROM users 
WHERE role = 'ADMIN';