package com.dawm.sonara.entities;

import com.dawm.sonara.response.CancionExterna;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Cancion {
    private String id;
    private String titulo;
    private String album;
    private String artista;
    private String genero;
    private String videoUrl;
    private String portada;

    public Cancion(CancionExterna ext) {
        this.id = ext.idArtist; // La API a veces cruza IDs, pero idTrack es el correcto
        this.titulo = ext.strTrack;
        this.album = ext.strAlbum;
        this.artista = ext.strArtist;
        this.genero = ext.strGenre;
        this.videoUrl = ext.strMusicVid;
        this.portada = ext.strTrackThumb;
    }

    // Getters y Setters...
}