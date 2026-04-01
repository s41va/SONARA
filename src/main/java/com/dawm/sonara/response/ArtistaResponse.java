package com.dawm.sonara.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArtistaResponse {

    @JsonProperty("artists") // Mapea la clave "artists" del JSON a esta lista
    private List<ArtistaExterno> artistas;

    public List<ArtistaExterno> getArtistas() {
        return artistas;
    }

    public void setArtistas(List<ArtistaExterno> artistas) {
        this.artistas = artistas;
    }
}
