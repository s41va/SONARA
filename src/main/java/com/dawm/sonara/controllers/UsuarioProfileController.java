package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.perfil.UsuarioProfileDTO;
import com.dawm.sonara.dtos.perfil.UsuarioProfileUpdateDTO;
import com.dawm.sonara.services.UsuarioProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Perfil de Usuario", description = "Controlador para que los usuarios autenticados gestionen sus datos personales, información de cuenta y preferencias")
public class UsuarioProfileController {

    @Autowired
    private UsuarioProfileService perfilService;

    @Operation(
            summary = "Obtener perfil del usuario autenticado",
            description = "Recupera la información detallada del perfil y las preferencias musicales del usuario que ha iniciado sesión a través del token de seguridad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos del perfil recuperados con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioProfileDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado o token de sesión inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontró el perfil asociado al usuario autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioProfileDTO> getMiPerfil(@Parameter(hidden = true) Authentication authentication) {
        // authentication.getName() suele ser el email si así está configurado en tu UserDetailsService
        UsuarioProfileDTO dto = perfilService.obtenerPerfilPorEmail(authentication.getName());
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Actualizar perfil del usuario autenticado",
            description = "Permite al usuario en sesión modificar sus propios datos personales y preferencias de la cuenta. " +
                    "El email del token se utiliza para blindar el endpoint y asegurar que nadie edite perfiles ajenos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioProfileDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos o con formato incorrecto"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado o token de sesión inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontró el perfil que se intenta actualizar"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/perfil")
    public ResponseEntity<UsuarioProfileDTO> updateMiPerfil(
            @Parameter(hidden = true) Authentication authentication,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura con los nuevos datos y preferencias del perfil a guardar", required = true)
            @Valid @RequestBody UsuarioProfileUpdateDTO updateDTO) {

        // Usamos el email del token para asegurar que el usuario solo edite su propio perfil
        UsuarioProfileDTO actualizado = perfilService.actualizarPerfil(authentication.getName(), updateDTO);
        return ResponseEntity.ok(actualizado);
    }
}