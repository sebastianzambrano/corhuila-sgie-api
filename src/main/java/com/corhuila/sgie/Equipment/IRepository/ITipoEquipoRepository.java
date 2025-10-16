package com.corhuila.sgie.Equipment.IRepository;

import com.corhuila.sgie.Equipment.Entity.TipoEquipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoEquipoRepository extends IBaseRepository<TipoEquipo, Long> {
}
