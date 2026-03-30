package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.concierto.ConciertoCreateDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDetailDTO;
import com.dawm.sonara.dtos.concierto.ConciertoUpdateDTO;
import com.dawm.sonara.repositories.ConciertoRepository;
import com.dawm.sonara.services.ConciertoService;
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
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/concierto")
public class ConciertoController {
    private static final Logger logger = LoggerFactory.getLogger(ConciertoController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ConciertoService conciertoService;

    @Autowired
    private ConciertoRepository conciertoRepository;
    /*
    @GetMapping
    public ResponseEntity<Page<ConciertoDTO>> listConciertos(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("Solicitando la lista de conciertos... page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<ConciertoDTO> page = conciertoService.list(pageable);

        logger.info("Se han cargado {} conciertos en la pagina {}.",
                page.getNumberOfElements(), page.getNumber());
        return ResponseEntity.ok(page);
    }*/
    @Operation(
            summary = "Obtener todos los conciertos",
            description = "Devuelve una lista paginada de todos los conciertos disponibles en el sistema. " +
                    "Si se envía el parámetro 'unpaged=true', devuelve la lista completa sin paginación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de conciertos recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ConciertoDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> listAllConciertos(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestParam(defaultValue = "false") boolean unpaged) {

        if (unpaged) {
            return ResponseEntity.ok(conciertoService.listAll(Sort.by("name").ascending()));
        }

        return ResponseEntity.ok(conciertoService.list(pageable));
    }

    @Operation(
            summary = "Crear un nuevo concierto",
            description = "Permite registrar un nuevo concierto en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Concierto creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConciertoDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ConciertoDTO> createConcierto(@Valid @RequestBody ConciertoCreateDTO dto) {

        ConciertoDTO created = conciertoService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(
            summary = "Actualizar un concierto",
            description = "Permite actualizar los datos de un concierto existente en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Concierto actualizada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConciertoDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "404", description = "Concierto no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConciertoDTO> updateUsuario(@PathVariable Long id,
                                                      @Valid @RequestBody ConciertoUpdateDTO conciertoDTO) {
        logger.info("Actualizando concierto con ID {}", conciertoDTO.getId());

        ConciertoDTO updated = conciertoService.update(conciertoDTO);
        logger.info("Concierto con ID {} actualizado con éxito", conciertoDTO.getId());

        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar un concierto",
            description = "Permite eliminar un concierto específico de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Concierto eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Concierto no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcierto(@PathVariable Long id) {
        logger.info("Eliminando concierto con ID {}", id);

        conciertoService.delete(id);

        logger.info("Concierto con ID {} eliminado con éxito.", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener un concierto por ID",
            description = "Recupera un concierto específico según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Concierto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConciertoDetailDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Concierto no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConciertoDetailDTO> getRegionById(@PathVariable Long id) {
        logger.info("Mostrando detalles del concierto con ID {}", id);

        ConciertoDetailDTO conciertoDetailDTO = conciertoService.getDetail(id);

        return ResponseEntity.ok(conciertoDetailDTO);
    }
}

