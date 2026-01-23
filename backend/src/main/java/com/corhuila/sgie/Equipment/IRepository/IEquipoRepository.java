package com.corhuila.sgie.Equipment.IRepository;

import com.corhuila.sgie.Equipment.DTO.EquipoReporteDTO;
import com.corhuila.sgie.Equipment.DTO.IEquipoInstalacionDTO;
import com.corhuila.sgie.Equipment.Entity.Equipo;
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
public interface IEquipoRepository extends IBaseRepository<Equipo, Long> {
    @Query(value = """
            SELECT 
                eq.id AS idEquipo,
                eq.codigo AS codigoEquipo, 
                te.nombre AS nombreEquipo, 
                eq.state  AS estadoEquipo, 
                ins.nombre AS nombreInstalacion, 
                ins.state  AS estadoInstalacion, 
                ca.nombre  AS nombreCampus, 
                ca.state   AS estadoCampus,
                te.id_categoria_equipo AS idCategoriaEquipo,
                ce.nombre AS nombreCategoriaEquipo
            FROM equipo eq
            INNER JOIN instalacion ins ON eq.id_instalacion = ins.id
            INNER JOIN campus ca ON ins.id_campus = ca.id
            INNER JOIN tipo_equipo te ON eq.id_tipo_equipo = te.id
            INNER JOIN categoria_equipo ce ON te.id_categoria_equipo = ce.id
            WHERE (:codigoEquipo IS NULL OR :codigoEquipo = '' OR eq.codigo = :codigoEquipo)
              AND (:nombreInstalacion IS NULL OR :nombreInstalacion = '' OR ins.nombre = :nombreInstalacion)
            """, nativeQuery = true)
    List<IEquipoInstalacionDTO> findEquiposInstalaciones(
            @Param("codigoEquipo") String codigoEquipo,
            @Param("nombreInstalacion") String nombreInstalacion
    );

    @Query("""
            SELECT new com.corhuila.sgie.Equipment.DTO.EquipoReporteDTO(
                eq.id,
                eq.codigo,
                te.nombre,
                eq.state,
                ins.nombre,
                ce.nombre,
                ca.nombre
            )
            FROM Equipo eq
            JOIN eq.instalacion ins
            JOIN ins.campus ca
            JOIN eq.tipoEquipo te
            JOIN te.categoriaEquipo ce
            """)
    @QueryHints(@QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "1000"))
    Stream<EquipoReporteDTO> generarReporteEquipos();
}
