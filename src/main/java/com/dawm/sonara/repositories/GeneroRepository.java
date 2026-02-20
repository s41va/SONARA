package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    @Override
    Optional<Genero> findById(Long id);
}
