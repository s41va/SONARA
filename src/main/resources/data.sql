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




-- Insertar datos en GENERO (sin cambios)

INSERT IGNORE INTO genero (id, nombre, descripcion) VALUES
(1,"Rap","Genero emergente de EEUU alrededor de los años 70, por la cultura afroamericana"),
(2, "Pop", "El genero Popular procedente del rock and roll"),
(3, "Reggaeton", "Caribeño surgio alrededor de los principios de los 90 en Puerto Rico y Panama"),
(4, 'Rock', 'Género musical surgido en los años 40 y 50, caracterizado por un ritmo enérgico y el uso de guitarras eléctricas.'),
(5, 'Electrónica', 'Música creada con instrumentos y tecnología electrónica, ideal para clubes y festivales.'),
(6, 'R&B', 'Rhythm and Blues. Género afroamericano que combina jazz, góspel y blues.');

-- Insertar datos en ARTISTA (Con la clave foránea genero_id)
-- Se asigna un UNICO genero_id a cada artista.
INSERT IGNORE INTO artista (nombre, pais_origen, descripcion, genero_id) VALUES
-- ID de Género elegido: 1=Rap, 2=Pop, 3=Reggaeton, 4=Rock, 5=Electrónica, 6=R&B
('Eminem', 'Estados Unidos', 'Famoso rapero blanco, reconocido por su lírica y velocidad.', 1),        -- Rap
('Bad Bunny','Puerto Rico', 'Icono global del trap y reggaetón, conocido por su estilo único.', 3),     -- Reggaeton (principal)
('Taylor Swift','Estados Unidos', 'Cantautora pop y country, una de las artistas más vendidas.', 2),   -- Pop
('Rosalía','España', 'Fusiona el flamenco con el pop urbano y el trap.', 2),                             -- Pop (principal)
('Drake','Canadá', 'Rapero, cantante y actor, dominante en las listas de streaming.', 1),               -- Rap
('J Balvin','Colombia', 'Pionero en llevar el reggaetón a la escena musical global.', 3),               -- Reggaeton
('Madonna','Estados Unidos', 'Reina del Pop, famosa por su reinvención constante.', 2),                 -- Pop
('Quevedo','España', 'Artista emergente de música urbana y reggaetón con gran éxito internacional.', 3), -- Reggaeton (principal)
('Kendrick Lamar', 'Estados Unidos', 'Considerado uno de los raperos más influyentes de su generación.', 1), -- Rap
('Rihanna','Barbados', 'Cantante de pop, R&B y dancehall, empresaria.', 2),                              -- Pop (principal)
('The Weeknd','Canadá', 'Famoso por fusionar R&B, pop y trap con letras oscuras.', 6),                   -- R&B (principal)
('Coldplay','Reino Unido', 'Banda de rock alternativo conocida por sus himnos de estadio.', 4),          -- Rock (principal)
('Calvin Harris','Reino Unido', 'DJ, productor y músico reconocido por la música electrónica de baile.', 5), -- Electrónica
('Aretha Franklin','Estados Unidos', 'Legendaria cantante y pianista, conocida como la "Reina del Soul".', 6), -- R&B
('Arctic Monkeys','Reino Unido', 'Banda de rock indie y post-punk, muy influyente en los 2000s.', 4);   -- Rock


-- INSERT IGNORE INTO usuario (nombre, email, contrasena_hash, fecha_nacimiento, fecha_registro) VALUES
-- ('Ana García', 'ana.garcia@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1990-04-12', '2025-12-15 10:30:00'),
-- ('Carlos López', 'carlos.lopez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1985-07-23', '2025-12-14 09:00:00'),
-- ('María Pérez', 'maria.perez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1992-11-05', '2025-12-15 11:45:00'),
-- ('Jorge Martínez', 'jorge.martinez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1988-01-30', '2025-12-13 14:20:00'),
-- ('Lucía Fernández', 'lucia.fernandez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1995-06-18', '2025-12-12 08:15:00'),
-- ('Miguel Torres', 'miguel.torres@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1991-09-22', '2025-12-11 16:40:00'),
-- ('Sofía Ruiz', 'sofia.ruiz@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1993-03-10', '2025-12-10 13:55:00'),
-- ('Diego Sánchez', 'diego.sanchez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1987-12-02', '2025-12-09 12:30:00'),
-- ('Valeria Gómez', 'valeria.gomez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1994-08-25', '2025-12-08 15:10:00'),
-- ('Andrés Castillo', 'andres.castillo@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1989-05-14', '2025-12-07 09:45:00');


INSERT IGNORE INTO roles (id, name, display_name, description) VALUES
(1, 'ROLE_ADMIN', 'Administrador', 'Acceso total a todas las funcionalidades del sistema'),
(2, 'ROLE_USER', 'Usuario', 'Usuario estándar con acceso limitado'),
(3, 'ROLE_MANAGER', 'Gestor', 'Usuario gestor con permisos de gestión de datos');




INSERT IGNORE INTO usuario_roles (usuario_id, rol_id) VALUES
-- Usuario 1: Ana García → admin completo
(1, 1),  -- ROLE_ADMIN
(1, 2),  -- ROLE_USER

-- Usuario 2: Carlos López → usuario estándar
(2, 2),  -- ROLE_USER

-- Usuario 3: María Pérez → manager + usuario
(3, 3),  -- ROLE_MANAGER
(3, 2); -- ROLE_USER



--
-- INSERT IGNORE INTO usuario_roles (usuario_id, rol_id) VALUES
-- -- Usuario 1: Ana García → admin completo
-- (1, 1),  -- ROLE_ADMIN
-- (1, 2),  -- ROLE_USER
--
-- -- Usuario 2: Carlos López → usuario estándar
-- (2, 2),  -- ROLE_USER
--
-- -- Usuario 3: María Pérez → manager + usuario
-- (3, 3),  -- ROLE_MANAGER
-- (3, 2),  -- ROLE_USER
--
-- -- Usuario 4: Jorge Martínez → admin + usuario
-- (4, 1),  -- ROLE_ADMIN
-- (4, 2),  -- ROLE_USER
--
-- -- Usuario 5: Lucía Fernández → usuario
-- (5, 2),  -- ROLE_USER
--
-- -- Usuario 6: Miguel Torres → usuario
-- (6, 2),  -- ROLE_USER
--
-- -- Usuario 7: Sofía Ruiz → usuario
-- (7, 2),  -- ROLE_USER
--
-- -- Usuario 8: Diego Sánchez → usuario
-- (8, 2),  -- ROLE_USER
--
-- -- Usuario 9: Valeria Gómez → usuario
-- (9, 2),  -- ROLE_USER
--
-- -- Usuario 10: Andrés Castillo → usuario
-- (10, 2); -- ROLE_USER
