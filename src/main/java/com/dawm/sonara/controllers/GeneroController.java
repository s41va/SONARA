package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.dtos.generos.GenerosDetailDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import com.dawm.sonara.services.GeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping("/api/generos")
public class GeneroController {

    private static final Logger logger = LoggerFactory.getLogger(GeneroController.class);

    @Autowired
    private GeneroService generoService;

    @Operation(
            summary = "Obtener todos los géneros",
            description = "Devuelve una lista paginada de todos los géneros musicales."
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
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {

        logger.info("Listando géneros vía API: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(generoService.list(pageable));
    }

    @Operation(
            summary = "Obtener un género por ID",
            description = "Recupera la información detallada de un género específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género encontrado"),
            @ApiResponse(responseCode = "404", description = "Género no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GenerosDetailDTO> getGeneroById(@PathVariable Long id) {
        logger.info("Obteniendo detalle del género ID: {}", id);
        return ResponseEntity.ok(generoService.getDetail(id));
    }

    @Operation(
            summary = "Crear un nuevo género",
            description = "Registra un nuevo género musical en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Género creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "El nombre del género ya existe")
    })
    @PostMapping
    public ResponseEntity<GenerosDTO> createGenero(@Valid @RequestBody GenerosCreateDTO dto) {
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
            description = "Actualiza los datos de un género basándose en su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Género actualizado"),
            @ApiResponse(responseCode = "404", description = "Género no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GenerosDTO> updateGenero(@PathVariable Long id,
                                                   @Valid @RequestBody GenerosUpdateDTO dto) {
        logger.info("Actualizando género ID: {}", id);
        dto.setId(id);
        GenerosDTO updated = generoService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar un género",
            description = "Borra de forma permanente un género del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Género eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Género no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenero(@PathVariable Long id) {
        logger.info("Eliminando género ID: {}", id);
        generoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}