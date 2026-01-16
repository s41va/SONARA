package com.dawm.sonara.daos;


import com.dawm.sonara.entities.Artista;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class ArtistaDAOImple implements ArtistaDAO{

    private static final Logger logger = LoggerFactory.getLogger(ArtistaDAOImple.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Artista> listAllArtist() {
        logger.info("Entrando en el metodo lista Artistas:");
        String hql = "SELECT a FROM Artista a";
        List<Artista> ar = entityManager.createQuery(hql, Artista.class).getResultList();
        logger.info("{} artista encontrados. ", ar.size());
        return ar;
    }

    @Override
    public void insertArtista(Artista ar) {
        logger.info("Entrando al metodo insertar un Artista");
        entityManager.persist(ar);
        logger.info("Artista insertado correctamente");
    }

    @Override
    public void deleteArtista(long id) {
        logger.info("Eliminando Artista con id: {}", id);
        Artista a = entityManager.find(Artista.class, id);
        if (a != null){
            entityManager.remove(a);
            logger.info("Artista con id {} eliminado correctamente.", id);
        }else{
            logger.warn("Artista con id:{} no encontrado", id);
        }
    }

    @Override
    public void updateArtista(Artista ar) {
        logger.info("Actualizando artista con id: {}", ar.getArtista_id());
        entityManager.merge(ar);
        logger.info("Artista actualizado correctamente");
    }

    @Override
    public Artista getArtistaById(Long id) {
        logger.info("Obteniendo artista con id: {}", id);
        Artista artista = entityManager.find(Artista.class, id);
        if (artista != null){
            logger.info("Artista encontrado con id: {} - {}", artista.getArtista_id(), artista.getNombre_artistico());

        }else{
            logger.warn("Artista con id {} no encontrado. ", artista.getArtista_id());
        }

        return artista;

    }

    @Override
    public Artista getArtistaByName(String name) {
        logger.info("Obteniendo el artista con nombre: {}", name);
        Artista artista = entityManager.find(Artista.class, name);
        if (artista != null){
            logger.info("Artistas {} encontrado.", artista.getNombre_artistico());

        }else {
            logger.warn("Artista {} no encontrado", name);
        }
        return artista;
    }

    @Override
    public Boolean existsArtistaByName(String name) {
        logger.info("Obteniendo artista con nombre", name);
        String hql = "SELECT COUNT(a) FROM Artista a WHERE a.nombre_artistico = :name";
        Long count =entityManager.createQuery(hql, Long.class)
                .setParameter("name", name)
                .getSingleResult();

        boolean exists = count != null && count > 0;

        logger.info("Artista con nombre artistico: {} existe: {}", name, exists);
        return exists;

    }

    @Override
    public Boolean existsArtistaByNameAndNotId(String name, Long id) {
        logger.info(" Entrando al metodo existsArtistaByNameAndNotId para nombre artistico: {} excluyendo ID: {}", name, id);
        String hql = "SELECT COUNT(u) FROM Artista a WHERE UPPER(a.nombre_artistico) = :name AND u.artista_id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("name", name.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info(" Artista con nombre artistico: {} existe incluyendo: {}: {}", name, id, exists);
        return exists;
    }


}
