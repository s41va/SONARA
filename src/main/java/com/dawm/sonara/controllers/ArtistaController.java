package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.services.ArtistaService;
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
@RequestMapping("/api/artistas")
@Tag(name = "Artistas", description = "Gestión del catálogo de artistas e importación desde TheAudioDB")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @Operation(
            summary = "Obtener detalle por nombre (API Externa)",
            description = "Busca la información de un artista específico por su nombre utilizando el servicio externo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Artista encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún artista con ese nombre"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ArtistaDTO> obtenerPorNombre(
            @Parameter(description = "Nombre del artista a buscar", example = "Coldplay", required = true)
            @PathVariable String nombre) {
        ArtistaDTO dto = artistaService.buscarPorNombre(nombre);
        return (dto != null) ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Listar artistas locales",
            description = "Lista todos los artistas guardados en la base de datos local de manera ordenada."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de artistas recuperado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArtistaDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ArtistaDTO>> listar(
            @Parameter(description = "Campo por el cual ordenar la lista", example = "nombre")
            @RequestParam(defaultValue = "nombre") String sortField,
            @Parameter(description = "Dirección del ordenamiento (asc o desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(artistaService.obtenerTodosOrdenados(sortField, sortDir));
    }

    @Operation(
            summary = "Obtener detalle completo por ID",
            description = "Recupera los datos detallados de un artista de la base de datos local usando su ID único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Detalle del artista encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No se encontró el artista con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArtistaDTO> obtenerDetalle(
            @Parameter(description = "ID único del artista", example = "12345", required = true)
            @PathVariable String id) {
        ArtistaDTO detalle = artistaService.obtenerPorIdCompleto(id);
        return (detalle != null) ? ResponseEntity.ok(detalle) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Importar artista",
            description = "Guarda la información de un artista proveniente de la API externa en la base de datos local."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Artista importado y guardado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos del artista no válidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ArtistaDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del artista a importar", required = true)
            @RequestBody ArtistaDTO artistaDTO) {
        ArtistaDTO guardado = artistaService.guardarArtistaLocal(artistaDTO);
        return ResponseEntity.status(201).body(guardado);
    }

    @Operation(
            summary = "Eliminar artista local",
            description = "Elimina de forma permanente un artista de la base de datos local mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "24", description = "Artista eliminado exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "No se encontró el artista a eliminar"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del artista a eliminar", example = "12345", required = true)
            @PathVariable String id) {
        artistaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}