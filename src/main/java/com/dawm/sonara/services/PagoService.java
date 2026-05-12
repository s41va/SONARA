package com.dawm.sonara.services;

import com.dawm.sonara.dtos.pagos.StripeResponseDTO;
import com.dawm.sonara.entities.Usuario;
import com.stripe.exception.StripeException;

public interface PagoService {

    /**
     * Crea un PaymentIntent en Stripe y registra el pago como PENDING en nuestra DB.
     */
    StripeResponseDTO crearIntentoPago(Long conciertoId, Usuario usuario) throws StripeException;

    /**
     * Confirma el pago, cambia el estado a SUCCESS y resta el stock del concierto.
     */
    void confirmarPago(String paymentIntentId);

    /**
     * Cancela o marca como fallido el pago si el usuario cierra el formulario o hay error.
     */
    void cancelarPago(String paymentIntentId);
}