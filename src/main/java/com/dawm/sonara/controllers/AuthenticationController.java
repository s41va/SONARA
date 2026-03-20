package com.dawm.sonara.controllers;

import jakarta.validation.Valid;
import com.dawm.sonara.dtos.auth.AuthRequestDTO;
import com.dawm.sonara.dtos.auth.AuthResponseDTO;
import com.dawm.sonara.utils.JwtUtil;
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
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * Autentica al usuario y genera un token JWT si las credenciales son válidas.
     * * <p>Flujo:</p>
     * <ol>
     * <li>Recibe credenciales en el body (JSON) y las valida con {@code @Valid}.</li>
     * <li>Delegación en {@link AuthenticationManager} para autenticar.</li>
     * <li>Extrae el nombre de usuario y los roles desde {@link Authentication}.</li>
     * <li>Genera el JWT incluyendo roles como claim.</li>
     * <li>Devuelve el token en un {@link AuthResponseDTO}.</li>
     * </ol>
     * * @param authRequest DTO con {@code username} y {@code password}.
     * @return {@link ResponseEntity} con {@link AuthResponseDTO} que incluye el token JWT.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO authRequest) {
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
