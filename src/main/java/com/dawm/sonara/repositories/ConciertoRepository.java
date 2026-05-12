package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Concierto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConciertoRepository extends JpaRepository<Concierto, Long> {
    @Override
    Optional<Concierto> findById(Long id);

    @Query("SELECT c FROM Concierto c WHERE " +
            "(:name IS NULL OR LOWER(c.artistaNombre) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:date IS NULL OR c.fechaHora = :date) AND " +
            "(:location IS NULL OR LOWER(c.localidad.nombreCiudad) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Concierto> findByFilters(
            @Param("name") String name,
            @Param("date") Date date,
            @Param("location") String location,
            Pageable pageable
    );
}
