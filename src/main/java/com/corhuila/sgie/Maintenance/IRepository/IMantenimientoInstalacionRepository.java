package com.corhuila.sgie.Maintenance.IRepository;

import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMantenimientoInstalacionRepository extends IBaseRepository<MantenimientoInstalacion, Long> {
}
