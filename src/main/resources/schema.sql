SET FOREIGN_KEY_CHECKS = 0;

-- ==========================
-- GENEROS
-- ==========================
CREATE TABLE IF NOT EXISTS genero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ==========================
-- LOCALIDADES
-- ==========================
CREATE TABLE IF NOT EXISTS localidad (
    localidad_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pais VARCHAR(100) NOT NULL,
    nombre_ciudad VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(12) NOT NULL,
    UNIQUE KEY unique_ciudad (pais, nombre_ciudad)
) ENGINE=InnoDB;

-- ==========================
-- ARTISTAS (Usando IDs reales de TheAudioDB como ejemplo)
-- ==========================
CREATE TABLE IF NOT EXISTS artista (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(50),
    foto VARCHAR(255),
    votos_ranking INT DEFAULT 0,
    ultima_sincronizacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ==========================
-- USUARIOS (Actualizado para OAuth2)
-- ==========================
CREATE TABLE IF NOT EXISTS usuario (
    usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    -- Quitamos el NOT NULL: los usuarios de Google no tienen contraseña en nuestra DB
    contrasena_hash VARCHAR(100),
    -- Quitamos el NOT NULL: Google no siempre devuelve la fecha de nacimiento
    fecha_nacimiento DATE,
    -- Quitamos el NOT NULL: Al registrarse con Google, aún no sabemos su localidad
    localidad_id BIGINT,
    -- Añadimos el interruptor de cuenta activa
    activada BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_localidad FOREIGN KEY (localidad_id) REFERENCES localidad(localidad_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================
-- ROLES
-- ==========================
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    display_name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB;

-- ==========================
-- USUARIO_ROLES
-- ==========================
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_rol FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================
-- GENEROS FAVORITOS
-- ==========================
CREATE TABLE IF NOT EXISTS usuario_generos_favoritos (
    usuario_id BIGINT NOT NULL,
    genero_id INT NOT NULL,
    PRIMARY KEY (usuario_id, genero_id),
    CONSTRAINT fk_ugf_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    CONSTRAINT fk_ugf_genero FOREIGN KEY (genero_id) REFERENCES genero(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================
-- CONCIERTOS (20 Registros)
-- ==========================
CREATE TABLE IF NOT EXISTS conciertos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    artista_id VARCHAR(50),
    artista_nombre VARCHAR(100),
    localidad_id BIGINT,
    fecha_hora DATETIME,
    local VARCHAR(150),
    descripcion TEXT,
    precio DECIMAL(10,2) DEFAULT 45.00,
    stock INT DEFAULT 100,
    CONSTRAINT fk_concierto_localidad FOREIGN KEY (localidad_id) REFERENCES localidad(localidad_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================
-- ARTISTAS FAVORITOS DE USUARIOS
-- ==========================
CREATE TABLE IF NOT EXISTS usuario_artistas_favoritos (
    usuario_id BIGINT NOT NULL,
    artista_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (usuario_id, artista_id),
    CONSTRAINT fk_uaf_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    CONSTRAINT fk_uaf_artista FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE
    ) ENGINE=InnoDB;

-- ==========================
-- CANCIONES FAVORITAS DE USUARIOS (Sin de momento)
-- ==========================
CREATE TABLE IF NOT EXISTS usuario_canciones_favoritas_ids (
    usuario_id BIGINT NOT NULL,
    cancion_externa_id VARCHAR(255), -- ID que viene de TheAudioDB
    CONSTRAINT fk_ucf_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================
-- CANCIONES (Referencial a TheAudioDB)
-- ==========================
CREATE TABLE IF NOT EXISTS cancion (
    cancion_id INT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    artista_id VARCHAR(50),
    reproducciones_locales INT DEFAULT 0,
    CONSTRAINT fk_cancion_artista FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================
-- PERFILES DE USUARIO (Relación 1:1 con Usuario)
-- ==========================
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

-- ==========================
-- VOTOS
-- ==========================
CREATE TABLE IF NOT EXISTS votos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    artista_id VARCHAR(50) NOT NULL,
    localidad VARCHAR(100) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_voto_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    CONSTRAINT fk_voto_artista FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE
    ) ENGINE=InnoDB;

-- ==========================
-- SOLICITUDES DE NUEVOS ARTISTAS
-- ==========================
CREATE TABLE IF NOT EXISTS solicitudes_artistas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_artista VARCHAR(100) NOT NULL,
    genero_sugerido VARCHAR(50),
    descripcion TEXT, -- Aquí el usuario explica quién es el artista
    foto_url VARCHAR(255),
    usuario_id BIGINT,
    estado VARCHAR(20) DEFAULT 'PENDIENTE', -- PENDIENTE, APROBADA, RECHAZADA
    fecha_solicitud DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_solicitud_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================
-- TOKENS PARA PASSWORD RESET
-- ==========================
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,
    used_at DATETIME NULL,
    created_at DATETIME NOT NULL,
    request_ip VARCHAR(45) NULL,
    user_agent VARCHAR(255) NULL,

    CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    INDEX idx_prt_user_id (user_id),
    INDEX idx_prt_token_hash (token_hash),
    INDEX idx_prt_expires_at (expires_at)
    ) ENGINE=InnoDB;
SET FOREIGN_KEY_CHECKS = 1;


-- ==========================
-- INFORMACION PAGO (Stripe)
-- ==========================
CREATE TABLE IF NOT EXISTS informacion_pago (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    concierto_id BIGINT NOT NULL,
    id_transaccion_stripe VARCHAR(255),
    monto_pago DECIMAL(10,2),
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado_pago VARCHAR(50), -- PENDING, SUCCESS, FAILED
    CONSTRAINT fk_pago_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    CONSTRAINT fk_pago_concierto FOREIGN KEY (concierto_id) REFERENCES conciertos(id) ON DELETE CASCADE
) ENGINE=InnoDB;