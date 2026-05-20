package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.services.GeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/generos")
@Tag(name = "Géneros", description = "Controlador para la administración y consulta del catálogo de géneros musicales")
public class GeneroController {

    private static final Logger logger = LoggerFactory.getLogger(GeneroController.class);

    @Autowired
    private GeneroService generoService;

    @Operation(
            summary = "Obtener todos los géneros",
            description = "Devuelve una lista paginada de todos los géneros musicales registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de géneros recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GenerosDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<GenerosDTO>> listGeneros(
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {

        logger.info("Listando géneros vía API: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(generoService.list(pageable));
    }

    @Operation(
            summary = "Obtener un género por ID",
            description = "Recupera la información detallada de un género específico mediante su identificador numérico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Género encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenerosDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Género no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GenerosDTO> getGeneroById(
            @Parameter(description = "ID único del género a consultar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("Obteniendo detalle del género ID: {}", id);
        return ResponseEntity.ok(generoService.getDetail(id));
    }

    @Operation(
            summary = "Crear un nuevo género",
            description = "Registra un nuevo género musical en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Género creado correctamente. Retorna la localización del nuevo recurso en el header.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenerosDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes"),
            @ApiResponse(responseCode = "409", description = "El nombre del género ya se encuentra registrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<GenerosDTO> createGenero(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura de datos del nuevo género", required = true)
            @Valid @RequestBody GenerosDTO dto) {
        logger.info("Creando nuevo género: {}", dto.getNombre());
        GenerosDTO created = generoService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(
            summary = "Actualizar un género existente",
            description = "Actualiza los datos de un género basándose en su ID único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Género actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenerosDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos proporcionados inválidos"),
            @ApiResponse(responseCode = "404", description = "Género no encontrado con el ID indicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GenerosDTO> updateGenero(
            @Parameter(description = "ID del género a modificar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del género", required = true)
            @Valid @RequestBody GenerosDTO dto) {
        logger.info("Actualizando género ID: {}", id);
        dto.setId(id);
        GenerosDTO updated = generoService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar un género",
            description = "Borra de forma permanente un género musical del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Género eliminado correctamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Género no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenero(
            @Parameter(description = "ID del género a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("Eliminando género ID: {}", id);
        generoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener lista simple de géneros",
            description = "Devuelve todos los géneros sin paginación, ideal para rellenar componentes selectores o desplegables en el front-end."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista completa de géneros recuperada con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GenerosDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/all")
    public ResponseEntity<List<GenerosDTO>> listAll() {
        logger.info("Listando todos los géneros para selector");
        return ResponseEntity.ok(generoService.listAllPlain());
    }
}