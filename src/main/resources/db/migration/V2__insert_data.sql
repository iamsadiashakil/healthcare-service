INSERT INTO patients (name, dob, gender, phone) VALUES
('John Doe', '1985-04-12', 'Male', '+1234567890'),
('Jane Smith', '1990-09-22', 'Female', '+9876543210');

INSERT INTO doctors (name, specialization, phone) VALUES
('Dr. Sarah Connor', 'Cardiology', '+1122334455'),
('Dr. Alan Walker', 'Orthopedics', '+2233445566');

INSERT INTO appointments (patient_id, doctor_id, appointment_date, status) VALUES
(1, 1, '2025-07-25 10:00:00', 'SCHEDULED'),
(2, 2, '2025-07-26 14:30:00', 'COMPLETED'),
(1, 2, '2025-07-28 09:00:00', 'CANCELLED'),
(2, 1, '2025-07-30 16:00:00', 'SCHEDULED');