-- V2__seed_datos_ejemplo.sql
-- Datos de ejemplo para facilitar pruebas con Postman.

-- Vehículos
INSERT INTO vehiculos (id, patente, marca, modelo, anio, kilometraje_actual)
VALUES
    (1, 'AA123BB', 'TOYOTA', 'COROLLA', 2020, 45000),
    (2, 'AB456CD', 'FORD', 'FOCUS', 2018, 80000),
    (3, 'AC789EF', 'VOLKSWAGEN', 'GOLF', 2019, 62000);

-- Mantenimientos
-- Estados esperados: PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO
-- Tipos esperados: (según tus enums) por ej: INTERIOR, CHASIS, PINTURA, MECANICA, etc.

INSERT INTO mantenimientos (
    id, vehiculo_id, tipo_mantenimiento, descripcion, fecha_creacion, estado, costo_estimado, costo_final
)
VALUES
    (100, 1, 'CAMBIO_ACEITE', 'Cambio de aceite y filtro', NOW(), 'PENDIENTE', 1000.00, NULL),
    (101, 1, 'FRENOS', 'Rectificacion de discos de freno', NOW(), 'EN_PROCESO', 2500.00, NULL),
    (102, 2, 'TRANSMISION', 'Cambio de aceite de caja', NOW(), 'COMPLETADO', 8000.00, 8200.00),
    (103, 3, 'GENERAL', 'Revisión general de tren delantero', NOW(), 'CANCELADO', 5000.00, NULL);

-- Ajustar secuencias para que no choquen con IDs insertados manualmente
SELECT setval(pg_get_serial_sequence('vehiculos', 'id'), (SELECT MAX(id) FROM vehiculos));
SELECT setval(pg_get_serial_sequence('mantenimientos', 'id'), (SELECT MAX(id) FROM mantenimientos));
