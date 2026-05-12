package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.password.PasswordResetDTO;
import com.dawm.sonara.dtos.password.PasswordResetRequestDTO;
import com.dawm.sonara.services.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Controlador REST para el flujo de recuperación de contraseña con Angular.
 */
@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private MessageSource messageSource;

    /**
     * PASO 1: El usuario envía su email desde Angular para solicitar el reset.
     * POST /api/auth/forgot
     */
    @PostMapping("/forgot")
    public ResponseEntity<?> handleForgotPassword(
            @Valid @RequestBody PasswordResetRequestDTO dto,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // La lógica interna decide si envía el mail (con link al puerto 4200)
        passwordResetService.requestPasswordReset(dto.getEmail(), ip, userAgent);

        Locale locale = LocaleContextHolder.getLocale();
        String msg = messageSource.getMessage("msg.password-reset.request.sent", null, locale);

        Map<String, String> response = new HashMap<>();
        response.put("message", msg);

        // Siempre devolvemos 200 OK para evitar enumeración de emails
        return ResponseEntity.ok(response);
    }

    /**
     * PASO 2: Angular envía el token y la nueva contraseña.
     * POST /api/auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> handleResetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, String> response = new HashMap<>();

        // Validación manual de coincidencia de contraseñas
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            String errorMsg = messageSource.getMessage("password.mismatch", null, locale);
            response.put("error", errorMsg);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            passwordResetService.resetPassword(dto.getToken(), dto.getNewPassword());

            String msg = messageSource.getMessage("msg.password-reset.success", null, locale);
            response.put("message", msg);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            // Token inválido, caducado o usado
            String msg = messageSource.getMessage("msg.password-reset.invalid", null, locale);
            response.put("error", msg);
            return ResponseEntity.badRequest().body(response);
        }
    }
}