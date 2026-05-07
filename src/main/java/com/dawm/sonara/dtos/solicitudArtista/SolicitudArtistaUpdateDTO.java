package com.dawm.sonara.dtos.solicitudArtista;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudArtistaUpdateDTO {
    private String nombreArtista;
    private String generoSugerido;
    private String descripcion;
    private String fotoUrl;
    // No incluimos el estado aquí, se cambia mediante un endpoint de "aprobar"
}
