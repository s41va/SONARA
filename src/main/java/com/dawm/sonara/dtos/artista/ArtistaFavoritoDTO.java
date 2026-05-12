package com.dawm.sonara.dtos.artista;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistaFavoritoDTO {
    private String id;
    private String nombre;
    private String foto;
    private String genero;
}