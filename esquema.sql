CREATE DATABASE IF NOT EXISTS gestion_empleados_db;
USE gestion_empleados_db;

CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    fecha_contratacion DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Datos de prueba iniciales para empezar a programar
INSERT INTO empleados (nombre, departamento, salario, fecha_contratacion, activo) 
VALUES ('Ana Lucía Pérez', 'Sistemas', 8500.00, '2024-03-15', TRUE);