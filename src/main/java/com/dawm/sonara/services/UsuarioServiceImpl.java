package com.dawm.sonara.services;


import com.dawm.sonara.dtos.usuario.UsuarioCreateDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDetailDTO;
import com.dawm.sonara.dtos.usuario.UsuarioUpdateDTO;
import com.dawm.sonara.entities.Localidad;
import com.dawm.sonara.entities.Roles;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.entities.UsuarioPerfil;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.UsuarioMapper;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.repositories.LocalidadRepository;
import com.dawm.sonara.repositories.RolesRepository;
import com.dawm.sonara.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<UsuarioDTO> list(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(UsuarioMapper::toDTO);
    }

    @Override
    public UsuarioUpdateDTO getForEdit(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return UsuarioMapper.toUpdateDTO(usuario);
    }

    @Override
    @Transactional // IMPORTANTE: Para que si falla el perfil, no se cree el usuario
    public UsuarioDTO create(UsuarioCreateDTO dto) {
        // 1. Validaciones previas
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
        }
        validarFechaNacimiento(dto.getFechaNacimiento());

        // 2. Mapear DTO a Entidad Usuario
        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasenaHash()));
        usuario.setFechaRegistro(LocalDateTime.now());

        // 3. Asignar Localidad
        Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("localidad", "id", dto.getLocalidadId()));
        usuario.setLocalidad(localidad);

        // 4. Asignar Roles
        if (dto.getRolesIds() != null && !dto.getRolesIds().isEmpty()) {
            usuario.setRoles(new HashSet<>(rolesRepository.findAllById(dto.getRolesIds())));
        }

        // 5. CREAR PERFIL PROVISIONAL (La clave del éxito)
        UsuarioPerfil perfil = new UsuarioPerfil();
        perfil.setFirstName(usuario.getNombre());
        perfil.setLastName(""); // Para cumplir con el NOT NULL de la DB sin inventar datos
        perfil.setLocale(org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage());

        // Vinculación bidireccional necesaria para @MapsId y Cascade
        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        // 6. Guardar Usuario (Guardará el perfil automáticamente por CascadeType.ALL)
        usuario = usuarioRepository.save(usuario);

        // 7. Retornar DTO
        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioDTO update(UsuarioUpdateDTO dto, Set<Roles> roles) {
        // 1. Validaciones básicas
        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
            throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
        }
        validarFechaNacimiento(dto.getFechaNacimiento());

        // 2. Buscar usuario
        Usuario usuario = usuarioRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", dto.getId()));

        // 3. Localidad
        Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("localidad", "id", dto.getLocalidadId()));
        usuario.setLocalidad(localidad);

        // 4. Actualizar campos con el Mapper
        // (Importante: copyToExistingEntity ahora también copia los IDs de artistas/canciones)
        UsuarioMapper.copyToExistingEntity(dto, usuario, roles);

        // 5. Password
        if (dto.getContrasenaHash() != null && !dto.getContrasenaHash().isBlank()) {
            usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasenaHash()));
        }

        return UsuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioDetailDTO getDetail(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return UsuarioMapper.toDetailDTO(usuario);
    }

    @Override
    public List<UsuarioDTO> listAll(Sort name) {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }

    private void validarFechaNacimiento(LocalDate fechaNacimiento) {

        LocalDate fechaMinima = LocalDate.of(1900, 1, 1);
        LocalDate hoy = LocalDate.now();

        if (fechaNacimiento.isBefore(fechaMinima) || fechaNacimiento.isAfter(hoy)) {
            throw new IllegalArgumentException("Fecha de nacimiento fuera de rango permitido");
        }
    }
}