-- ==========================
-- GENEROS
-- ==========================
INSERT IGNORE INTO genero (id, nombre) VALUES
(1, 'Rap'),
(2, 'Pop'),
(3, 'Reggaeton'),
(4, 'Rock'),
(5, 'Electrónica'),
(6, 'R&B'),
(7, 'Jazz'),
(8, 'Blues'),
(9, 'Country'),
(10, 'Heavy Metal'),
(11, 'Punk'),
(12, 'Soul'),
(13, 'Folk'),
(14, 'Classical'),
(15, 'Disco'),
(16, 'Latin'),
(17, 'Reggae'),
(18, 'Funk'),
(19, 'House'),
(20, 'Techno'),
(21, 'Trap'),
(22, 'Dembow');

-- ==========================
-- LOCALIDADES
-- ==========================
INSERT IGNORE INTO localidad (pais, nombre_ciudad, codigo_postal) VALUES
('España', 'Vitoria-Gasteiz', '01'), ('España', 'Albacete', '02'), ('España', 'Alicante', '03'),
('España', 'Almería', '04'), ('España', 'Ávila', '05'), ('España', 'Badajoz', '06'),
('España', 'Palma', '07'), ('España', 'Barcelona', '08'), ('España', 'Burgos', '09'),
('España', 'Cáceres', '10'), ('España', 'Cádiz', '11'), ('España', 'Castellón de la Plana', '12'),
('España', 'Ciudad Real', '13'), ('España', 'Córdoba', '14'), ('España', 'A Coruña', '15'),
('España', 'Cuenca', '16'), ('España', 'Girona', '17'), ('España', 'Granada', '18'),
('España', 'Guadalajara', '19'), ('España', 'San Sebastián', '20'), ('España', 'Huelva', '21'),
('España', 'Huesca', '22'), ('España', 'Jaén', '23'), ('España', 'León', '24'),
('España', 'Lleida', '25'), ('España', 'Logroño', '26'), ('España', 'Lugo', '27'),
('España', 'Madrid', '28'), ('España', 'Málaga', '29'), ('España', 'Murcia', '30'),
('España', 'Pamplona', '31'), ('España', 'Ourense', '32'), ('España', 'Oviedo', '33'),
('España', 'Palencia', '34'), ('España', 'Las Palmas de Gran Canaria', '35'),
('España', 'Pontevedra', '36'), ('España', 'Salamanca', '37'), ('España', 'Santa Cruz de Tenerife', '38'),
('España', 'Santander', '39'), ('España', 'Segovia', '40'), ('España', 'Sevilla', '41'),
('España', 'Soria', '42'), ('España', 'Tarragona', '43'), ('España', 'Teruel', '44'),
('España', 'Toledo', '45'), ('España', 'Valencia', '46'), ('España', 'Valladolid', '47'),
('España', 'Bilbao', '48'), ('España', 'Zamora', '49'), ('España', 'Zaragoza', '50'),
('España', 'Ceuta', '51'), ('España', 'Melilla', '52');

-- ==========================
-- ARTISTAS (Usando IDs reales de TheAudioDB)
-- ==========================
INSERT IGNORE INTO artista (id, nombre, genero, foto, votos_ranking) VALUES
('111239', 'Eminem', 'Rap', NULL, 150),
('134722', 'Bad Bunny', 'Reggaeton', NULL, 230),
('112045', 'Taylor Swift', 'Pop', NULL, 190),
('142144', 'Rosalía', 'Pop', NULL, 120),
('111516', 'Coldplay', 'Rock', NULL, 85),
('114381', 'The Weeknd', 'R&B', NULL, 110),
('111304', 'Arctic Monkeys', 'Indie Rock', NULL, 95);

-- ==========================
-- USUARIOS
-- ==========================
INSERT IGNORE INTO usuario (usuario_id, nombre, email, contrasena_hash, fecha_nacimiento, fecha_registro, localidad_id, activada) VALUES
(1, 'Ana García', 'ana.garcia@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1990-04-12', '2025-12-15 10:30:00', 1, TRUE),
(2, 'Carlos López', 'carlos.lopez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1985-07-23', '2025-12-14 09:00:00', 5, TRUE),
(3, 'María Pérez', 'maria.perez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1992-11-05', '2025-12-15 11:45:00', 3, TRUE),
(4, 'Jorge Martínez', 'jorge.martinez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1988-01-30', '2025-12-13 14:20:00', 1, TRUE),
(5, 'Lucía Fernández', 'lucia.fernandez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1995-06-18', '2025-12-12 08:15:00', 20, TRUE);

-- ==========================
-- ROLES
-- ==========================
INSERT IGNORE INTO roles (id, name, display_name, description) VALUES
(1, 'ROLE_ADMIN', 'Administrador', 'Acceso total a todas las funcionalidades del sistema'),
(2, 'ROLE_USER', 'Usuario', 'Usuario estándar con acceso limitado'),
(3, 'ROLE_MANAGER', 'Gestor', 'Usuario gestor con permisos de gestión de datos');

-- ==========================
-- USUARIO_ROLES
-- ==========================
INSERT IGNORE INTO usuario_roles (usuario_id, rol_id) VALUES
(1, 1), (1, 2), -- Ana: Admin y User
(2, 2),         -- Carlos: User
(3, 3), (3, 2), -- María: Manager y User
(4, 1), (4, 2), -- Jorge: Admin y User
(5, 2);         -- Lucía: User

-- ==========================
-- GENEROS FAVORITOS
-- ==========================
INSERT IGNORE INTO usuario_generos_favoritos (usuario_id, genero_id) VALUES
(1, 4), (2, 1), (3, 2), (4, 2), (5, 1);


-- ==========================
-- CONCIERTOS
-- ==========================
INSERT IGNORE INTO conciertos (id, artista_id, artista_nombre, localidad_id, fecha_hora, local, descripcion, precio, stock) VALUES
(1, '111516', 'Coldplay', 8, '2026-06-15 21:00:00', 'Estadi Olímpic', 'Gira Music of the Spheres', 75.00, 500),
(2, '142144', 'Rosalía', 28, '2026-07-20 22:00:00', 'WiZink Center', 'Presentación nuevo álbum', 60.00, 300),
(3, '111239', 'Eminem', 8, '2026-08-10 20:30:00', 'Palau Sant Jordi', 'The Eminem Show Revival Tour', 90.00, 200),
(4, '134722', 'Bad Bunny', 41, '2026-05-12 21:30:00', 'Estadio La Cartuja', 'Most Wanted Tour España', 85.50, 150),
(5, '112045', 'Taylor Swift', 28, '2026-09-05 20:00:00', 'Estadio Santiago Bernabéu', 'The Eras Tour (Extended)', 120.00, 50);
-- ==========================
-- ARTISTAS FAVORITOS DE USUARIOS
-- ==========================
INSERT IGNORE INTO usuario_artistas_favoritos (usuario_id, artista_id) VALUES
(1, '111239'), -- Ana García -> Eminem
(1, '111516'), -- Ana García -> Coldplay
(2, '134722'), -- Carlos López -> Bad Bunny
(3, '112045'), -- María Pérez -> Taylor Swift
(5, '114381'); -- Lucía Fernández -> The Weeknd

-- ==========================
-- CANCIONES FAVORITAS DE USUARIOS (Sin uso de momento)
-- ==========================
INSERT IGNORE INTO usuario_canciones_favoritas_ids (usuario_id, cancion_externa_id) VALUES
(1, '32724562'), -- IDs de ejemplo de la API
(2, '32890123'),
(4, '32724562');

-- ==========================
-- CANCIONES (Referencial a TheAudioDB)
-- ==========================
INSERT IGNORE INTO cancion (cancion_id, titulo, artista_id, reproducciones_locales) VALUES
(32724562, 'Lose Yourself', 111239, 500),   -- Eminem
(32890123, 'Monaco', 134722, 1200),        -- Bad Bunny
(34125678, 'Anti-Hero', 112045, 850),      -- Taylor Swift
(35678901, 'Despechá', 142144, 980),       -- Rosalía
(31234567, 'Yellow', 111516, 450);         -- Coldplay

-- ==========================
-- PERFILES DE USUARIO (Relación 1:1 con Usuario)
-- ==========================
INSERT IGNORE INTO usuario_profiles (usuario_id, first_name, last_name, phone_number, bio, locale) VALUES
(1, 'Ana', 'García Pelayo', '600111222', 'Amante del rock y el pop clásico.', 'es'),
(2, 'Carlos', 'López Iturbe', '611222333', 'Produciendo beats desde el 85.', 'es'),
(3, 'María', 'Pérez Sodupe', '622333444', 'Melómana empedernida.', 'es'),
(4, 'Jorge', 'Martínez de Quel', '633444555', 'Fan de los festivales de verano.', 'es'),
(5, 'Lucía', 'Fernández Sanz', '644555666', 'Ingeniera de sonido en mis ratos libres.', 'es');

-- ==========================
-- VOTOS
-- ==========================
INSERT INTO votos (usuario_id, artista_id, localidad) VALUES
(1, '111239', 'Sevilla'), -- Eminem en Sevilla
(2, '111239', 'Sevilla'), -- Eminem otro voto en Sevilla
(3, '134722', 'Madrid'),  -- Bad Bunny en Madrid
(4, '112045', 'Madrid'),  -- Taylor Swift en Madrid
(5, '111239', 'Sevilla'); -- Eminem ya lleva 3 en Sevilla