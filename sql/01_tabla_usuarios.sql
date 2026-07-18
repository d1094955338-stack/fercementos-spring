-- =====================================================================
-- Fercementos Spring - tabla de usuarios
--
-- Esta tabla COMPLEMENTA el esquema oficial del proyecto
-- ("Script bases de datos del proyecto. Evidencia GA6-220501096-AA2-EV03"),
-- que ya contiene: categorias, clientes, detalle_venta, productos,
-- proveedores y ventas. La tabla usuarios da soporte al modulo de
-- inicio de sesion / registro definido en el prototipo Balsamiq y en
-- el diagrama de funcionalidades del proyecto.
--
-- Ejecutar sobre la base de datos "ferreteria" DESPUES del script oficial.
-- =====================================================================

USE ferreteria;

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario        INT NOT NULL AUTO_INCREMENT,
    nombre_usuario    VARCHAR(50)  NOT NULL,
    -- Hash BCrypt (60 caracteres); se reserva espacio adicional.
    contrasena        VARCHAR(100) NOT NULL,
    correo            VARCHAR(100) DEFAULT NULL,
    documento         VARCHAR(20)  DEFAULT NULL,
    fecha_nacimiento  DATE         DEFAULT NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_nombre_usuario (nombre_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
