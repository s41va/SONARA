package com.dawm.sonara.controllers;

import jakarta.validation.Valid;
import com.dawm.sonara.dtos.auth.AuthRequestDTO;
import com.dawm.sonara.dtos.auth.AuthResponseDTO;
import com.dawm.sonara.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Controlador para el manejo de sesiones, login y generación de tokens JWT")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
            summary = "Autenticar usuario",
            description = "Valida las credenciales del usuario (nombre de usuario y contraseña) y, si son correctas, " +
                    "genera y devuelve un token JWT con sus roles correspondientes para acceder a las rutas protegidas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa. Se devuelve el token JWT.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Formato de solicitud inválido o faltan campos obligatorios"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas (Usuario o contraseña no válidos)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciales de acceso del usuario", required = true)
            @Valid @RequestBody AuthRequestDTO authRequest) {

        // 1) Autenticación (si falla, Spring lanza AuthenticationException y lo gestiona el ApiExceptionHandler)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        // 2) Username autenticado (normalmente el mismo que el enviado en el login)
        String username = authentication.getName();

        // 3) Roles/authorities del usuario autenticado
        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        // 4) Generación del JWT con subject + roles (claims)
        String token = jwtUtil.generateToken(username, roles);

        // 5) Respuesta OK con token
        return ResponseEntity.ok(new AuthResponseDTO(token, "Authentication successful"));
    }
}