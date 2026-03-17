package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Set<Roles> findAllByIdIn(Set<Long> ids);
    Optional<Roles> findByName(String name);
}