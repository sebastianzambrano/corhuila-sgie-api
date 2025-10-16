package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.Entity.Permiso;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermisoRepository extends IBaseRepository<Permiso, Long> {
}
