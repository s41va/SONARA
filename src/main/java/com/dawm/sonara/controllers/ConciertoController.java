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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Date;

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
            summary = "Obtener conciertos con filtros",
            description = "Devuelve una lista paginada de conciertos. Permite filtrar por nombre de artista, ciudad o fecha."
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
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "ubi", required = false) String localidad,
            @RequestParam(value = "fecha", required = false) String fecha,
            @RequestParam(defaultValue = "false") boolean unpaged) {

        logger.info("Solicitando conciertos. Filtros -> nombre: {}, localidad: {}, fecha: {}", nombre, localidad, fecha);

        if (unpaged) {
            // Se asume que listAll también podría recibir filtros si fuera necesario,
            // por ahora se mantiene la lógica de lista completa por nombre de artista
            return ResponseEntity.ok(conciertoService.listAll(Sort.by("artistaNombre").ascending()));
        }

        // LLAMADA CLAVE: Pasamos los parámetros de búsqueda al método list del service
        return ResponseEntity.ok(conciertoService.list(nombre, localidad, fecha, pageable));
    }

    @Operation(
            summary = "Buscar conciertos con filtros",
            description = "Devuelve una lista paginada de conciertos filtrados por nombre, fecha y/o ubicación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista filtrada recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ConciertoDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchConciertos(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam(required = false) String location,
            @PageableDefault(size = 10, sort = "date") Pageable pageable) {

        // Nota: El service debería manejar la lógica de qué filtros aplicar si vienen nulos
        return ResponseEntity.ok(conciertoService.findByFilters(name, date, location, pageable));
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
    public ResponseEntity<ConciertoDTO> updateConcierto(@PathVariable Long id,
                                                      @Valid @RequestBody ConciertoUpdateDTO conciertoDTO) {
        logger.info("Actualizando concierto con ID {}", conciertoDTO.getId());
        conciertoDTO.setId(id);
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
    public ResponseEntity<ConciertoDetailDTO> getConciertoById(@PathVariable Long id) {
        logger.info("Mostrando detalles del concierto con ID {}", id);

        ConciertoDetailDTO conciertoDetailDTO = conciertoService.getDetail(id);

        return ResponseEntity.ok(conciertoDetailDTO);
    }
}

