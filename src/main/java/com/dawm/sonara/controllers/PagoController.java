package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.pagos.StripeResponseDTO;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.services.PagoService;
import com.dawm.sonara.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService paymentService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Iniciar el proceso de pago
    @PostMapping("/checkout/{conciertoId}")
    public ResponseEntity<?> crearIntent(@PathVariable Long conciertoId) {
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

    // 2. Confirmar que el pago se hizo correctamente en el cliente
    @PostMapping("/confirmar/{intentId}")
    public ResponseEntity<?> confirmar(@PathVariable String intentId) {
        try {
            paymentService.confirmarPago(intentId);
            return ResponseEntity.ok("Pago confirmado y stock actualizado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}