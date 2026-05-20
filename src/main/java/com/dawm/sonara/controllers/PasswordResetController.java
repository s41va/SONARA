package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.password.PasswordResetDTO;
import com.dawm.sonara.dtos.password.PasswordResetRequestDTO;
import com.dawm.sonara.services.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticación", description = "Controlador para el manejo de sesiones, login y recuperación de cuentas")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private MessageSource messageSource;

    @Operation(
            summary = "Solicitar recuperación de contraseña (PASO 1)",
            description = "Recibe el correo electrónico del usuario y, si está registrado, genera un token temporal " +
                    "y envía un enlace con las instrucciones para restablecer la contraseña al buzón de correo. " +
                    "Por motivos de seguridad, siempre responde con un código 200 OK para evitar la enumeración de emails válidos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitud procesada correctamente (Mensaje internacionalizado de confirmación de envío)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Si el correo electrónico existe en nuestro sistema, se ha enviado un enlace de recuperación.\"}")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Formato de correo electrónico inválido o cuerpo de la petición erróneo"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar el envío del correo electrónico")
    })
    @PostMapping("/forgot")
    public ResponseEntity<?> handleForgotPassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO con el email del usuario que solicita el cambio de contraseña", required = true)
            @Valid @RequestBody PasswordResetRequestDTO dto,
            @Parameter(hidden = true) HttpServletRequest request
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

    @Operation(
            summary = "Restablecer contraseña con token (PASO 2)",
            description = "Valida el token de recuperación enviado desde Angular. Si el token es válido, no ha expirado, " +
                    "no ha sido usado y las contraseñas coinciden, se procede a cambiar la contraseña del usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contraseña actualizada con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Su contraseña ha sido restablecida correctamente.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Las contraseñas no coinciden, o bien el token es inválido, caducado o ya ha sido utilizado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"error\": \"El enlace de recuperación es inválido o ha expirado.\"}")
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> handleResetPassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos que contienen el token, la nueva contraseña y la confirmación", required = true)
            @Valid @RequestBody PasswordResetDTO dto) {
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