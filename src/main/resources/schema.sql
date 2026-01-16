
-- schema.sql
CREATE TABLE IF NOT EXISTS localidad (
    localidad_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pais VARCHAR(100) NOT NULL,
    nombre_ciudad VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(12) NOT NULL -- Tamaño 12 ya que hay paises que tienen codigos mas largos
);

CREATE TABLE IF NOT EXISTS usuario (
    usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    localidad_id BIGINT NOT NULL,
    fecha_registro DATETIME NOT NULL,
    CONSTRAINT fk_usuario_localidad FOREIGN KEY (localidad_id) REFERENCES localidad(localidad_id)
);

CREATE TABLE IF NOT EXISTS usuario_generos_favoritos (
    usuario_id BIGINT NOT NULL,
    genero_favorito VARCHAR(50),
    PRIMARY KEY (usuario_id, genero_favorito),
    CONSTRAINT fk_ugf_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id)
);