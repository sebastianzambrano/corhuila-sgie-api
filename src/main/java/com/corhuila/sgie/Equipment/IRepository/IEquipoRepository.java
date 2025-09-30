package com.corhuila.sgie.Equipment.IRepository;

import com.corhuila.sgie.Equipment.DTO.IEquipoInstalacionDTO;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEquipoRepository extends IBaseRepository<Equipo,Long> {
    @Query(value = """
        SELECT 
            eq.codigo AS codigoEquipo, 
            te.nombre AS nombreEquipo, 
            eq.state  AS estadoEquipo, 
            ins.nombre AS nombreInstalacion, 
            ins.state  AS estadoInstalacion, 
            ca.nombre  AS nombreCampus, 
            ca.state   AS estadoCampus
        FROM equipo eq
        INNER JOIN instalacion ins ON eq.id_instalacion = ins.id
        INNER JOIN campus ca ON ins.id_campus = ca.id
        INNER JOIN tipo_equipo te ON eq.id_tipo_equipo = te.id
        WHERE (:codigoEquipo IS NULL OR :codigoEquipo = '' OR eq.codigo = :codigoEquipo)
          AND (:nombreInstalacion IS NULL OR :nombreInstalacion = '' OR ins.nombre = :nombreInstalacion)
        """, nativeQuery = true)
    List<IEquipoInstalacionDTO> findEquiposInstalaciones(
            @Param("codigoEquipo") String codigoEquipo,
            @Param("nombreInstalacion") String nombreInstalacion
    );
}
