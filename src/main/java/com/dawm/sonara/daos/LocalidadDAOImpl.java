package com.dawm.sonara.daos;

import com.dawm.sonara.entities.Localidad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public class LocalidadDAOImpl implements LocalidadDAO{

    private static final Logger logger = LoggerFactory.getLogger(LocalidadDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Localidad> listAllLocalidades() {
        logger.info("Listando las localidades de la base de datos.");
        String hql = "SELECT l FROM Localidad l";
        List<Localidad> localidades = entityManager.createQuery(hql, Localidad.class).getResultList();
        logger.info("Devueltas las {} localidades de la base de datos ", localidades.size());
        return localidades;
    }

    @Override
    public List<Localidad> listLocalidadesPage(int page, int size, String sortField, String sortDir) {
        logger.info("Listando localidades page={}, size{}, sortField={}, sortDir={} de la base de datos.",
                page, size, sortField, sortDir);

        int offset = page * size;
        //1. Construccion de criteria
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Localidad> cq = cb.createQuery(Localidad.class);
        Root<Localidad> root = cq.from(Localidad.class);
        //2. Determinar el campo de ordenacion permitido
        Path<?> sortPath;
        switch (sortField) {
            case "id" -> sortPath = root.get("id");
            case "pais" -> sortPath = root.get("pais");
            case "nombreCiudad" -> sortPath = root.get("nombreCiudad");
            case "codigoPostal" -> sortPath = root.get("codigoPostal");
            default -> {
                logger.warn("Campo desconocido '{}', usando predeterminado 'name'.", sortField);
                sortPath = root.get("nombreCiudad");
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
    public long countLocalidades() {
        String hql = "SELECT COUNT(l) FROM Localidad l";
        Long total = entityManager.createQuery(hql, Long.class).getSingleResult();
        return (total!=null) ? total: 0L;
    }

    @Override
    public void insertLocalidad(Localidad localidad) {
        logger.info("Insertando localidad con pais: {}, nombre de ciudad {} y codigo postal {}",
                localidad.getPais(), localidad.getNombreCiudad(), localidad.getCodigoPostal()
        );
        entityManager.persist(localidad);
        logger.info("Localidad insertada con codigo: {}", localidad.getId());
    }

    @Override
    public void updateLocalidad(Localidad localidad) {
        logger.info("Actualizando localidad con id: {}", localidad.getId());
        entityManager.merge(localidad);
        logger.info("Localidad actualizada con ID {}", localidad.getId());
    }

    @Override
    public void deleteLocalidad(Long id) {
        logger.info("Eliminando localidad con id: {}", id);
        Localidad localidad = entityManager.find(Localidad.class, id);
        if (localidad != null) {
            entityManager.remove(localidad);
            logger.info("Localidad eliminada con id: {}", id);
        }
        else{
            logger.info("No se encontro la localidad con id: {} ", id);
        }
    }

    @Override
    public Localidad getLocalidadById(Long id) {
        logger.info("Recogiendo localidad con id: {}", id);
        Localidad localidad = entityManager.find(Localidad.class, id);
        if (localidad != null) {
            logger.info("Localidad encontrada: {} - {} - {}", localidad.getPais(), localidad.getNombreCiudad(), localidad.getCodigoPostal());
        }
        else {
            logger.warn("No se ha encontrado la localidad con id: {}", id);
        }
        return localidad;
    }

    @Override
    public boolean existLocalidadByCodigoPostal(String codigoPostal) {
        logger.info("Comprobando si la localidad con codigo postal: {} exsite", codigoPostal);
        String hql = "SELECT COUNT(l) FROM Localidad l WHERE UPPER(l.codigoPostal) = :codigoPostal";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("codigoPostal", codigoPostal.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Localidad con codigo postal: {} existe: {}", codigoPostal, exists);
        return exists;
    }

    @Override
    public boolean existLocalidadByCodigoPostalAndNotId(String codigoPostal, Long id) {
        logger.info("Comprobando si la localidad con codigo: {} existe excluyendo la id: {}", codigoPostal, id);
        String hql = "SELECT COUNT(l) FROM Localidad l WHERE UPPER(l.codigoPostal) = :codigoPostal AND l.id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("codigoPostal", codigoPostal.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Localidad con codigo postal: {} existe excluyendo la id {}: {}", codigoPostal, id, exists);
        return exists;
    }
}
