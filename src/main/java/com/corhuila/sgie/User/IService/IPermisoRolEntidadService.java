package com.corhuila.sgie.User.IService;

import com.corhuila.sgie.User.DTO.IPermisoPorPersonaDTO;
import com.corhuila.sgie.User.DTO.IPermisoRolEntidadDTO;
import com.corhuila.sgie.User.Entity.PermisoRolEntidad;
import com.corhuila.sgie.common.IBaseService;

import java.util.List;

public interface IPermisoRolEntidadService extends IBaseService<PermisoRolEntidad> {
    List<IPermisoPorPersonaDTO> obtenerPermisos(String numeroIdentificacion);

    List<IPermisoRolEntidadDTO> findPermisosByRolByEntidad();
}
