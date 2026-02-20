package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.usuario.UsuarioCreateDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDetailDTO;
import com.dawm.sonara.dtos.usuario.UsuarioUpdateDTO;
import com.dawm.sonara.entities.Roles;
import com.dawm.sonara.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

public interface UsuarioService {
    Page<UsuarioDTO> list(Pageable pageable);
    UsuarioUpdateDTO getForEdit(Long id);
    UsuarioDTO create(UsuarioCreateDTO dto);
    UsuarioDTO update(UsuarioUpdateDTO dto, Set<Roles> roles);
    void delete(Long id);
    UsuarioDetailDTO getDetail(Long id);

    Page<UsuarioDTO> listAll(Pageable pageable);
}
