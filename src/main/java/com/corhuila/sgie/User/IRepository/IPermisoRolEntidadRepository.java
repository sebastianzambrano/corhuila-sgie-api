package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.DTO.IPermisoPorPersonaDTO;
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
        WHERE pe.numero_identificacion = :numeroIdentificacion
        """, nativeQuery = true)
    List<IPermisoPorPersonaDTO> findPermisosPorNumeroIdentificacion(@Param("numeroIdentificacion") String numeroIdentificacion);

}
