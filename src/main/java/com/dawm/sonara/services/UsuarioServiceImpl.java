package com.dawm.sonara.services;


import com.dawm.sonara.dtos.usuario.UsuarioCreateDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDetailDTO;
import com.dawm.sonara.dtos.usuario.UsuarioUpdateDTO;
import com.dawm.sonara.entities.Localidad;
import com.dawm.sonara.entities.Roles;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.UsuarioMapper;
import com.dawm.sonara.repositories.LocalidadRepository;
import com.dawm.sonara.repositories.RolesRepository;
import com.dawm.sonara.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UsuarioDTO create(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
        }

        validarFechaNacimiento(dto.getFechaNacimiento());

        Usuario usuario = UsuarioMapper.toEntity(dto);

        usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasenaHash()));

        // Validar Localidad
        Long localidadId = dto.getLocalidadId();
        Localidad localidad = localidadRepository.findById(localidadId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "localidad", "id", localidadId
                ));
        usuario.setLocalidad(localidad);

        Set<Roles> roles = new HashSet<>();
        if (dto.getRolesIds() != null) {
            roles = new HashSet<>(rolesRepository.findAllById(dto.getRolesIds()));
        }

        usuario.setRoles(roles);

        usuario.setFechaRegistro(LocalDateTime.now());

        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioDTO update(UsuarioUpdateDTO dto, Set<Roles> roles) {

        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
            throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
        }
        validarFechaNacimiento(dto.getFechaNacimiento());

        Usuario usuario = usuarioRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", dto.getId()));

        // Validar Localidad
        Long localidadId = dto.getLocalidadId();
        Localidad localidad = localidadRepository.findById(localidadId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "localidad", "id", localidadId
                ));
        usuario.setLocalidad(localidad);

        if (dto.getRolesIds() != null) {
            roles = new HashSet<>(rolesRepository.findAllById(dto.getRolesIds()));
        }
        usuario.setRoles(roles);


        UsuarioMapper.copyToExistingEntity(dto, usuario, roles);

        if (dto.getContrasenaHash() != null && !dto.getContrasenaHash().isBlank()) {
            usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasenaHash()));
        }

        usuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(usuario);
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