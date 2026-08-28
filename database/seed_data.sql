-- ==========================================================
-- Smart Equipment Booking & Maintenance System
-- Seed / Sample Data (PostgreSQL)
-- ==========================================================

-- 1. Insert Users (Password: 'password123' or bcrypt hash)
-- Note: In production / Spring Security, use BCryptPasswordEncoder.
INSERT INTO users (name, email, password, role, phone, department) VALUES
('System Administrator', 'admin@equipsphere.com', '$2a$10$eACCYoNOHEqgkZ560mDyeu91zE2F89qO1v1rX6l5oT8.r321UoYvy', 'ADMIN', '+94771234567', 'IT & Operations'),
('John Doe', 'john@university.edu', '$2a$10$eACCYoNOHEqgkZ560mDyeu91zE2F89qO1v1rX6l5oT8.r321UoYvy', 'USER', '+94779876543', 'Computer Science'),
('Jane Smith', 'jane@university.edu', '$2a$10$eACCYoNOHEqgkZ560mDyeu91zE2F89qO1v1rX6l5oT8.r321UoYvy', 'USER', '+94775551122', 'Electrical Engineering'),
('David Silva', 'david@university.edu', '$2a$10$eACCYoNOHEqgkZ560mDyeu91zE2F89qO1v1rX6l5oT8.r321UoYvy', 'USER', '+94778889900', 'Mechanical Engineering'),
('Kavindi Fernando', 'kavindi@university.edu', '$2a$10$eACCYoNOHEqgkZ560mDyeu91zE2F89qO1v1rX6l5oT8.r321UoYvy', 'USER', '+94773334455', 'Media Studies');

-- 2. Insert Sample Equipment (Categorized & with realistic locations)
INSERT INTO equipment (name, category, serial_number, location, status, description, image_url) VALUES
('Epson EB-2250U Projector 01', 'Projector', 'PRJ-EPS-001', 'Lab 101, Main Building', 'AVAILABLE', 'Full HD 5000 Lumens Presentation Projector', 'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=500'),
('Epson EB-2250U Projector 02', 'Projector', 'PRJ-EPS-002', 'Seminar Room 02', 'BOOKED', 'Full HD 5000 Lumens Presentation Projector with HDMI', 'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=500'),
('BenQ 4K HDR Projector 03', 'Projector', 'PRJ-BNQ-003', 'Auditorium A', 'AVAILABLE', '4K Ultra HD Cinema Projector for big events', 'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=500'),
('Dell XPS 15 Developer Laptop', 'Laptop', 'LAP-DEL-001', 'IT Resource Center', 'AVAILABLE', 'Intel Core i9, 32GB RAM, 1TB SSD, RTX 4060', 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=500'),
('Apple MacBook Pro 16 M3', 'Laptop', 'LAP-MAC-002', 'Media Lab B', 'BOOKED', 'Apple M3 Pro, 18GB RAM, 512GB SSD', 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500'),
('Lenovo ThinkPad P16 Workstation', 'Laptop', 'LAP-LEN-003', 'Robotics Lab', 'MAINTENANCE', 'Battery replacement ongoing. Hardware diagnostic.', 'https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=500'),
('Sony Alpha A7 IV Camera 01', 'Camera', 'CAM-SNY-001', 'Media Center', 'AVAILABLE', 'Full-frame Mirrorless Camera with 28-70mm Lens', 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=500'),
('Canon EOS R6 Mark II Camera 02', 'Camera', 'CAM-CAN-002', 'Media Center', 'BOOKED', '24.2 MP Mirrorless Camera with 4K60p recording', 'https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=500'),
('Nikon D850 DSLR Camera 03', 'Camera', 'CAM-NIK-003', 'Photography Studio', 'AVAILABLE', '45.7 MP Professional DSLR with 50mm f/1.8 lens', 'https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=500'),
('Rode Wireless GO II Microphone Kit', 'Microphone', 'MIC-ROD-001', 'Audio Studio 01', 'AVAILABLE', 'Dual channel wireless microphone system for interviews', 'https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=500'),
('Shure SM7B Vocal Dynamic Mic', 'Microphone', 'MIC-SHU-002', 'Podcast Room 03', 'AVAILABLE', 'Broadcast dynamic vocal microphone with boom arm', 'https://images.unsplash.com/photo-1583244532610-2a234e7c3eca?w=500'),
('Arduino Ultimate Starter Kit 01', 'Arduino Kit', 'ARD-STK-001', 'Embedded Systems Lab', 'AVAILABLE', 'Includes Arduino UNO R3, sensors, LCD, stepper motors', 'https://images.unsplash.com/photo-1553406830-ef2513450d76?w=500'),
('Arduino Robotics IoT Kit 02', 'Arduino Kit', 'ARD-IOT-002', 'Embedded Systems Lab', 'AVAILABLE', 'ESP32 & Arduino Wi-Fi IoT development bundle', 'https://images.unsplash.com/photo-1553406830-ef2513450d76?w=500'),
('Raspberry Pi 5 8GB Lab Kit 01', 'Embedded Kit', 'RPI-005-001', 'IoT Research Lab', 'AVAILABLE', 'Raspberry Pi 5 with Active Cooler, Power Adapter, 64GB Card', 'https://images.unsplash.com/photo-1629654297299-c8506221ca97?w=500'),
('DJI RS 3 Pro Gimbal Stabilizer', 'Accessories', 'ACC-DJI-001', 'Media Center', 'MAINTENANCE', 'Motor calibration error reported on pitch axis', 'https://images.unsplash.com/photo-1527011046414-4781f1f94f8c?w=500');

-- 3. Insert Sample Bookings
INSERT INTO bookings (user_id, equipment_id, start_time, end_time, status, purpose, admin_remark) VALUES
(2, 2, '2026-08-28 10:00:00', '2026-08-28 13:00:00', 'APPROVED', 'Software Engineering Final Year Presentation', 'Approved for Lab 101'),
(3, 5, '2026-08-29 09:00:00', '2026-08-29 17:00:00', 'APPROVED', 'Video editing and rendering for IEEE Student Branch', 'Approved'),
(4, 8, '2026-08-30 14:00:00', '2026-08-30 18:00:00', 'PENDING', 'Photography coverage for Annual Sports Meet', NULL),
(2, 1, '2026-08-25 09:00:00', '2026-08-25 12:00:00', 'COMPLETED', 'Guest lecture on Artificial Intelligence', 'Completed smoothly'),
(5, 7, '2026-08-27 11:00:00', '2026-08-27 15:00:00', 'REJECTED', 'Personal photoshoot', 'Equipment reserved only for academic and club events');

-- 4. Insert Sample Maintenance Records
INSERT INTO maintenance (equipment_id, reported_by, description, status, cost, technician_notes, reported_at, resolved_at) VALUES
(6, 4, 'ThinkPad laptop battery drains within 10 minutes and system overheats.', 'UNDER_MAINTENANCE', 15000.00, 'New battery ordered from distributor, awaiting delivery.', '2026-08-20 10:30:00', NULL),
(15, 5, 'DJI RS3 Gimbal keeps showing Motor Overload error on the pitch axis.', 'REPORTED', 0.00, 'Pending technician inspection.', '2026-08-25 16:45:00', NULL),
(1, 2, 'HDMI port was loose and projector was flickering during display.', 'REPAIRED', 3500.00, 'Replaced internal HDMI ribbon connector. Working normally now.', '2026-08-10 09:00:00', '2026-08-12 14:00:00');
