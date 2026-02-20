package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.InformacionPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InformacionPagoRepository extends JpaRepository<InformacionPago, Long> {
    @Override
    Optional<InformacionPago> findById(Long id);
}
