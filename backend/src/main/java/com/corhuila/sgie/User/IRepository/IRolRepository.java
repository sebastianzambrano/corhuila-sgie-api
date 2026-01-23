package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.Entity.Rol;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolRepository extends IBaseRepository<Rol, Long> {
}
