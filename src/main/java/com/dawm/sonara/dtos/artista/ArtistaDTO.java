package com.dawm.sonara.dtos.artista;

import com.dawm.sonara.entities.Artista;
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
    private Integer generoId;
    private String foto;
    private String web;
    private Integer votosRanking; // Nuevo: Para el ranking

    // Constructor para convertir la Entidad local -> DTO (Ranking)
    public ArtistaDTO(Artista entidad) {
        this.id = entidad.getId().toString();
        this.nombre = entidad.getNombre();
        this.generoId = entidad.getGeneroId();
        this.votosRanking = entidad.getVotosRanking();
        // Los campos de la API se quedan null o vacíos en el ranking
    }

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