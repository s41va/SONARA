package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.concierto.ConciertoCreateDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDetailDTO;
import com.dawm.sonara.dtos.concierto.ConciertoUpdateDTO;
import com.dawm.sonara.repositories.ConciertoRepository;
import com.dawm.sonara.services.ConciertoService;
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
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("/api/concierto")
@Tag(name = "Conciertos", description = "Controlador para la gestión, programación y búsqueda de conciertos")
public class ConciertoController {
    private static final Logger logger = LoggerFactory.getLogger(ConciertoController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ConciertoService conciertoService;

    @Autowired
    private ConciertoRepository conciertoRepository;

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
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @Parameter(description = "Filtro por nombre del artista", example = "Coldplay")
            @RequestParam(value = "nombre", required = false) String nombre,
            @Parameter(description = "Filtro por ubicación o ciudad", example = "Madrid")
            @RequestParam(value = "ubi", required = false) String localidad,
            @Parameter(description = "Filtro por fecha en formato cadena", example = "2026-05-20")
            @RequestParam(value = "fecha", required = false) String fecha,
            @Parameter(description = "Indica si se requiere la lista completa sin paginación", example = "false")
            @RequestParam(defaultValue = "false") boolean unpaged) {

        logger.info("Solicitando conciertos. Filtros -> nombre: {}, localidad: {}, fecha: {}", nombre, localidad, fecha);

        if (unpaged) {
            return ResponseEntity.ok(conciertoService.listAll(Sort.by("artistaNombre").ascending()));
        }

        return ResponseEntity.ok(conciertoService.list(nombre, localidad, fecha, pageable));
    }

    @Operation(
            summary = "Buscar conciertos con filtros avanzados",
            description = "Devuelve una lista paginada de conciertos filtrados formalmente por nombre, fecha estructurada y/o ubicación."
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
            @Parameter(description = "Nombre o término de búsqueda", example = "Coldplay")
            @RequestParam(required = false) String name,
            @Parameter(description = "Fecha exacta del concierto (Formato ISO: YYYY-MM-DD)", example = "2026-05-20")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @Parameter(description = "Ubicación o local del evento", example = "Estadio Metropolitano")
            @RequestParam(required = false) String location,
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "date") Pageable pageable) {

        return ResponseEntity.ok(conciertoService.findByFilters(name, date, location, pageable));
    }

    @Operation(
            summary = "Crear un nuevo concierto",
            description = "Permite registrar un nuevo concierto en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Concierto creado exitosamente. Retorna la localización del nuevo recurso en el header.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConciertoDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados para el registro"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ConciertoDTO> createConcierto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura de datos requerida para agendar un concierto", required = true)
            @Valid @RequestBody ConciertoCreateDTO dto) {

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
            description = "Permite actualizar por completo los datos de un concierto existente localizándolo mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Concierto actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConciertoDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "ID o datos proporcionados incompatibles o inválidos"),
            @ApiResponse(responseCode = "404", description = "Concierto no encontrado con el ID indicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConciertoDTO> updateConcierto(
            @Parameter(description = "ID del concierto a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos modificados del concierto", required = true)
            @Valid @RequestBody ConciertoUpdateDTO conciertoDTO) {
        logger.info("Actualizando concierto con ID {}", conciertoDTO.getId());
        conciertoDTO.setId(id);
        ConciertoDTO updated = conciertoService.update(conciertoDTO);
        logger.info("Concierto con ID {} actualizado con éxito", conciertoDTO.getId());

        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar un concierto",
            description = "Permite eliminar un concierto específico del registro de manera permanente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Concierto eliminado exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Concierto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcierto(
            @Parameter(description = "ID del concierto a dar de baja", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("Eliminando concierto con ID {}", id);

        conciertoService.delete(id);

        logger.info("Concierto con ID {} eliminado con éxito.", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener un concierto por ID",
            description = "Recupera los datos extendidos y detallados de un concierto específico según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Detalle del concierto localizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConciertoDetailDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Concierto no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConciertoDetailDTO> getConciertoById(
            @Parameter(description = "ID único del concierto a consultar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("Mostrando detalles del concierto con ID {}", id);

        ConciertoDetailDTO conciertoDetailDTO = conciertoService.getDetail(id);

        return ResponseEntity.ok(conciertoDetailDTO);
    }
}