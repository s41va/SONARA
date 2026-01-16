-- data.sql
INSERT IGNORE  INTO localidad (pais, nombre_ciudad, codigo_postal) VALUES
-- Comunidad de Madrid
('España', 'Madrid', '28001'),
('España', 'Alcalá de Henares', '28801'),
('España', 'Getafe', '28901'),
('España', 'Móstoles', '28931'),

-- Cataluña
('España', 'Barcelona', '08001'),
('España', 'L\'Hospitalet de Llobregat', '08901'),
('España', 'Badalona', '08911'),
('España', 'Terrassa', '08221'),
('España', 'Sabadell', '08201'),
('España', 'Tarragona', '43001'),
('España', 'Lleida', '25001'),
('España', 'Girona', '17001'),

-- Comunidad Valenciana
('España', 'Valencia', '46001'),
('España', 'Alicante', '03001'),
('España', 'Elche', '03201'),
('España', 'Castellón de la Plana', '12001'),

-- Andalucía
('España', 'Sevilla', '41001'),
('España', 'Málaga', '29001'),
('España', 'Córdoba', '14001'),
('España', 'Granada', '18001'),
('España', 'Jaén', '23001'),
('España', 'Almería', '04001'),
('España', 'Huelva', '21001'),
('España', 'Cádiz', '11001'),
('España', 'Jerez de la Frontera', '11401'),

-- País Vasco
('España', 'Bilbao', '48001'),
('España', 'Vitoria-Gasteiz', '01001'),
('España', 'San Sebastián', '20001'),

-- Galicia
('España', 'A Coruña', '15001'),
('España', 'Vigo', '36201'),
('España', 'Santiago de Compostela', '15701'),
('España', 'Lugo', '27001'),
('España', 'Ourense', '32001'),
('España', 'Pontevedra', '36001'),

-- Castilla y León
('España', 'Valladolid', '47001'),
('España', 'Salamanca', '37001'),
('España', 'Burgos', '09001'),
('España', 'León', '24001'),
('España', 'Zamora', '49001'),
('España', 'Ávila', '05001'),
('España', 'Segovia', '40001'),
('España', 'Soria', '42001'),
('España', 'Palencia', '34001'),

-- Castilla-La Mancha
('España', 'Toledo', '45001'),
('España', 'Albacete', '02001'),
('España', 'Ciudad Real', '13001'),
('España', 'Cuenca', '16001'),
('España', 'Guadalajara', '19001'),

-- Aragón
('España', 'Zaragoza', '50001'),
('España', 'Huesca', '22001'),
('España', 'Teruel', '44001'),

-- Murcia
('España', 'Murcia', '30001'),
('España', 'Cartagena', '30201'),

-- Extremadura
('España', 'Badajoz', '06001'),
('España', 'Cáceres', '10001'),

-- Asturias
('España', 'Oviedo', '33001'),
('España', 'Gijón', '33201'),
('España', 'Avilés', '33401'),

-- Cantabria
('España', 'Santander', '39001'),
('España', 'Torrelavega', '39300'),

-- Navarra
('España', 'Pamplona', '31001'),

-- La Rioja
('España', 'Logroño', '26001'),

-- Islas Baleares
('España', 'Palma', '07001'),
('España', 'Manacor', '07500'),

-- Islas Canarias
('España', 'Las Palmas de Gran Canaria', '35001'),
('España', 'Santa Cruz de Tenerife', '38001'),
('España', 'La Laguna', '38201'),

-- Ceuta y Melilla
('España', 'Ceuta', '51001'),
('España', 'Melilla', '52001');


INSERT IGNORE  INTO usuario (nombre, email, contrasena, fecha_nacimiento, fecha_registro, localidad_id) VALUES
('Ana García', 'ana.garcia@example.com', 'pass123', '1990-04-12', '2025-12-15 10:30:00', 1),
('Carlos López', 'carlos.lopez@example.com', 'pass456', '1985-07-23', '2025-12-14 09:00:00', 5),
('María Pérez', 'maria.perez@example.com', 'pass789', '1992-11-05', '2025-12-15 11:45:00', 3),
('Jorge Martínez', 'jorge.martinez@example.com', 'pass321', '1988-01-30', '2025-12-13 14:20:00', 1),
('Lucía Fernández', 'lucia.fernandez@example.com', 'pass654', '1995-06-18', '2025-12-12 08:15:00', 20),
('Miguel Torres', 'miguel.torres@example.com', 'pass987', '1991-09-22', '2025-12-11 16:40:00', 3),
('Sofía Ruiz', 'sofia.ruiz@example.com', 'passabc', '1993-03-10', '2025-12-10 13:55:00', 10),
('Diego Sánchez', 'diego.sanchez@example.com', 'passdef', '1987-12-02', '2025-12-09 12:30:00', 21),
('Valeria Gómez', 'valeria.gomez@example.com', 'passghi', '1994-08-25', '2025-12-08 15:10:00', 33),
('Andrés Castillo', 'andres.castillo@example.com', 'passjkl', '1989-05-14', '2025-12-07 09:45:00', 1);

INSERT IGNORE  INTO usuario_generos_favoritos (usuario_id, genero_favorito) VALUES
(1, 'Rock'),
(2, 'Jazz'),
(4, 'Pop'),
(5, 'Trap'),
(6, 'R&B'),
(7, 'Breakbeat'),
(8, 'Reggaeton');