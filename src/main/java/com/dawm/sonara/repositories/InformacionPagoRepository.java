package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.InformacionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformacionPagoRepository extends JpaRepository<InformacionPago, Long> {
    @Override
    Optional<InformacionPago> findById(Long id);
}
