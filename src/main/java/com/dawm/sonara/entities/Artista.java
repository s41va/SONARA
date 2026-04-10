package com.dawm.sonara.entities;

import com.dawm.sonara.response.ArtistaExterno;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
//@Entity (Sin entity pq no esta en la base de datos)
public class Artista {
    private String id;
    private String nombre;
    private String biografia;
    private String foto;
    private String web;

    // Constructor que recibe al "externo" y lo transforma
    public Artista(ArtistaExterno externo) {
        this.id = externo.idArtist;
        this.nombre = externo.strArtist;
        this.biografia = (externo.strBiographyES != null && !externo.strBiographyES.isEmpty())
                ? externo.strBiographyES : externo.strBiographyEN;
        this.foto = externo.strArtistThumb;
        this.web = externo.strWebsite;
    }
}