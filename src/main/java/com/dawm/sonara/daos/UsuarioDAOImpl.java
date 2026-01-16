package com.dawm.sonara.daos;

import com.dawm.sonara.entities.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UsuarioDAOImpl implements UsuarioDAO {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Usuario> listAllUsuarios() {
        logger.info("Listando todos los usuarios de la base de datos.");
        String hql = "SELECT u FROM Usuario u";
        List<Usuario> usuarios = entityManager.createQuery(hql, Usuario.class).getResultList();
        logger.info("Devueltos {} usuarios.", usuarios.size());
        return usuarios;
    }

    @Override
    public List<Usuario> listUsuariosPage(int page, int size, String sortField, String sortDir) {
        logger.info("Listando usuarios page={}, size={}, sortField={}, sortDir={}.", page, size, sortField, sortDir);
        int offset = page * size;
        //1. Construccion de criteria
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
        Root<Usuario> root = cq.from(Usuario.class);
        //2. Determinar el campo de ordenacion permitido
        Path<?> sortPath;
        switch (sortField) {
            case "id" -> sortPath = root.get("id");
            case "nombre" -> sortPath = root.get("nombre");
            case "email" -> sortPath = root.get("email");
            case "fechaNacimiento" -> sortPath = root.get("fechaNacimiento");
            case "fechaRegistro" -> sortPath = root.get("fechaRegistro");
            default -> {
                logger.warn("Campo desconocido '{}', usando predeterminado 'nombre'.", sortField);
                sortPath = root.get("nombre");
            }
        }
        //3. Direccion de ordenacion
        boolean descending = "desc".equalsIgnoreCase(sortDir);
        //cb.desc y cb.asc sn funciones predefinidas de criteria para las ordenaciones
        Order order = descending ? cb.desc(sortPath) : cb.asc(sortPath);
        //4. Aplicar ordenacion a la query
        cq.select(root).orderBy(order);
        //5. Crear TypedQuery, aplicar paginacion y ejecutar
        return entityManager.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public long countUsuarios() {
        String hql = "SELECT COUNT(u) FROM Usuario u";
        Long total = entityManager.createQuery(hql, Long.class).getSingleResult();
        return (total != null) ? total : 0L;
    }

    @Override
    public void insertUsuario(Usuario usuario) {
        logger.info("Insertando usuario con nombre: {}, email: {}", usuario.getNombre(), usuario.getEmail());
        entityManager.persist(usuario);
        logger.info("Usuario insertado con id: {}", usuario.getId());
    }

    @Override
    public void updateUsuario(Usuario usuario) {
        logger.info("Actualizando usuario con id: {}", usuario.getId());
        entityManager.merge(usuario);
        logger.info("Usuario actualizado con id: {}", usuario.getId());
    }

    @Override
    public void deleteUsuario(Long id) {
        logger.info("Eliminando usuario con id: {}", id);
        Usuario usuario = entityManager.find(Usuario.class, id);
        if (usuario != null) {
            entityManager.remove(usuario);
            logger.info("Usuario eliminado con id: {}", id);
        } else {
            logger.warn("No se encontró usuario con id: {}", id);
        }
    }

    @Override
    public Usuario getUsuarioById(Long id) {
        logger.info("Buscando usuario con id: {}", id);
        Usuario usuario = entityManager.find(Usuario.class, id);
        if (usuario != null) {
            logger.info("Usuario encontrado: {} - {}", usuario.getNombre(), usuario.getEmail());
        } else {
            logger.warn("No se encontró usuario con id: {}", id);
        }
        return usuario;
    }

    @Override
    public boolean existUsuarioByEmail(String email) {
        logger.info("Comprobando existencia de usuario con email: {}", email);
        String hql = "SELECT COUNT(u) FROM Usuario u WHERE UPPER(u.email) = :email";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("email", email.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Usuario con email {} existe: {}", email, exists);
        return exists;
    }

    @Override
    public boolean existUsuarioByEmailAndNotId(String email, Long id) {
        logger.info("Comprobando existencia de usuario con email: {} excluyendo id: {}", email, id);
        String hql = "SELECT COUNT(u) FROM Usuario u WHERE UPPER(u.email) = :email AND u.id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("email", email.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Usuario con email {} existe excluyendo id {}: {}", email, id, exists);
        return exists;
    }
}
