package com.dawm.sonara.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora los campos del JSON que no usemos
public class ArtistaExterno {
    public String idArtist;
    public String strArtist;
    public String strBiographyES;
    public String strBiographyEN;
    public String strArtistThumb;
    public String strGenre;
    public String strWebsite;
    public String intFormedYear;
}
