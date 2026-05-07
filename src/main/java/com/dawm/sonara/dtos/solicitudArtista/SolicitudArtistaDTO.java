package com.dawm.sonara.dtos.solicitudArtista;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SolicitudArtistaDTO {
    private Long id;
    private String nombreArtista;
    private String generoSugerido;
    private String descripcion;
    private String fotoUrl;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private String nombreSolicitante; // Para mostrar quién lo pidió
}