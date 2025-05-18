-- Insertar datos de prueba en la tabla historiales_medicos
INSERT INTO historiales_medicos (id, paciente_id)
VALUES (1, 101),
    (2, 102);
-- Insertar datos de prueba en la tabla historial_entradas
INSERT INTO historial_entradas (
        id,
        historial_id,
        fecha,
        tipo,
        descripcion,
        origen_id
    )
VALUES (
        1,
        1,
        '2023-01-15',
        'CITA',
        'Consulta general con el médico.',
        'ORIGEN123'
    ),
    (
        2,
        1,
        '2023-02-10',
        'PAGO',
        'Pago de consulta médica.',
        'ORIGEN124'
    ),
    (
        3,
        2,
        '2023-03-05',
        'RESEÑA',
        'Reseña del servicio recibido.',
        'ORIGEN125'
    );