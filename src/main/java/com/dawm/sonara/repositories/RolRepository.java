package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Set<Rol> findAllByIdIn(Set<Long> ids);

    Optional<Rol> findByName(String name);
}
