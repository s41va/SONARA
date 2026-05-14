package com.dawm.sonara.services;

import com.dawm.sonara.dtos.pagos.StripeResponseDTO;
import com.dawm.sonara.entities.Concierto;
import com.dawm.sonara.entities.InformacionPago;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.entities.enums.Estado;
import com.dawm.sonara.repositories.ConciertoRepository;
import com.dawm.sonara.repositories.PagoRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PagoServiceImpl implements PagoService {

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    @Transactional
    // En tu PagoService.java
    public StripeResponseDTO crearIntentoPago(Long conciertoId, Usuario usuario) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // Importante: Aquí configuras a dónde vuelve el usuario tras pagar
                .setSuccessUrl("http://localhost:4200/pago-exito?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/pago-cancelado")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(2000L) // Ejemplo: 20.00€
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Entrada Concierto ID: " + conciertoId)
                                                                .build()
                                                    )
                                                .build()
                                )
                                .build()
                )
                .build();

        // 1. Creamos la sesión en los servidores de Stripe
        Session session = Session.create(params);

        // 2. ¡AQUÍ ESTÁ EL CAMBIO!
        // Devuelve session.getUrl() que es la URL a la que el usuario debe ir
        return new StripeResponseDTO(session.getUrl());
    }

    @Override
    @Transactional
    public void confirmarPago(String paymentIntentId) {
        InformacionPago pago = pagoRepository.findByIdTransaccionStripe(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (pago.getEstadoPago() == Estado.PENDIENTE) {
            // 1. Cambiamos estado
            pago.setEstadoPago(Estado.APROBADA);

            // 2. Restamos stock al concierto
            Concierto concierto = pago.getConcierto();
            concierto.setStock(concierto.getStock() - 1);

            conciertoRepository.save(concierto);
            pagoRepository.save(pago);
        }
    }

    @Override
    @Transactional
    public void cancelarPago(String paymentIntentId) {
        InformacionPago pago = pagoRepository.findByIdTransaccionStripe(paymentIntentId)
                .orElse(null);
        if (pago != null) {
            pago.setEstadoPago(Estado.RECHAZADA);
            pagoRepository.save(pago);
        }
    }
}