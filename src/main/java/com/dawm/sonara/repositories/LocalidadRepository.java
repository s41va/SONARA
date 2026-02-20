package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalidadRepository extends JpaRepository<Localidad, Long> {
    @Override
    Optional<Localidad> findById(Long id);
}
