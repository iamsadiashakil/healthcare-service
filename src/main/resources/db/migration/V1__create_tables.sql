CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    sex VARCHAR(10) NOT NULL,
    blood_group VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE allergies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    reaction VARCHAR(100) NOT NULL,
    noted_on DATE NOT NULL,
    patient_id BIGINT REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE vitals (
    id BIGSERIAL PRIMARY KEY,
    reading VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    measured_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    patient_id BIGINT REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE staff (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(50) NOT NULL,
    join_date DATE NOT NULL
);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    is_user_message BOOLEAN NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    staff_id BIGINT REFERENCES staff(id) ON DELETE CASCADE,
    patient_id BIGINT REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT REFERENCES patients(id) ON DELETE CASCADE,
    staff_id BIGINT REFERENCES staff(id) ON DELETE CASCADE,
    time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes TEXT,
    prescription TEXT
);