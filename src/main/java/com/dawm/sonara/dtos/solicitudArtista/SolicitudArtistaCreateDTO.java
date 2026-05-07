package com.dawm.sonara.dtos.solicitudArtista;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudArtistaCreateDTO {
    private String nombreArtista;
    private String generoSugerido;
    private String descripcion;
    private String fotoUrl;
}