package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.Entity.Entidad;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEntidadRepository extends IBaseRepository<Entidad, Long> {
}
