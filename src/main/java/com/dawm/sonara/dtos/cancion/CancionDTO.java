package com.dawm.sonara.dtos.cancion;

import com.dawm.sonara.response.CancionExterna;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancionDTO {
    private String id;
    private String titulo;
    private String artista;
    private String album;
    private String descripcion;
    private String portada;
    private String urlVideo;

    // Constructor de conversión
    public CancionDTO(CancionExterna externo) {
        this.id = externo.idTrack;
        this.titulo = externo.strTrack;
        this.artista = externo.strArtist;
        this.album = externo.strAlbum;
        this.descripcion = externo.strDescriptionEN;
        this.portada = externo.strTrackThumb;
        this.urlVideo = externo.strMusicVid;
    }
}