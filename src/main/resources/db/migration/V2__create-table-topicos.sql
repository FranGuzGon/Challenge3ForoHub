CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN NOT NULL,
    autor_id BIGINT NOT NULL,
    curso VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);