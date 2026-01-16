package com.dawm.sonara.daos;

import com.dawm.sonara.entities.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioDAO {
    List<Usuario> listAllUsuarios();
    List<Usuario> listUsuariosPage(int page, int size, String sortField, String sortDir);
    long countUsuarios();
    void insertUsuario(Usuario usuario);
    void updateUsuario(Usuario usuario);
    void deleteUsuario(Long id);
    Usuario getUsuarioById(Long id);
    boolean existUsuarioByEmail(String email);
    boolean existUsuarioByEmailAndNotId(String email, Long id);
}
