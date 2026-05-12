package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.InformacionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<InformacionPago, Long> {

    // Útil para buscar el registro cuando Stripe nos confirme que el pago fue ok
    Optional<InformacionPago> findByIdTransaccionStripe(String idTransaccionStripe);
}