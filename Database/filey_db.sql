
DROP DATABASE IF EXISTS filey_db;
CREATE DATABASE filey_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE filey_db;

CREATE TABLE editoriales (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email_contacto VARCHAR(100) NOT NULL
);

CREATE TABLE stands (
    id VARCHAR(20) PRIMARY KEY,
    metros_cuadrados DOUBLE NOT NULL,
    valor_metro_cuadrado DOUBLE NOT NULL,
    estado ENUM('DISPONIBLE', 'RESERVADO', 'OCUPADO', 'BLOQUEADO') NOT NULL DEFAULT 'DISPONIBLE'
);

CREATE TABLE reservaciones (
    id_reservacion INT PRIMARY KEY AUTO_INCREMENT,
    id_stand VARCHAR(20) NOT NULL,
    id_editorial VARCHAR(50) NOT NULL,
    monto_pagado DOUBLE NOT NULL DEFAULT 0.0,
    limite_reserva DATETIME NULL,
    limite_pronto_pago DATETIME NULL,
    limite_pago_total DATETIME NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_reservacion_stand FOREIGN KEY (id_stand) 
        REFERENCES stands(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_reservacion_editorial FOREIGN KEY (id_editorial) 
        REFERENCES editoriales(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO stands (id, metros_cuadrados, valor_metro_cuadrado, estado) VALUES
('A1', 10.0, 1000.0, 'DISPONIBLE'),
('B2', 15.0, 1200.0, 'DISPONIBLE'),
('C3', 20.0, 1000.0, 'BLOQUEADO'),
('D4', 12.0, 1100.0, 'DISPONIBLE');