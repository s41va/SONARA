package com.dawm.sonara.dtos.artista;

import com.dawm.sonara.response.ArtistaExterno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistaDTO {
    private String id;
    private String nombre;
    private String biografia;
    private String foto;
    private String web;

    // Constructor de conversión
    public ArtistaDTO(ArtistaExterno externo) {
        this.id = externo.idArtist;
        this.nombre = externo.strArtist;
        this.biografia = (externo.strBiographyES != null && !externo.strBiographyES.isEmpty())
                ? externo.strBiographyES : externo.strBiographyEN;
        this.foto = externo.strArtistThumb;
        this.web = externo.strWebsite;
    }
}