package com.dawm.sonara.dtos.solicitudArtista;

import lombok.Data;

@Data
public class SolicitudArtistaCreateDTO {
    private String nombreArtista;
    private String generoSugerido;
    private String descripcion;
    private String fotoUrl;
}