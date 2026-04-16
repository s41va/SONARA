package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.cancion.CancionDTO;
import com.dawm.sonara.services.CancionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/canciones")
@Tag(name = "Canciones", description = "Controlador para la búsqueda y gestión de canciones")
public class CancionController {

    @Autowired
    private CancionService cancionService;

    @Operation(
            summary = "Buscar una canción específica",
            description = "Busca los detalles de una canción (incluyendo letra y metadatos) basándose en el nombre del artista y el título del tema. " +
                    "Consulta fuentes externas si no se encuentra en la base de datos local."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Canción encontrada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CancionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna canción con esos criterios"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar")
    public ResponseEntity<CancionDTO> buscar(
            @Parameter(description = "Nombre del artista", example = "Coldplay")
            @RequestParam String artista,
            @Parameter(description = "Título de la canción", example = "Yellow")
            @RequestParam String titulo) {

        CancionDTO cancion = cancionService.buscarCancion(artista, titulo);

        if (cancion == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cancion);
    }
}