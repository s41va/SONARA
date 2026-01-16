package com.dawm.sonara.daos;


import com.dawm.sonara.entities.Genero;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Transactional
public class GeneroDAOImple implements GeneroDAO{

    private static final Logger logger = LoggerFactory.getLogger(GeneroDAOImple.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Genero> listAllGeneros() {
        logger.info("Entrando al metodo Listar los Generos");
        String hql = "SELECT g FROM Genero g";
        List<Genero> ge = entityManager.createQuery(hql, Genero.class).getResultList();
        logger.info("{} genero encontrados",ge.size() );
        return ge;
    }

    @Override
    public void insertGenero(Genero genero) {
        logger.info("Entrando al metodo insertar un Genero");
        entityManager.persist(genero);
        logger.info("Genero insertado correctamente");

    }

    @Override
    public void deleteGenero(long id) {
        logger.info("Eliminando Genero con id: {}", id);
        Genero g = entityManager.find(Genero.class, id);
        if (g != null){
            entityManager.remove(g);
            logger.info("Genero con id {} eliminado correctamente.", id);
        }else{
            logger.warn("Genero con id:{} no encontrado", id);
        }
    }

    @Override
    public void updateGenero(Genero genero) {
        logger.info("Actualizando genero con id: {}", genero.getId());
        entityManager.merge(genero);
        logger.info("Genero actualizado correctamente");
    }

    @Override
    public Genero getGeneroById(Long id) {
        logger.info("Obteniendo genero con id: {}", id);
        Genero genero = entityManager.find(Genero.class, id);
        if (genero != null){
            logger.info("Genero encontrado con id: {} - {}", genero.getId(), genero.getId());

        }else{
            logger.warn("Genero con id {} no encontrado. ", genero.getId());
        }

        return genero;
    }

    @Override
    public Genero getGeneroByName(String name) {
        logger.info("Obteniendo el genero con nombre: {}", name);
        String hql = "SELECT g FROM Genero g WHERE g.nombre = :name"; // Usa HQL/JPQL
        try {
            Genero genero = entityManager.createQuery(hql, Genero.class)
                    .setParameter("nombre", name)
                    .getSingleResult();
            logger.info("Género {} encontrado.", genero.getNombre());
            return genero;
        } catch (jakarta.persistence.NoResultException e) {
            logger.warn("Género {} no encontrado", name);
            return null; // Devuelve null si no se encuentra
        }

    }

    @Override
    public Boolean existsGeneroByName(String name) {
        logger.info("Obteniendo genero con nombre", name);
        String hql = "SELECT COUNT(g) FROM Genero g WHERE g.nombre = :name";
        Long count =entityManager.createQuery(hql, Long.class)
                .setParameter("name", name)
                .getSingleResult();

        boolean exists = count != null && count > 0;

        logger.info("Genero con nombre: {} existe: {}", name, exists);
        return exists;
    }

    @Override
    public Boolean existsGeneroByNameAndNotId(String name, Long id) {
        logger.info(" Entrando al metodo existsGeneroByNameAndNotId para nombre: {} excluyendo ID: {}", name, id);
        String hql = "SELECT COUNT(g) FROM Genero g WHERE UPPER(a.nombre) = :name AND g.id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("nombre", name.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info(" Genero con nombre : {} existe incluyendo: {}: {}", name, id, exists);
        return exists;
    }
}
