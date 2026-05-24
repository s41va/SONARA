package com.dawm.sonara.dtos.concierto;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConciertoDetailDTO {

    private Long id;
    private ArtistaDTO artista;
    private LocalidadDTO localidad;
    private LocalDateTime fechaHora;
    private String local;
    private String descripcion;
    private BigDecimal precio;
}