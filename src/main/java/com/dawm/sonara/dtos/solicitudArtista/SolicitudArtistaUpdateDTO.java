package com.dawm.sonara.dtos.solicitudArtista;

import lombok.Data;

@Data
public class SolicitudArtistaUpdateDTO {
    private String nombreArtista;
    private String generoSugerido;
    private String descripcion;
    private String fotoUrl;
    // No incluimos el estado aquí, se cambia mediante un endpoint de "aprobar"
}
