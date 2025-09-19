package com.corhuila.sgie.Maintenance.IService;

import com.corhuila.sgie.Maintenance.DTO.IMantenimientoEquipoDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.common.IBaseService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMantenimientoEquipoService extends IBaseService<MantenimientoEquipo> {
    List<IMantenimientoEquipoDTO> findMantenimientosEquipoByNumeroIdentificacion(
            @Param("numeroIdentificacionPersona") String numeroIdentificacionPersona
    );
}
