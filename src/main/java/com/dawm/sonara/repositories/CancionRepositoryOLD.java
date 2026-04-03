package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.CancionOLD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancionRepositoryOLD extends JpaRepository<CancionOLD, Long> {

    @Override
    Optional<CancionOLD> findById(Long id);
}
