package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "contrasenaHash", nullable = false, length = 100)
    private String contrasenaHash;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidad;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Roles> roles = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_generos_favoritos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "genero_favorito", length = 50)
    private Set<String> generosFavoritos;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_artistas_favoritos_ids", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "artista_externo_id")
    private Set<String> artistasFavoritosIds = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_canciones_favoritas_ids", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "cancion_externa_id")
    private Set<String> cancionesFavoritasIds = new HashSet<>();

}