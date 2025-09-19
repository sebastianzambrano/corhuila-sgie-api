package com.corhuila.sgie.Site.IService;

import com.corhuila.sgie.Site.DTO.IInstalacionCampusDTO;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.IBaseService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IInstalacionService extends IBaseService<Instalacion> {
    List<IInstalacionCampusDTO> findInstalacionesCampus(
            @Param("nombreInstalacion") String nombreInstalacion,
            @Param("nombreCampus") String nombreCampus
    );
}
