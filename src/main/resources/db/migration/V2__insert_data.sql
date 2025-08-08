-- Insert mock staff
INSERT INTO staff (name, email, phone, role, join_date, password) VALUES
('Dr. Sarah Johnson', 'sarah.johnson@hospital.com', '+1234567890', 'Doctor', '2020-01-15', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy'),
('Dr. Michael Brown', 'michael.brown@hospital.com', '+1987654321', 'Doctor', '2019-05-20', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy'),
('Nurse Emily Davis', 'emily.davis@hospital.com', '+1122334455', 'Nurse', '2021-03-10', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy'),
('Dr. Robert Wilson', 'robert.wilson@hospital.com', '+1555666777', 'Doctor', '2018-11-05', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy');

-- Insert mock patients
INSERT INTO patients (name, age, sex, blood_group, is_active, email, password) VALUES
('John Doe', 45, 'Male', 'A+', true, 'john.doe@hospital.com', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy'),
('Jane Smith', 32, 'Female', 'B-', true, 'jane.smith@hospital.com', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy'),
('Michael Johnson', 58, 'Male', 'O+', true, 'micheal.johnson@hospital.com', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy'),
('Emily Williams', 29, 'Female', 'AB+', true, 'emily.williams@hospital.com', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy'),
('David Brown', 63, 'Male', 'A-', true, 'david.brown@hospital.com', '$2a$10$/cgtOfdpdeSkjTX2rlJw8.QXdy6EoF7Z11Qktx7cF4XdjXJd0tJuy');

-- Insert mock allergies
INSERT INTO allergies (name, type, severity, reaction, noted_on, patient_id) VALUES
('Penicillin', 'Drug', 'Severe', 'Anaphylaxis', '2022-03-12', 1),
('Pollen', 'Environmental', 'Moderate', 'Sneezing', '2023-04-02', 1),
('Peanuts', 'Food', 'Severe', 'Swelling', '2021-06-20', 1),
('Shellfish', 'Food', 'Moderate', 'Hives', '2020-11-15', 2),
('Dust', 'Environmental', 'Mild', 'Itchy eyes', '2023-01-10', 3),
('Latex', 'Environmental', 'Severe', 'Rash', '2022-08-25', 4),
('Aspirin', 'Drug', 'Moderate', 'Stomach pain', '2021-12-05', 5);

-- Insert mock vitals
INSERT INTO vitals (reading, type, measured_at, status, patient_id) VALUES
('120/80 mmHg', 'Blood Pressure', '2023-05-01 08:00:00', 'Normal', 1),
('98.6°F', 'Body Temperature', '2023-05-01 08:05:00', 'Normal', 1),
('72 bpm', 'Pulse Rate', '2023-05-01 08:10:00', 'Normal', 1),
('97%', 'Blood Oxygen', '2023-05-01 08:15:00', 'Normal', 1),
('95 mg/dL', 'Sugar Level', '2023-05-01 08:20:00', 'Normal', 1),
('130/85 mmHg', 'Blood Pressure', '2023-05-02 08:00:00', 'Elevated', 1),
('99.1°F', 'Body Temperature', '2023-05-02 08:05:00', 'Fever', 1),
('118/78 mmHg', 'Blood Pressure', '2023-05-01 09:00:00', 'Normal', 2),
('98.2°F', 'Body Temperature', '2023-05-01 09:05:00', 'Normal', 2),
('68 bpm', 'Pulse Rate', '2023-05-01 09:10:00', 'Normal', 2),
('96%', 'Blood Oxygen', '2023-05-01 09:15:00', 'Normal', 2),
('102 mg/dL', 'Sugar Level', '2023-05-01 09:20:00', 'Elevated', 2);

-- Insert mock appointments
INSERT INTO appointments (patient_id, staff_id, time, status, notes, prescription) VALUES
(1, 1, '2023-05-10 09:00:00', 'Scheduled', 'Annual checkup', NULL),
(1, 2, '2023-05-15 10:30:00', 'Completed', 'Follow-up for blood pressure', 'Lisinopril 10mg daily'),
(2, 1, '2023-05-11 11:00:00', 'Scheduled', 'New patient consultation', NULL),
(3, 3, '2023-05-12 14:00:00', 'Completed', 'Vaccination', 'Tetanus booster'),
(4, 4, '2023-05-16 13:30:00', 'Scheduled', 'Post-surgery follow-up', NULL),
(5, 2, '2023-05-17 15:00:00', 'Cancelled', 'Patient rescheduled', NULL);

-- Insert mock messages
INSERT INTO messages (text, is_user_message, timestamp, staff_id, patient_id) VALUES
('Hello, how are you feeling today?', false, '2023-05-01 09:00:00', 1, 1),
('I am feeling better, thank you.', true, '2023-05-01 09:05:00', 1, 1),
('Please remember to take your medication.', false, '2023-05-02 10:00:00', 1, 1),
('Your test results are ready.', false, '2023-05-03 11:30:00', 2, 1),
('When is my next appointment?', true, '2023-05-04 12:15:00', 1, 2),
('Your next appointment is on May 11 at 11 AM.', false, '2023-05-04 12:30:00', 1, 2);