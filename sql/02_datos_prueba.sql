-- =====================================================================
-- Fercementos Spring - datos de prueba (opcional)
-- El esquema oficial se creo sin datos; estos INSERT permiten probar
-- la aplicacion sin tener que capturar todo a mano.
-- =====================================================================

USE ferreteria;

INSERT INTO categorias (nombre_categoria) VALUES
    ('Materiales'), ('Herramientas'), ('Electricidad'), ('Plomeria'), ('Pintura');

INSERT INTO proveedores (nombre, telefono, direccion) VALUES
    ('Cementos Argos',        '018000-111222', 'Medellin, Antioquia'),
    ('Ferrasa',               '604-4448899',   'Itagui, Antioquia'),
    ('Electricos del Quindio','606-7413355',   'Armenia, Quindio');

INSERT INTO clientes (nombre, telefono, direccion) VALUES
    ('Construcciones AXM SAS', '310-5551234', 'Cra 14 # 10-25, Armenia'),
    ('Maria Fernanda Rios',    '315-8887766', 'Calle 21 # 15-40, Armenia');

INSERT INTO productos (nombre, precio, stock, id_categoria, id_proveedor) VALUES
    ('Cemento gris x 50Kg',         32000.00, 100, 1, 1),
    ('Varilla corrugada 1/2" x 6m', 28500.00,  60, 1, 2),
    ('Taladro percutor 1/2"',      185000.00,  15, 2, 2),
    ('Cable encauchetado #12 (m)',   2300.00, 300, 3, 3),
    ('Tubo PVC 1/2" x 6m',           9800.00,  80, 4, 2),
    ('Pintura vinilo blanco 1 gal', 62000.00,  40, 5, 2);
