package com.dawm.sonara.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CancionResponse {
    @JsonProperty("track")
    private List<CancionExterna> canciones;

    public List<CancionExterna> getCanciones() { return canciones; }
    public void setCanciones(List<CancionExterna> canciones) { this.canciones = canciones; }
}