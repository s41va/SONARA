-- ==========================
-- GENEROS
-- ==========================
INSERT IGNORE INTO genero (id, nombre, descripcion) VALUES
(1, 'Rap', 'Género emergente de EEUU alrededor de los años 70, por la cultura afroamericana'),
(2, 'Pop', 'El género Popular procedente del rock and roll'),
(3, 'Reggaeton', 'Caribeño surgido alrededor de los principios de los 90 en Puerto Rico y Panamá'),
(4, 'Rock', 'Género musical surgido en los años 40 y 50, caracterizado por un ritmo enérgico.'),
(5, 'Electrónica', 'Música creada con instrumentos y tecnología electrónica.'),
(6, 'R&B', 'Rhythm and Blues. Género afroamericano que combina jazz, góspel y blues.');

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
INSERT IGNORE INTO artista (id, nombre, genero, votos_ranking) VALUES
(111239, 'Eminem', 'Rap', 150),
(134722, 'Bad Bunny', 'Reggaeton', 230),
(112045, 'Taylor Swift', 'Pop', 190),
(142144, 'Rosalía', 'Pop', 120),
(111516, 'Coldplay', 'Rock', 85),
(114381, 'The Weeknd', 'R&B', 110),
(111304, 'Arctic Monkeys', 'Indie Rock', 95);

-- ==========================
-- USUARIOS
-- ==========================
INSERT IGNORE INTO usuario (usuario_id, nombre, email, contrasena_hash, fecha_nacimiento, fecha_registro, localidad_id) VALUES
(1, 'Ana García', 'ana.garcia@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1990-04-12', '2025-12-15 10:30:00', 1),
(2, 'Carlos López', 'carlos.lopez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1985-07-23', '2025-12-14 09:00:00', 5),
(3, 'María Pérez', 'maria.perez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1992-11-05', '2025-12-15 11:45:00', 3),
(4, 'Jorge Martínez', 'jorge.martinez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1988-01-30', '2025-12-13 14:20:00', 1),
(5, 'Lucía Fernández', 'lucia.fernandez@example.com', '$2a$12$23zzGOK04oE9CyVMx/viPucEO8relsSV.8Av205WULOoI18s/ciRG', '1995-06-18', '2025-12-12 08:15:00', 20);

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
-- CONCIERTOS (20 Registros)
-- ==========================
INSERT IGNORE INTO conciertos (artista_id, artista_nombre, localidad_id, fecha_hora, local, descripcion) VALUES
('111516', 'Coldplay', 8, '2026-06-15 21:00:00', 'Estadi Olímpic', 'Gira Music of the Spheres'),
('142144', 'Rosalía', 28, '2026-07-20 22:00:00', 'WiZink Center', 'Presentación nuevo álbum'),
('111239', 'Eminem', 8, '2026-08-10 20:30:00', 'Palau Sant Jordi', 'The Eminem Show Revival Tour'),
('134722', 'Bad Bunny', 41, '2026-05-12 21:30:00', 'Estadio La Cartuja', 'Most Wanted Tour España'),
('112045', 'Taylor Swift', 28, '2026-09-05 20:00:00', 'Estadio Santiago Bernabéu', 'The Eras Tour (Extended)'),
('114381', 'The Weeknd', 29, '2026-07-15 22:00:00', 'Estadio La Rosaleda', 'After Hours Til Dawn'),
('111304', 'Arctic Monkeys', 48, '2026-10-12 21:00:00', 'Bizkaia Arena BEC', 'The Car Tour'),
('127051', 'Quevedo', 35, '2026-04-20 20:00:00', 'Estadio Gran Canaria', 'DQE Tour Final'),
('111247', 'Metallica', 28, '2026-07-07 21:00:00', 'Estadio Metropolitano', 'M72 World Tour'),
('111279', 'Iron Maiden', 30, '2026-06-30 19:30:00', 'Estadio Nueva Condomina', 'The Future Past Tour'),
('112035', 'Lady Gaga', 8, '2026-11-20 21:00:00', 'Palau Sant Jordi', 'Chromatica Ball Return'),
('111391', 'Imagine Dragons', 46, '2026-08-25 21:30:00', 'Estadio Ciudad de Valencia', 'Mercury World Tour'),
('111318', 'Muse', 15, '2026-07-12 22:00:00', 'Estadio de Riazor', 'Will of the People Tour'),
('114364', 'Harry Styles', 28, '2026-05-28 20:45:00', 'WiZink Center', 'Love on Tour 2026'),
('135315', 'Karol G', 41, '2026-06-10 21:30:00', 'Estadio Benito Villamarín', 'Mañana será Bonito Tour'),
('111564', 'Dua Lipa', 8, '2026-09-18 21:00:00', 'Palau Sant Jordi', 'Future Nostalgia Deluxe'),
('111259', 'Red Hot Chili Peppers', 41, '2026-07-03 22:00:00', 'Estadio de la Cartuja', 'Unlimited Love Tour'),
('111306', 'The Killers', 31, '2026-07-10 23:00:00', 'Recinto de Festivales', 'Festival San Fermín Especial'),
('111514', 'Radiohead', 28, '2026-10-01 20:00:00', 'WiZink Center', 'A Moon Shaped Pool Anniversary'),
('111394', 'Gorillaz', 8, '2026-06-05 22:30:00', 'Parc del Fòrum', 'Primavera Sound Headliner');

-- ==========================
-- ARTISTAS FAVORITOS DE USUARIOS
-- ==========================
INSERT IGNORE INTO usuario_artistas_favoritos_ids (usuario_id, artista_externo_id) VALUES
(1, '111239'), -- Ana García gusta de Eminem
(1, '111516'), -- Ana García gusta de Coldplay
(2, '134722'), -- Carlos López gusta de Bad Bunny
(3, '112045'), -- María Pérez gusta de Taylor Swift
(5, '114381'); -- Lucía Fernández gusta de The Weeknd

-- ==========================
-- CANCIONES FAVORITAS DE USUARIOS
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

