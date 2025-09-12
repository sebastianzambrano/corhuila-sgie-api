package com.corhuila.sgie.Equipment.IRepository;

import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEquipoRepository extends IBaseRepository<Equipo,Long> {
}
