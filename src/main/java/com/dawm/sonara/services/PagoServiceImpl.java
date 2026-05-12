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
import com.stripe.param.PaymentIntentCreateParams;
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
    public StripeResponseDTO crearIntentoPago(Long conciertoId, Usuario usuario) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        Concierto concierto = conciertoRepository.findById(conciertoId)
                .orElseThrow(() -> new RuntimeException("Concierto no encontrado"));

        if (concierto.getStock() <= 0) {
            throw new RuntimeException("No quedan entradas para este concierto");
        }

        // 1. Configurar el intento de pago en Stripe
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(concierto.getPrecio().multiply(new BigDecimal(100)).longValue()) // Stripe usa céntimos
                .setCurrency("eur")
                .setReceiptEmail(usuario.getEmail())
                .putMetadata("concierto_id", conciertoId.toString())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        // 2. Registrar el pago en nuestra DB como PENDIENTE
        InformacionPago pago = new InformacionPago();
        pago.setUsuario(usuario);
        pago.setConcierto(concierto);
        pago.setMontoPago(concierto.getPrecio());
        pago.setIdTransaccionStripe(intent.getId());
        pago.setEstadoPago(Estado.PENDIENTE); 
        pago.setFechaPago(LocalDateTime.now());

        pagoRepository.save(pago);

        return new StripeResponseDTO(intent.getId(), intent.getClientSecret());
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