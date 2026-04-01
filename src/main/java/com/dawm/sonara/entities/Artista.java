package com.dawm.sonara.entities;

import com.dawm.sonara.response.ArtistaExterno;

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

    // Getters y Setters...
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getBiografia() { return biografia; }
    public String getFoto() { return foto; }
    public String getWeb() { return web; }
}