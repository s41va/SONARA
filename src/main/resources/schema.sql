CREATE TABLE IF NOT EXISTS Localidad (
    localidad_id INT AUTO_INCREMENT PRIMARY KEY,
    pais VARCHAR(100),
    nombre_ciudad VARCHAR(100),
    codigo_postal VARCHAR(10)
    );

CREATE TABLE IF NOT EXISTS Genero (
    genero_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    descripcion TEXT
    );
CREATE TABLE IF NOT EXISTS Artista (
    artista_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_artistico VARCHAR(150),
    pais_origen VARCHAR(100),
    descripcion TEXT,
    genero_id INT,
    CONSTRAINT fk_artista_genero
    FOREIGN KEY (genero_id) REFERENCES Genero(genero_id)
    );

CREATE TABLE IF NOT EXISTS Usuario (
    usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    contrasena VARCHAR(255),
    fecha_nacimiento DATE,
    localidad_id INT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_localidad
    FOREIGN KEY (localidad_id) REFERENCES Localidad(localidad_id)
    );
CREATE TABLE IF NOT EXISTS usuario_generos_favoritos (
    usuario_id INT NOT NULL,
    genero_id INT NOT NULL,

    PRIMARY KEY (usuario_id, genero_id),

    CONSTRAINT fk_ugf_usuario
    FOREIGN KEY (usuario_id) REFERENCES Usuario(usuario_id)
    ON DELETE CASCADE,

    CONSTRAINT fk_ugf_genero
    FOREIGN KEY (genero_id) REFERENCES Genero(genero_id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Cancion (
    cancion_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200),
    artista_id INT,
    genero_id INT,
    fecha_lanzamiento DATE,
    duracion INT,
    album VARCHAR(200),
    CONSTRAINT fk_cancion_artista
    FOREIGN KEY (artista_id) REFERENCES Artista(artista_id),
    CONSTRAINT fk_cancion_genero
    FOREIGN KEY (genero_id) REFERENCES Genero(genero_id)
    );
