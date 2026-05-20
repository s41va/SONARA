package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaRankingDTO;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.repositories.UsuarioRepository;
import com.dawm.sonara.services.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
@Tag(name = "Ranking y Votos", description = "Controlador para la consulta de estadísticas de artistas y la gestión del sistema de votaciones por usuario")
public class RankingController {

    @Autowired
    private VotoService votoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- SECCIÓN DE CONSULTAS (RANKING) ---

    @Operation(
            summary = "Ranking por Localidad",
            description = "Obtiene el Top de los artistas más votados en una ciudad o localidad específica proporcionada por parámetro."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ranking de la localidad recuperado con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArtistaRankingDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada en el sistema"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/localidad/{nombreCiudad}")
    public ResponseEntity<List<ArtistaRankingDTO>> getRankingByLocalidad(
            @Parameter(description = "Nombre de la ciudad o localidad a consultar", example = "Sevilla", required = true)
            @PathVariable String nombreCiudad) {
        return ResponseEntity.ok(votoService.getRankingLocal(nombreCiudad));
    }

    @Operation(
            summary = "Ranking Global",
            description = "Obtiene la lista consolidada de los artistas con mayor número de votos acumulados en toda la plataforma."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ranking global recuperado con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArtistaRankingDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/global")
    public ResponseEntity<List<ArtistaRankingDTO>> getRankingGlobal() {
        return ResponseEntity.ok(votoService.getRankingGlobal());
    }

    // --- SECCIÓN DE ACCIONES (VOTOS) ---

    @Operation(
            summary = "Votar Artista",
            description = "Registra o incrementa el voto hacia un artista específico utilizando la identidad del usuario autenticado en el contexto de seguridad."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Voto registrado correctamente (Retorna un cuerpo vacío)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en la solicitud (Usuario no encontrado, límite de votos alcanzado o artista no válido)"
            ),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado en la plataforma"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/votar/{artistaId}")
    public ResponseEntity<?> votar(
            @Parameter(description = "ID único del artista al que se le va a asignar el voto", example = "art_9x8c7b6a", required = true)
            @PathVariable String artistaId) {
        try {
            String email = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getName();

            Usuario usuarioLogueado = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            votoService.votar(artistaId, usuarioLogueado);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}