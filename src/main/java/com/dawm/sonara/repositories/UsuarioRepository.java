package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findByEmail(String email);
    /**
     * Localiza un usuario por email (ignorando mayúsculas/minúsculas) y asegura que sus roles
     * queden cargados en la misma consulta.
     *
     * @param email email del usuario (usado como identificador/username del sistema).
     * @return {@link java.util.Optional} con el usuario y sus roles; {@code Optional.empty()} si no existe.
     */
    Optional<Usuario> findByEmailIgnoreCase(String email);
}
