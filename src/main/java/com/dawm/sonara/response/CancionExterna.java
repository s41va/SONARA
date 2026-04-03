package com.dawm.sonara.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CancionExterna {
    public String idTrack;
    public String strTrack;
    public String strAlbum;
    public String strArtist;
    public String strGenre;
    public String strDescriptionEN;
    public String strTrackThumb; // Portada de la canción (si hay)
    public String strMusicVid;   // Link de YouTube
}