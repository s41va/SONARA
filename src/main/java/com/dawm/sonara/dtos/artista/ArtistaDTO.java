package com.dawm.sonara.dtos.artista;

import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.response.ArtistaExterno;
import com.dawm.sonara.services.ArtistaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistaDTO {

    private ArtistaRepository artistaRepository;

    private String id;
    private String nombre;
    private String biografia;
    private String genero;
    private String foto;
    private String web;
    private LocalDateTime ultimaSincronizacion;
    private Integer votosRanking;



    // Constructor para convertir la Entidad local -> DTO (Ranking)
    public ArtistaDTO(Artista entidad) {
        this.id = entidad.getId().toString();
        this.nombre = entidad.getNombre();
        this.genero = entidad.getGenero();
        this.votosRanking = entidad.getVotosRanking();
        this.ultimaSincronizacion = entidad.getUltimaSincronizacion();
        // Los campos de la API se quedan null o vacíos en el ranking
    }

    // Constructor de conversión
    public ArtistaDTO(ArtistaExterno externo) {
        this.id = externo.idArtist;
        this.nombre = externo.strArtist;
        this.genero = externo.strGenre;
        this.biografia = (externo.strBiographyES != null && !externo.strBiographyES.isEmpty())
                ? externo.strBiographyES : externo.strBiographyEN;
        this.foto = externo.strArtistThumb;
        this.web = externo.strWebsite;
    }



}