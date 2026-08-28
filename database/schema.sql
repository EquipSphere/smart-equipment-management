-- ==========================================================
-- Smart Equipment Booking & Maintenance System
-- Database Schema (PostgreSQL)
-- ==========================================================

-- 1. Create Database (Run this separately if not already created)
-- CREATE DATABASE smart_equipment_db;

-- Connect to smart_equipment_db before running below queries:
-- \c smart_equipment_db;

-- 2. Drop existing tables (in reverse order of foreign key dependencies)
DROP TABLE IF EXISTS maintenance CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS equipment CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ----------------------------------------------------------
-- Table: users
-- Roles: ADMIN, USER
-- ----------------------------------------------------------
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('ADMIN', 'USER')),
    phone VARCHAR(20),
    department VARCHAR(100),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------
-- Table: equipment
-- Status: AVAILABLE, BOOKED, MAINTENANCE
-- ----------------------------------------------------------
CREATE TABLE equipment (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(100) NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(150) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'BOOKED', 'MAINTENANCE')),
    description TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------
-- Table: bookings
-- Status: PENDING, APPROVED, REJECTED, CANCELLED, COMPLETED
-- ----------------------------------------------------------
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED', 'COMPLETED')),
    purpose TEXT,
    admin_remark TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE,
    CONSTRAINT chk_booking_time CHECK (end_time > start_time)
);

-- ----------------------------------------------------------
-- Table: maintenance
-- Status: REPORTED, UNDER_MAINTENANCE, REPAIRED, CANCELLED
-- ----------------------------------------------------------
CREATE TABLE maintenance (
    id BIGSERIAL PRIMARY KEY,
    equipment_id BIGINT NOT NULL,
    reported_by BIGINT NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'REPORTED' CHECK (status IN ('REPORTED', 'UNDER_MAINTENANCE', 'REPAIRED', 'CANCELLED')),
    cost NUMERIC(10, 2) DEFAULT 0.00,
    technician_notes TEXT,
    reported_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_maintenance_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE,
    CONSTRAINT fk_maintenance_reported_by FOREIGN KEY (reported_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes for high performance searches & conflict detection
CREATE INDEX idx_equipment_status ON equipment(status);
CREATE INDEX idx_equipment_category ON equipment(category);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_equipment ON bookings(equipment_id);
CREATE INDEX idx_bookings_timerange ON bookings(equipment_id, start_time, end_time, status);
CREATE INDEX idx_maintenance_equipment ON maintenance(equipment_id);
CREATE INDEX idx_maintenance_status ON maintenance(status);
