package com.dawm.sonara.services;

import com.dawm.sonara.dtos.pagos.StripeResponseDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Concierto;
import com.dawm.sonara.entities.InformacionPago;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.entities.enums.Estado;
import com.dawm.sonara.repositories.ArtistaRepository;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class PagoServiceImpl implements PagoService {

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    @Value("${APP_PUBLIC_BASE_URL}")
    private String frontendUrl;

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Override
    @Transactional
    public StripeResponseDTO crearIntentoPago(Long conciertoId, Usuario usuario) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        // 1. Buscamos el concierto usando el ID que viene por parámetro
        Concierto concierto = conciertoRepository.findById(conciertoId)
                .orElseThrow(() -> new RuntimeException("Concierto no encontrado"));

        // 2. Extraemos y codificamos de forma segura todas las características para la URL
        String artistaCodificado = URLEncoder.encode(concierto.getArtistaNombre(), StandardCharsets.UTF_8);

        String fechaCodificada = URLEncoder.encode(concierto.getFechaHora().toString(), StandardCharsets.UTF_8);

        // Combinamos la localidad y el local específico del concierto
        String lugarCompleto = concierto.getLocalidad().getNombreCiudad() + " - " + concierto.getLocal();
        String lugarCodificado = URLEncoder.encode(lugarCompleto, StandardCharsets.UTF_8);

        String descCodificada = URLEncoder.encode(concierto.getDescripcion() != null ? concierto.getDescripcion() : "", StandardCharsets.UTF_8);

        String precioString = concierto.getPrecio().setScale(2, java.math.RoundingMode.HALF_UP).toString() + "€";
        String precioCodificado = URLEncoder.encode(precioString, StandardCharsets.UTF_8);

        // 3. Calculamos el precio real en céntimos para Stripe (ej: 25.50 -> 2550)
        long montoCentimos = concierto.getPrecio().multiply(new BigDecimal(100)).longValue();

        // 4. Construimos la URL de éxito con todos los parámetros anexados
        String successUrl = frontendUrl + "/pago-exito"
                + "?session_id={CHECKOUT_SESSION_ID}"
                + "&artista=" + artistaCodificado
                + "&fecha=" + fechaCodificada
                + "&lugar=" + lugarCodificado
                + "&descripcion=" + descCodificada
                + "&precio=" + precioCodificado;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(frontendUrl + "/concierto")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(montoCentimos)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Entrada para " + concierto.getArtistaNombre())
                                                                .setDescription(concierto.getLocalidad().getNombreCiudad() + " - " + concierto.getLocal())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);
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