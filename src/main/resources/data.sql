-- ==========================
-- LOCALIDADES
-- ==========================
INSERT INTO Localidad (pais, nombre_ciudad, codigo_postal)
SELECT 'España', 'Madrid', '28001'
    WHERE NOT EXISTS (
    SELECT 1 FROM Localidad
    WHERE pais = 'España' AND nombre_ciudad = 'Madrid'
);

INSERT INTO Localidad (pais, nombre_ciudad, codigo_postal)
SELECT 'España', 'Barcelona', '08001'
    WHERE NOT EXISTS (
    SELECT 1 FROM Localidad
    WHERE pais = 'España' AND nombre_ciudad = 'Barcelona'
);

INSERT INTO Localidad (pais, nombre_ciudad, codigo_postal)
SELECT 'México', 'Ciudad de México', '01000'
    WHERE NOT EXISTS (
    SELECT 1 FROM Localidad
    WHERE pais = 'México' AND nombre_ciudad = 'Ciudad de México'
);

INSERT INTO Localidad (pais, nombre_ciudad, codigo_postal)
SELECT 'Argentina', 'Buenos Aires', '1000'
    WHERE NOT EXISTS (
    SELECT 1 FROM Localidad
    WHERE pais = 'Argentina' AND nombre_ciudad = 'Buenos Aires'
);

-- ==========================
-- GENEROS
-- ==========================
INSERT INTO Genero (nombre, descripcion)
SELECT 'Pop', 'Música popular de amplio alcance y melodías pegadizas'
    WHERE NOT EXISTS (SELECT 1 FROM Genero WHERE nombre = 'Pop');

INSERT INTO Genero (nombre, descripcion)
SELECT 'Rock', 'Música con guitarras eléctricas y ritmos potentes'
    WHERE NOT EXISTS (SELECT 1 FROM Genero WHERE nombre = 'Rock');

INSERT INTO Genero (nombre, descripcion)
SELECT 'Hip-Hop', 'Música urbana con rap y ritmo marcado'
    WHERE NOT EXISTS (SELECT 1 FROM Genero WHERE nombre = 'Hip-Hop');

INSERT INTO Genero (nombre, descripcion)
SELECT 'Electrónica', 'Música creada con sintetizadores y electrónica'
    WHERE NOT EXISTS (SELECT 1 FROM Genero WHERE nombre = 'Electrónica');

-- ==========================
-- ARTISTAS
-- ==========================
INSERT INTO Artista (nombre_artistico, pais_origen, descripcion, genero_id)
SELECT 'Shakira', 'Colombia', 'Cantante pop y latina famosa mundialmente', 1
    WHERE NOT EXISTS (SELECT 1 FROM Artista WHERE nombre_artistico = 'Shakira');

INSERT INTO Artista (nombre_artistico, pais_origen, descripcion, genero_id)
SELECT 'Queen', 'Reino Unido', 'Banda de rock clásica', 2
    WHERE NOT EXISTS (SELECT 1 FROM Artista WHERE nombre_artistico = 'Queen');

INSERT INTO Artista (nombre_artistico, pais_origen, descripcion, genero_id)
SELECT 'Kendrick Lamar', 'EEUU', 'Rapero y compositor destacado', 3
    WHERE NOT EXISTS (SELECT 1 FROM Artista WHERE nombre_artistico = 'Kendrick Lamar');

INSERT INTO Artista (nombre_artistico, pais_origen, descripcion, genero_id)
SELECT 'Daft Punk', 'Francia', 'Dúo de música electrónica', 4
    WHERE NOT EXISTS (SELECT 1 FROM Artista WHERE nombre_artistico = 'Daft Punk');

-- ==========================
-- USUARIOS
-- ==========================
INSERT INTO Usuario (nombre, email, contrasena, fecha_nacimiento, localidad_id)
SELECT 'Alvaro Perez', 'alvaro@example.com', 'contrasena123', '1995-05-12', 1
    WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'alvaro@example.com');

INSERT INTO Usuario (nombre, email, contrasena, fecha_nacimiento, localidad_id)
SELECT 'Lucia Gomez', 'lucia@example.com', 'password456', '1998-11-23', 2
    WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'lucia@example.com');

INSERT INTO Usuario (nombre, email, contrasena, fecha_nacimiento, localidad_id)
SELECT 'Carlos Ruiz', 'carlos@example.com', 'pass789', '1990-02-15', 3
    WHERE NOT EXISTS (SELECT 1 FROM Usuario WHERE email = 'carlos@example.com');

-- ==========================
-- USUARIO GENEROS FAVORITOS
-- ==========================
INSERT INTO usuario_generos_favoritos (usuario_id, genero_id)
SELECT 1, 1
    WHERE NOT EXISTS (
    SELECT 1 FROM usuario_generos_favoritos
    WHERE usuario_id = 1 AND genero_id = 1
);

INSERT INTO usuario_generos_favoritos (usuario_id, genero_id)
SELECT 1, 2
    WHERE NOT EXISTS (
    SELECT 1 FROM usuario_generos_favoritos
    WHERE usuario_id = 1 AND genero_id = 2
);

INSERT INTO usuario_generos_favoritos (usuario_id, genero_id)
SELECT 2, 4
    WHERE NOT EXISTS (
    SELECT 1 FROM usuario_generos_favoritos
    WHERE usuario_id = 2 AND genero_id = 4
);

INSERT INTO usuario_generos_favoritos (usuario_id, genero_id)
SELECT 3, 3
    WHERE NOT EXISTS (
    SELECT 1 FROM usuario_generos_favoritos
    WHERE usuario_id = 3 AND genero_id = 3
);

INSERT INTO usuario_generos_favoritos (usuario_id, genero_id)
SELECT 3, 2
    WHERE NOT EXISTS (
    SELECT 1 FROM usuario_generos_favoritos
    WHERE usuario_id = 3 AND genero_id = 2
);

-- ==========================
-- CANCIONES
-- ==========================
INSERT INTO Cancion (titulo, artista_id, genero_id, fecha_lanzamiento, duracion, album)
SELECT 'Hips Don’t Lie', 1, 1, '2006-02-28', 218, 'Oral Fixation Vol. 2'
    WHERE NOT EXISTS (
    SELECT 1 FROM Cancion WHERE titulo = 'Hips Don’t Lie'
);

INSERT INTO Cancion (titulo, artista_id, genero_id, fecha_lanzamiento, duracion, album)
SELECT 'Bohemian Rhapsody', 2, 2, '1975-10-31', 354, 'A Night at the Opera'
    WHERE NOT EXISTS (
    SELECT 1 FROM Cancion WHERE titulo = 'Bohemian Rhapsody'
);

INSERT INTO Cancion (titulo, artista_id, genero_id, fecha_lanzamiento, duracion, album)
SELECT 'Alright', 3, 3, '2015-03-15', 210, 'To Pimp a Butterfly'
    WHERE NOT EXISTS (
    SELECT 1 FROM Cancion WHERE titulo = 'Alright'
);

INSERT INTO Cancion (titulo, artista_id, genero_id, fecha_lanzamiento, duracion, album)
SELECT 'One More Time', 4, 4, '2000-11-30', 320, 'Discovery'
    WHERE NOT EXISTS (
    SELECT 1 FROM Cancion WHERE titulo = 'One More Time'
);
