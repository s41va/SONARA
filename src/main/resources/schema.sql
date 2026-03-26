--SET FOREIGN_KEY_CHECKS = 0;
-- TABLA GENERO (Lado ONE de la relación)
CREATE TABLE IF NOT EXISTS genero (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(400)
);

-- TABLA ARTISTA (Lado MANY de la relación)
CREATE TABLE IF NOT EXISTS artista (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    pais_origen VARCHAR(100),
    descripcion VARCHAR(400),
    -- Añadimos la columna de clave foránea
    genero_id INT NOT NULL,
    -- Definición de la clave foránea
    CONSTRAINT fk_artista_genero
        FOREIGN KEY (genero_id)
        REFERENCES genero(id)
        ON DELETE RESTRICT 
        ON UPDATE CASCADE
);

-- TABLA CANCION
CREATE TABLE IF NOT EXISTS cancion (
    cancion_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200),
    artista_id INT,
    genero_id INT,
    fecha_lanzamiento DATE,
    duracion INT,
    album VARCHAR(200),
    CONSTRAINT fk_cancion_artista
        FOREIGN KEY (artista_id) REFERENCES artista(id),
    CONSTRAINT fk_cancion_genero
        FOREIGN KEY (genero_id) REFERENCES genero(id)
);


-- schema.sql
CREATE TABLE IF NOT EXISTS localidad (
    localidad_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pais VARCHAR(100) NOT NULL,
    nombre_ciudad VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(12) NOT NULL, -- Tamaño 12 ya que hay paises que tienen codigos mas largos
    UNIQUE KEY unique_ciudad (pais, nombre_ciudad)
);

CREATE TABLE IF NOT EXISTS usuario (
    usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    contrasena_hash VARCHAR(100),
    fecha_nacimiento DATE,
    localidad_id BIGINT,
    fecha_registro DATETIME,
    CONSTRAINT fk_usuario_localidad FOREIGN KEY (localidad_id) REFERENCES localidad(localidad_id)
);

CREATE TABLE IF NOT EXISTS usuario_generos_favoritos (
    usuario_id BIGINT NOT NULL,
    genero_favorito INT,
    PRIMARY KEY (usuario_id, genero_favorito),
    CONSTRAINT fk_ugf_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_ugf_genero FOREIGN KEY (genero_favorito) REFERENCES genero(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Nombre técnico que usaremos en Spring Security: ROLE_ADMIN, ROLE_USER...
                                     name VARCHAR(50) NOT NULL UNIQUE,
    -- Nombre legible para la interfaz
    display_name VARCHAR(100) NOT NULL,
    -- Descripción opcional del rol
    description VARCHAR(255) NULL
    );



-- Tabla intermedia N:M entre usuarios y roles
CREATE TABLE IF NOT EXISTS usuario_roles (
                                             usuario_id BIGINT NOT NULL,
                                             rol_id BIGINT NOT NULL,
                                             CONSTRAINT pk_usuario_roles PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_usuario_roles_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_roles_rol
    FOREIGN KEY (rol_id) REFERENCES roles(id)
    ON DELETE CASCADE ON UPDATE CASCADE
    );


  CREATE TABLE IF NOT EXISTS usuario_profiles (
      usuario_id BIGINT NOT NULL,
      first_name VARCHAR(60) NOT NULL,
      last_name VARCHAR(80) NOT NULL,
      phone_number VARCHAR(30) NULL,
      profile_image VARCHAR(255) NULL,
      bio VARCHAR(500) NULL,
      locale VARCHAR(10) NULL,
      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
          ON UPDATE CURRENT_TIMESTAMP,
      CONSTRAINT pk_usuario_profiles PRIMARY KEY (usuario_id),
      CONSTRAINT fk_usuario_profiles_usuario
          FOREIGN KEY (usuario_id)
          REFERENCES usuario(usuario_id)
          ON DELETE CASCADE
          ON UPDATE CASCADE
  ) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,
    used_at DATETIME NULL,
    created_at DATETIME NOT NULL,
    request_ip VARCHAR(45) NULL,
    user_agent VARCHAR(255) NULL,
    CONSTRAINT fk_prt_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id),
    INDEX idx_prt_usuario_id (usuario_id),
    INDEX idx_prt_token_hash (token_hash),
    INDEX idx_prt_expires_at (expires_at)
) ENGINE=InnoDB;
