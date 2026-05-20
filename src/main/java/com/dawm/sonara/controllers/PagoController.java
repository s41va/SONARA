package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.pagos.StripeResponseDTO;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.services.PagoService;
import com.dawm.sonara.repositories.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "Controlador para la integración con Stripe, pasarela de pagos y confirmación de transacciones")
public class PagoController {

    @Autowired
    private PagoService paymentService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(
            summary = "Iniciar el proceso de pago (Checkout)",
            description = "Crea un intento de pago (PaymentIntent) en Stripe para un concierto específico vinculando al usuario autenticado en la sesión."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Intento de pago creado de forma exitosa en Stripe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StripeResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud o datos del concierto no válidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado o sesión no válida"),
            @ApiResponse(responseCode = "500", description = "Error interno o fallo de comunicación con Stripe")
    })
    @PostMapping("/checkout/{conciertoId}")
    public ResponseEntity<?> crearIntent(
            @Parameter(description = "ID del concierto para el que se compran las entradas", example = "1", required = true)
            @PathVariable Long conciertoId) {
        try {
            // Obtenemos el usuario autenticado (ajusta según tu seguridad)
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            StripeResponseDTO response = paymentService.crearIntentoPago(conciertoId, usuario);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Confirmar pago",
            description = "Confirma que el cobro se ha completado correctamente en el cliente, consolidando la compra y actualizando el stock de entradas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago confirmado correctamente y stock actualizado",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Pago confirmado y stock actualizado")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "El ID del intento no es válido o la confirmación falló en el servidor"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/confirmar/{intentId}")
    public ResponseEntity<?> confirmar(
            @Parameter(description = "Identificador único del PaymentIntent provisto por Stripe", example = "pi_3MtwF2LkdIwI2ixR1aBcDeFg", required = true)
            @PathVariable String intentId) {
        try {
            paymentService.confirmarPago(intentId);
            return ResponseEntity.ok("Pago confirmado y stock actualizado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}