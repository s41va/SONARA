package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.dtos.artista.ArtistaFavoritoDTO;
import com.dawm.sonara.services.FavoritoService;
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

import java.util.Set;

@RestController
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos", description = "Controlador para la gestión de los artistas favoritos de los usuarios")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @Operation(
            summary = "Añadir artista a favoritos por ID",
            description = "Asocia un artista específico a la lista de favoritos de un usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista añadido a favoritos exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Usuario o artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/artistas/{usuarioId}/{artistaId}")
    public ResponseEntity<Void> agregarFavorito(
            @Parameter(description = "ID único del usuario", example = "1", required = true)
            @PathVariable Long usuarioId,
            @Parameter(description = "ID único del artista", example = "12345", required = true)
            @PathVariable String artistaId) {

        favoritoService.agregarArtistaAFavoritos(usuarioId, artistaId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Quitar de favoritos",
            description = "Elimina la asociación de un artista de la lista de favoritos del usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artista eliminado de favoritos correctamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Asociación, usuario o artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/artistas/{usuarioId}/{artistaId}")
    public ResponseEntity<Void> eliminarFavorito(
            @Parameter(description = "ID único del usuario", example = "1", required = true)
            @PathVariable Long usuarioId,
            @Parameter(description = "ID único del artista", example = "12345", required = true)
            @PathVariable String artistaId) {
        favoritoService.eliminarArtistaDeFavoritos(usuarioId, artistaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar mis favoritos",
            description = "Recupera la lista completa de artistas marcados como favoritos por un usuario en formato simplificado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Colección de artistas favoritos recuperada con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArtistaFavoritoDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/artistas/{usuarioId}")
    public ResponseEntity<Set<ArtistaFavoritoDTO>> listarFavoritos(
            @Parameter(description = "ID del usuario del que se quieren listar los favoritos", example = "1", required = true)
            @PathVariable Long usuarioId) {
        // Devuelve el SET de DTOs limpios (id, nombre, foto, genero)
        return ResponseEntity.ok(favoritoService.obtenerMisArtistasFavoritos(usuarioId));
    }
}