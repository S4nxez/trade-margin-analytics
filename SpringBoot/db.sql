CREATE TABLE patients (
                          patient_id INT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(50) NOT NULL,
                          date_of_birth DATE,
                          phone VARCHAR(20),
                          PRIMARY KEY (patient_id),
                          CONSTRAINT unique_name_dob UNIQUE (name, date_of_birth)
);

CREATE TABLE doctors (
                         doctor_id INT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(50) NOT NULL,
                         specialization VARCHAR(100) UNIQUE,
                         phone VARCHAR(20),
                         PRIMARY KEY (doctor_id)
);

CREATE TABLE medical_records (
                                 record_id INT NOT NULL AUTO_INCREMENT,
                                 patient_id INT NOT NULL,
                                 doctor_id INT NOT NULL,
                                 diagnosis TEXT,
                                 admission_date DATE,
                                 PRIMARY KEY (record_id),
                                 FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
                                 FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

CREATE TABLE appointments (
                              appointment_id INT NOT NULL AUTO_INCREMENT,
                              patient_id INT NOT NULL,
                              doctor_id INT NOT NULL,
                              appointment_date DATETIME,
                              PRIMARY KEY (appointment_id),
                              FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
                              FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);
CREATE TABLE prescribed_medications (
                                        prescription_id INT NOT NULL AUTO_INCREMENT,
                                        record_id INT NOT NULL,
                                        medication_name VARCHAR(100),
                                        dosage VARCHAR(50),
                                        PRIMARY KEY (prescription_id),
                                        FOREIGN KEY (record_id) REFERENCES medical_records(record_id)
);

CREATE TABLE patient_payments (
                                  payment_id INT NOT NULL AUTO_INCREMENT,
                                  patient_id INT NOT NULL,
                                  amount DECIMAL(10,2),
                                  payment_date DATE,
                                  PRIMARY KEY (payment_id),
                                  FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

CREATE TABLE user_login (
                            user_id INT NOT NULL AUTO_INCREMENT,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            password VARCHAR(100) NOT NULL,
                            patient_id INT,
                            doctor_id INT,
                            PRIMARY KEY (user_id),
                            FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
                            FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- Inserting data into the patients table
INSERT INTO patients (name, date_of_birth, phone)
VALUES
    ('John Smith', '1980-05-15', '555-1234'),
    ('Emily Johnson', '1975-10-20', '555-5678'),
    ('Michael Brown', '1992-03-08', '555-4321');

-- Inserting data into the doctors table
INSERT INTO doctors (name, specialization, phone)
VALUES
    ('Dr. Sarah White', 'Cardiologist', '555-1111'),
    ('Dr. David Miller', 'Pediatrician', '555-2222'),
    ('Dr. Laura Wilson', 'Dermatologist', '555-3333');

-- Inserting data into the medical_records table
INSERT INTO medical_records (patient_id, doctor_id, diagnosis, admission_date)
VALUES
    (1, 1, 'Hypertension', '2023-01-10'),
    (2, 2, 'Respiratory Infection', '2023-02-05'),
    (3, 3, 'Eczema', '2023-03-15');

-- Inserting data into the appointments table
INSERT INTO appointments (patient_id, doctor_id, appointment_date)
VALUES
    (1, 1, '2023-01-15 09:00:00'),
    (2, 2, '2023-02-10 10:30:00'),
    (3, 3, '2023-03-20 14:00:00');

-- Inserting data into the prescribed_medications table
INSERT INTO prescribed_medications (record_id, medication_name, dosage)
VALUES
    (1, 'Losartan', '50mg'),
    (2, 'Amoxicillin', '500mg'),
    (3, 'Moisturizing Creams', 'As needed');

-- Inserting data into the patient_payments table
INSERT INTO patient_payments (patient_id, amount, payment_date)
VALUES
    (1, 150.00, '2023-01-20'),
    (2, 100.00, '2023-02-15'),
    (3, 80.00, '2023-03-25');

-- Inserting data into the user_login table
INSERT INTO user_login (username, password, patient_id, doctor_id)
VALUES
    ('patient1', 'passwordp1', 1, NULL),
    ('patient2', 'passwordp2', 2, NULL),
    ('patient3', 'passwordp3', 3, NULL),
    ('doctor1', 'passwordd1', NULL, 1),
    ('doctor2', 'passwordd2', NULL, 2),
    ('doctor3', 'passwordd3', NULL, 3),
    ('root', 'quevedo2dam', NULL, NULL);