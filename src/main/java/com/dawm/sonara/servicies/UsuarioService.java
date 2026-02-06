package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.usuario.UsuarioCreateDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDetailDTO;
import com.dawm.sonara.dtos.usuario.UsuarioUpdateDTO;
import com.dawm.sonara.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {
    Page<UsuarioDTO> list(Pageable pageable);
    UsuarioUpdateDTO getForEdit(Long id);
    void create(UsuarioCreateDTO dto);
    void update(UsuarioUpdateDTO dto);
    void delete(Long id);
    UsuarioDetailDTO getDetail(Long id);
    List<Role> findAllRoles();
}
