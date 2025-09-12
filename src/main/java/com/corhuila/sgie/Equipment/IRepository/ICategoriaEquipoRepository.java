package com.corhuila.sgie.Equipment.IRepository;

import com.corhuila.sgie.Equipment.Entity.CategoriaEquipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaEquipoRepository extends IBaseRepository<CategoriaEquipo,Long> {
}
