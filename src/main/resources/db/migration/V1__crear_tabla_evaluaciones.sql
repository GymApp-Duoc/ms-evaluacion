CREATE TABLE evaluaciones (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              miembro_id BIGINT NOT NULL,
                              evaluador_id BIGINT NOT NULL,
                              fecha_evaluacion DATE NOT NULL,
                              peso DOUBLE NOT NULL,
                              porcentaje_grasa DOUBLE,
                              masa_muscular DOUBLE,
                              observaciones VARCHAR(1000),
                              activo BOOLEAN NOT NULL DEFAULT TRUE
);

