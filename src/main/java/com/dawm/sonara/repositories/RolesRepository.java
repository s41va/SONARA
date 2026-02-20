package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Set<Roles> findAllByIdIn(Set<Long> ids);
    Optional<Roles> findByName(String name);
}