-- Estructura Completa para MariaDB / SONARA
-- -----------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- 1. TABLA GENERO
CREATE TABLE IF NOT EXISTS genero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(400)
) ENGINE=InnoDB;

-- 2. TABLA LOCALIDAD
CREATE TABLE IF NOT EXISTS localidad (
    localidad_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pais VARCHAR(100) NOT NULL,
    nombre_ciudad VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(12) NOT NULL,
    UNIQUE KEY unique_ciudad (pais, nombre_ciudad)
) ENGINE=InnoDB;

-- 3. TABLA ARTISTA (Referencial a TheAudioDB)
CREATE TABLE IF NOT EXISTS artista (
    id INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero_id INT,
    votos_ranking INT DEFAULT 0,
    ultima_sincronizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_artista_genero FOREIGN KEY (genero_id) REFERENCES genero(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 4. TABLA CANCION (Referencial a TheAudioDB)
CREATE TABLE IF NOT EXISTS cancion (
    cancion_id INT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    artista_id INT,
    reproducciones_locales INT DEFAULT 0,
    CONSTRAINT fk_cancion_artista FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. TABLA CONCIERTOS (Nueva Entidad)
CREATE TABLE IF NOT EXISTS conciertos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    artista_id VARCHAR(50), -- Guardamos el ID de la API (idArtist)
    artista_nombre VARCHAR(100),
    localidad_id BIGINT,
    fecha_hora DATETIME,
    local VARCHAR(150),
    descripcion TEXT,
    CONSTRAINT fk_concierto_localidad FOREIGN KEY (localidad_id) REFERENCES localidad(localidad_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 6. TABLA USUARIO
CREATE TABLE IF NOT EXISTS usuario (
    usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    contrasena_hash VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    localidad_id BIGINT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_localidad FOREIGN KEY (localidad_id) REFERENCES localidad(localidad_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 7. TABLA ROLES
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    display_name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB;

-- 8. TABLA INTERMEDIA USUARIO_ROLES (N:M)
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_rol FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 9. PERFILES DE USUARIO (1:1)
CREATE TABLE IF NOT EXISTS usuario_profiles (
    usuario_id BIGINT PRIMARY KEY,
    first_name VARCHAR(60),
    last_name VARCHAR(80),
    phone_number VARCHAR(30),
    profile_image VARCHAR(255),
    bio VARCHAR(500),
    locale VARCHAR(10) DEFAULT 'es',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_up_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 10. GENEROS FAVORITOS (N:M)
CREATE TABLE IF NOT EXISTS usuario_generos_favoritos (
    usuario_id BIGINT NOT NULL,
    genero_id INT NOT NULL,
    PRIMARY KEY (usuario_id, genero_id),
    CONSTRAINT fk_ugf_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    CONSTRAINT fk_ugf_genero FOREIGN KEY (genero_id) REFERENCES genero(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 11. TOKENS PARA PASSWORD RESET
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,
    used_at DATETIME NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    request_ip VARCHAR(45),
    CONSTRAINT fk_prt_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;