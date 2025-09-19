package com.corhuila.sgie.Maintenance.IService;

import com.corhuila.sgie.Maintenance.DTO.IMantenimientoInstalacionDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.common.IBaseService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMantenimientoInstalacionService extends IBaseService<MantenimientoInstalacion> {
    List<IMantenimientoInstalacionDTO> findMantenimientosInstalacionByNumeroIdentificacion(
            @Param("numeroIdentificacionPersona") String numeroIdentificacionPersona
    );
}
