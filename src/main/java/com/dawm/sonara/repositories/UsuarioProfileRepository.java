package com.dawm.sonara.repositories;
import com.dawm.sonara.entities.UsuarioPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioProfileRepository extends JpaRepository<UsuarioPerfil, Long> {

    /**
     * Busca el perfil navegando a través de la entidad Usuario.
     * Spring Data JPA entiende automáticamente que "UsuarioEmail"
     * se refiere al campo "email" dentro de la entidad "usuario".
     */
    Optional<UsuarioPerfil> findByUsuarioEmail(String email);

    /**
     * Alternativa por si usas 'username' en lugar de email en tu token.
     */
    Optional<UsuarioPerfil> findByUsuarioNombre(String username);
}