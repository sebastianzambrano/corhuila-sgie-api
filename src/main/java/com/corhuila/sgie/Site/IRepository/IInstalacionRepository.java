package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.DTO.IInstalacionCampusDTO;
import com.corhuila.sgie.Site.DTO.InstalacionReporteDTO;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.IBaseRepository;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface IInstalacionRepository extends IBaseRepository<Instalacion, Long> {
    // Query corregida + IDs
    @Query(value = """
            SELECT 
                co.id   AS idContinente,
                co.nombre AS nombreContinente,
                pa.id   AS idPais,
                pa.nombre  AS nombrePais,
                de.id   AS idDepartamento,
                de.nombre AS nombreDepartamento,
                mu.id   AS idMunicipio,
                mu.nombre AS nombreMunicipio,
                ca.id   AS idCampus,
                ca.nombre AS nombreCampus,
                ins.id  AS idInstalacion,
                ins.nombre AS nombreInstalacion,
                ins.id_categoria_instalacion AS idCategoriaInstalacion,
                ci.descripcion AS nombreCategoriaInstalacion,
                ins.descripcion AS descripcionInstalacion,
                ins.state  AS estadoInstalacion,
                ca.descripcion AS descripcionCampus
            FROM instalacion ins
            INNER JOIN campus ca         ON ins.id_campus       = ca.id
            INNER JOIN categoria_instalacion ci ON ins.id_categoria_instalacion = ci.id
            INNER JOIN municipio mu      ON ca.id_municipio     = mu.id
            INNER JOIN departamento de   ON mu.id_departamento  = de.id
            INNER JOIN pais pa           ON de.id_pais          = pa.id
            INNER JOIN continente co     ON pa.id_continente    = co.id
            WHERE (:nombreInstalacion IS NULL OR :nombreInstalacion = '' OR ins.nombre = :nombreInstalacion)
              AND (:nombreCampus       IS NULL OR :nombreCampus       = '' OR ca.nombre  = :nombreCampus)
            """, nativeQuery = true)
    List<IInstalacionCampusDTO> findInstalacionesCampus(
            @Param("nombreInstalacion") String nombreInstalacion,
            @Param("nombreCampus") String nombreCampus
    );

    @Query("""
            SELECT new com.corhuila.sgie.Site.DTO.InstalacionReporteDTO(
                ins.id,
                co.nombre,
                pa.nombre,
                de.nombre,
                mu.nombre,
                ca.nombre,
                ins.nombre,
                ci.descripcion
                )
            FROM Instalacion ins
            JOIN ins.campus ca
            JOIN ca.municipio mu
            JOIN mu.departamento de
            JOIN de.pais pa
            JOIN pa.continente co
            JOIN ins.categoriaInstalacion ci
            """)
    @QueryHints(@QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "1000"))
    Stream<InstalacionReporteDTO> generarReporteInstalaciones();
}
