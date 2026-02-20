package com.dawm.sonara.servicies;


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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        // Verificar si el email ya está registrado
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
        }

        // Validar la fecha de nacimiento
        validarFechaNacimiento(dto.getFechaNacimiento());

        // Crear la entidad Usuario utilizando el DTO y los roles
        Set<Roles> roles = new HashSet<>();
        if (dto.getRolesIds() != null) {
            roles = new HashSet<>(rolesRepository.findAllById(dto.getRolesIds()));
        }

        // Usar mtodo copyToNewEntity para asignar los valores del DTO a la nueva entidad
        Usuario usuario = UsuarioMapper.copyToNewEntity(dto, roles);

        // Validar y asignar la localidad
        if (dto.getLocalidadId() != null) {
            Long localidadId = dto.getLocalidadId();
            Localidad localidad = localidadRepository.findById(localidadId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "localidad", "id", localidadId
                    ));
            usuario.setLocalidad(localidad);
        }

        // Asignar la fecha de registro
        usuario.setFechaRegistro(LocalDateTime.now());

        // Guardar el usuario en la base de datos
        usuarioRepository.save(usuario);

        // Retornar el DTO del usuario creado
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

        if (dto.getRolesIds() != null) {
            roles = new HashSet<>(rolesRepository.findAllById(dto.getRolesIds()));
        }
        usuario.setRoles(roles);

        // Validar Localidad
        Long localidadId = dto.getLocalidadId();
        Localidad localidad = localidadRepository.findById(localidadId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "localidad", "id", localidadId
                ));
        usuario.setLocalidad(localidad);

        UsuarioMapper.copyToExistingEntity(dto, usuario, roles);

        usuarioRepository.save(usuario);

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
    public List<UsuarioDTO> listAll(Sort sort) {
        return usuarioRepository.findAll(sort)
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