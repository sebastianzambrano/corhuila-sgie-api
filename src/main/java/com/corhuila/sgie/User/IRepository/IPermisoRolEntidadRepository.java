package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.DTO.IPermisoPorPersonaDTO;
import com.corhuila.sgie.User.DTO.IPermisoRolEntidadDTO;
import com.corhuila.sgie.User.Entity.PermisoRolEntidad;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPermisoRolEntidadRepository extends IBaseRepository<PermisoRolEntidad,Long> {
    List<PermisoRolEntidad> findByRolIdAndStateTrue(Long rolId);

    @Query(value = """
        SELECT 
            pre.id,        
            pe.nombres, 
            pe.apellidos, 
            e.nombre AS entidad, 
            pm.nombre AS permiso, 
            pre.state AS estado
        FROM persona pe
        INNER JOIN rol r ON pe.id_rol = r.id
        INNER JOIN permiso_rol_entidad pre ON r.id = pre.id_rol
        INNER JOIN entidad e ON pre.id_entidad = e.id
        INNER JOIN permiso pm ON pre.id_permiso = pm.id
        WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
        """, nativeQuery = true)
    List<IPermisoPorPersonaDTO> findPermisosPorNumeroIdentificacion(@Param("numeroIdentificacion") String numeroIdentificacion);

    @Query(value = """
        SELECT
            CONCAT(pre.id, '-', COALESCE(pe.id, 0)) AS unique,
            pre.id,        
            pe.nombres, 
            pe.apellidos, 
            e.nombre AS entidad, 
            pm.nombre AS permiso,
            r.nombre AS rol,
            pre.state AS estado
        FROM permiso_rol_entidad pre
        INNER JOIN rol r ON pre.id_rol = r.id
        INNER JOIN entidad e ON pre.id_entidad = e.id
        INNER JOIN permiso pm ON pre.id_permiso = pm.id
        LEFT JOIN persona pe ON r.id = pe.id_rol
        """, nativeQuery = true)
    List<IPermisoRolEntidadDTO> findPermisosByRolByEntidad();
}
