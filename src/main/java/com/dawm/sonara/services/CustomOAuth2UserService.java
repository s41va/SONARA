package com.dawm.sonara.services;

import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.entities.UsuarioPerfil;
import com.dawm.sonara.repositories.RolesRepository;
import com.dawm.sonara.repositories.UsuarioProfileRepository;
import com.dawm.sonara.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioProfileRepository perfilRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    @Transactional // Importante para que se guarde todo o nada
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        // Buscamos si existe, si no, ejecutamos la lógica de creación
        usuarioRepository.findByEmail(email).orElseGet(() -> {
            // 1. Crear el objeto Usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setNombre(oAuth2User.getAttribute("given_name"));
            nuevoUsuario.setEnabled(true);
            nuevoUsuario.setFechaRegistro(LocalDateTime.now());

            // 2. ASIGNAR ROL POR DEFECTO (Busca el rol 'ROLE_USER' en tu DB)
            rolesRepository.findByName("ROLE_USER").ifPresent(rol -> {
                nuevoUsuario.getRoles().add(rol);
            });

            // 3. Guardar Usuario para obtener el ID
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

            // 4. Crear su Perfil vinculado
            UsuarioPerfil perfil = new UsuarioPerfil();
            perfil.setUsuario(usuarioGuardado);
            perfil.setFirstName(oAuth2User.getAttribute("given_name"));
            perfil.setLastName(oAuth2User.getAttribute("family_name"));
            perfil.setProfileImage(oAuth2User.getAttribute("picture"));
            perfil.setLocale("es");

            perfilRepository.save(perfil);

            return usuarioGuardado;
        });

        return oAuth2User;
    }
}