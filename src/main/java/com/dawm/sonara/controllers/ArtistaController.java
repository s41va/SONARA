package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.services.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/artistas")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "Artistas", description = "Controlador para la gestión de artistas y votaciones")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Operation(
            summary = "Obtener detalle por nombre",
            description = "Recupera la información de un artista desde la API externa (TheAudioDB) utilizando su nombre."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista encontrado",
                    content = @Content(schema = @Schema(implementation = ArtistaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ArtistaDTO> obtener(@PathVariable String nombre) {
        ArtistaDTO dto = artistaService.buscarPorNombre(nombre);

        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Listar todos los artistas",
            description = "Devuelve una lista de todos los artistas almacenados con opciones de ordenación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArtistaDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ArtistaDTO>> listar(
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(artistaService.obtenerTodosOrdenados(sortField, sortDir));
    }

    @Operation(
            summary = "Obtener detalle por ID",
            description = "Recupera la información completa de un artista utilizando su ID de la API externa."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle del artista encontrado",
                    content = @Content(schema = @Schema(implementation = ArtistaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArtistaDTO> obtenerDetalle(@PathVariable Integer id) {
        ArtistaDTO detalle = artistaService.obtenerPorIdCompleto(id);

        return (detalle != null)
                ? ResponseEntity.ok(detalle)
                : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Eliminar un artista",
            description = "Elimina un artista de la base de datos local mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artista eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (artistaRepository.existsById(id)) {
            artistaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Obtener ranking de artistas",
            description = "Devuelve el Top 10 de los artistas más votados en la base de datos local."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking recuperado exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArtistaDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No hay datos suficientes para el ranking"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/ranking")
    public ResponseEntity<List<ArtistaDTO>> obtenerRanking() {
        List<ArtistaDTO> ranking = artistaService.obtenerRanking();

        if (ranking.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ranking);
    }

    @Operation(
            summary = "Votar por un artista",
            description = "Registra un voto para un artista. Si el artista no existe localmente, se crea automáticamente (Lazy Insert)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de artista inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/votar/{id}")
    public ResponseEntity<Void> votar(
            @PathVariable String id,
            @RequestParam String nombre) {
        try {
            Integer idNumerico = Integer.parseInt(id);
            artistaService.votarArtista(idNumerico, nombre);
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}