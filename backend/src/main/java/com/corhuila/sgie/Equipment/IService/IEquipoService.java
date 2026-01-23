package com.corhuila.sgie.Equipment.IService;

import com.corhuila.sgie.Equipment.DTO.IEquipoInstalacionDTO;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.common.IBaseService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEquipoService extends IBaseService<Equipo> {
    List<IEquipoInstalacionDTO> findEquiposInstalaciones(
            @Param("codigoEquipo") String codigoEquipo,
            @Param("nombreInstalacion") String nombreInstalacion
    );
}
