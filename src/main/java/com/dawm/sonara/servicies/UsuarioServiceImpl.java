package com.dawm.sonara.servicies;


import com.dawm.sonara.dtos.usuario.UsuarioCreateDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDetailDTO;
import com.dawm.sonara.dtos.usuario.UsuarioUpdateDTO;
import com.dawm.sonara.entities.Rol;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.UsuarioMapper;
import com.dawm.sonara.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Page<UsuarioDTO> list(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(UsuarioMapper::toDTO);
    }

    @Override
    public UsuarioUpdateDTO getForEdit(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("usuario", "id", id));
        return UsuarioMapper.toUpdateDTO(usuario);
    }

    @Override
    public UsuarioDTO create(UsuarioCreateDTO dto) {
        return null;
    }

    @Override
    public UsuarioDTO update(UsuarioUpdateDTO dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public UsuarioDetailDTO getDetail(Long id) {
        return null;
    }

    @Override
    public List<UsuarioDTO> listAll(Sort email) {
        return List.of();
    }

    @Override
    public Usuario findById(Long id) {
        return null;
    }

    @Override
    public List<Rol> findAllRoles() {
        return List.of();
    }
}
