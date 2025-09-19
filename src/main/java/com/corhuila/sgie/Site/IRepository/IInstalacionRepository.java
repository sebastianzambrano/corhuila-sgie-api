package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.DTO.IInstalacionCampusDTO;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInstalacionRepository extends IBaseRepository<Instalacion, Long> {
    @Query(value = """
        SELECT 
            ins.nombre AS nombreInstalacion, 
            ins.state  AS estadoInstalacion, 
            ca.nombre  AS nombreCampus, 
            ca.state   AS estadoCampus
        FROM instalacion ins
        JOIN campus ca ON ins.id_campus = ca.id
        WHERE (:nombreInstalacion IS NULL OR :nombreInstalacion = '' OR ins.nombre = :nombreInstalacion)
          AND (:nombreCampus       IS NULL OR :nombreCampus       = '' OR ca.nombre  = :nombreCampus)
        """, nativeQuery = true)
    List<IInstalacionCampusDTO> findInstalacionesCampus(
            @Param("nombreInstalacion") String nombreInstalacion,
            @Param("nombreCampus") String nombreCampus
    );
}
