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
    artista_id VARCHAR(50), -- Guardamos el ID de la API (idArtist)
    artista_nombre VARCHAR(100),
    localidad_id BIGINT,
    fecha_hora DATETIME,
    local VARCHAR(150),
    descripcion TEXT,
    CONSTRAINT fk_concierto_localidad FOREIGN KEY (localidad_id) REFERENCES localidad(localidad_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================
-- ARTISTAS FAVORITOS DE USUARIOS
-- ==========================
CREATE TABLE IF NOT EXISTS usuario_artistas_favoritos_ids (
    usuario_id BIGINT NOT NULL,
    artista_externo_id VARCHAR(255), -- ID que viene de TheAudioDB
    CONSTRAINT fk_uaf_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================
-- CANCIONES FAVORITAS DE USUARIOS
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
CREATE TABLE votos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    artista_id VARCHAR(50) NOT NULL, -- Referencia a TU tabla artista
    localidad VARCHAR(100) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_voto_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_voto_artista FOREIGN KEY (artista_id) REFERENCES artista(id)
);

-- ==========================
-- TOKENS PARA PASSWORD RESET
-- ==========================
CREATE TABLE IF NOT EXISTS password_reset_tokens (
 -- Clave primaria autoincremental. Identificador interno del registro de token.
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 -- Usuario al que pertenece el token.
 -- Relación N:1 (un usuario puede generar varios tokens a lo largo del tiempo).
 user_id BIGINT NOT NULL,
 -- Hash del token (NO el token en claro).
 -- Recomendación profesional: guardar SHA-256 en hexadecimal (64 chars) o similar.
 -- Así, aunque alguien lea la BD, no puede usar directamente el token para resetear.
 token_hash VARCHAR(64) NOT NULL,
 -- Momento exacto de caducidad del token (TTL).
 -- Buenas prácticas: 30–60 minutos. Tokens con caducidad corta reducen impacto ante robo del enlace.
 expires_at DATETIME NOT NULL,
 -- Momento en el que el token se consume (one-time token).
 -- Si used_at != NULL => token ya usado, cualquier intento posterior debe fallar.
 used_at DATETIME NULL,
 -- Momento de creación del token.
 -- Útil para auditoría y para detectar patrones anómalos (spam de solicitudes).
 created_at DATETIME NOT NULL,
 -- IP desde la que se solicitó el reset.
 -- Útil para auditoría y para correlación en incidentes (p.ej. detectar abuso por IP).
 -- VARCHAR(45) cubre IPv4 e IPv6.
 request_ip VARCHAR(45) NULL,
 -- User-Agent del cliente que solicitó el reset (navegador/dispositivo).
 -- Útil para auditoría y detección de bots, pero NO es un dato fiable para “seguridad dura”.
 user_agent VARCHAR(255) NULL,
 -- Clave foránea: garantiza integridad referencial (no puede haber tokens sin usuario existente).
 CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES users(id),
 -- Índice por user_id:
 -- Acelera operaciones típicas como invalidar tokens activos de un usuario o listar tokens por usuario.
 INDEX idx_prt_user_id (user_id),
 -- Índice por token_hash:
 -- Acelera la validación del token cuando el usuario llega con ?token=... (se busca por hash).
 INDEX idx_prt_token_hash (token_hash),
 -- Índice por expires_at:
 -- Acelera tareas de limpieza (borrar tokens caducados) y consultas por expiración.
 INDEX idx_prt_expires_at (expires_at)
);


SET FOREIGN_KEY_CHECKS = 1;

