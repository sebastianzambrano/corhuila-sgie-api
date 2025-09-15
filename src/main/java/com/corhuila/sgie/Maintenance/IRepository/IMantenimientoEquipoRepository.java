package com.corhuila.sgie.Maintenance.IRepository;

import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMantenimientoEquipoRepository extends IBaseRepository <MantenimientoEquipo,Long> {
}
