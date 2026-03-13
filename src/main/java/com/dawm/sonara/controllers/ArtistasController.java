package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artistas.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistas.ArtistasUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.dawm.sonara.services.ArtistaService;
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
@RequestMapping("/api/artistas")
public class ArtistasController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistasController.class);

    @Autowired
    private ArtistaService artistaService;

    @Operation(
            summary = "Obtener todos los artistas",
            description = "Devuelve una lista paginada de todos los artistas. Si se envía 'unpaged=true', se devuelve la lista completa."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de artistas recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArtistasDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> listAllArtistas(
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "false") boolean unpaged) {

        logger.info("Solicitando lista de artistas... unpaged={}", unpaged);

        if (unpaged) {
            return ResponseEntity.ok(artistaService.listAll(Sort.by("nombre").ascending()));
        }

        return ResponseEntity.ok(artistaService.list(pageable));
    }

    @Operation(
            summary = "Obtener un artista por ID",
            description = "Recupera la información detallada de un artista específico según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Artista encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistasDetailDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArtistasDetailDTO> getArtistaById(@PathVariable Long id) {
        logger.info("Mostrando detalle del artista con ID {}", id);
        ArtistasDetailDTO detailDTO = artistaService.getDetail(id);
        return ResponseEntity.ok(detailDTO);
    }

    @Operation(
            summary = "Crear un nuevo artista",
            description = "Registra un nuevo artista en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Artista creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistasDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto: El artista ya existe")
    })
    @PostMapping
    public ResponseEntity<ArtistasDTO> createArtista(@Valid @RequestBody ArtistasCreateDTO dto) {
        logger.info("Creando nuevo artista: {}", dto.getNombre_artistico());
        ArtistasDTO created = artistaService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(
            summary = "Actualizar un artista existente",
            description = "Actualiza los datos de un artista basándose en su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista actualizado"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArtistasDTO> updateArtista(@PathVariable Long id,
                                                     @Valid @RequestBody ArtistasUpdateDTO dto) {
        logger.info("Actualizando artista con ID {}", id);
        dto.setId(id);
        ArtistasDTO updated = artistaService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar un artista",
            description = "Borra de forma permanente un artista del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artista eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtista(@PathVariable Long id) {
        logger.info("Eliminando artista con ID {}", id);
        artistaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener géneros musicales", description = "Lista todos los géneros disponibles.")
    @GetMapping("/generos")
    public ResponseEntity<List<String>> getGeneros() {
        // Mapeamos la lista de objetos Genero a una lista de Strings usando el nombre
        List<String> nombres = artistaService.findAllGeneros().stream()
                .map(g -> g.getNombre()) // O el método que obtenga el nombre en tu entidad Genero
                .toList();

        return ResponseEntity.ok(nombres);
    }
}