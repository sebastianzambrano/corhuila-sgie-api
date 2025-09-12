package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.Entity.PermisoRol;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPermisoRolRepository extends IBaseRepository<PermisoRol, Long> {
    List<PermisoRol> findByRolIdAndStateTrue(Long rolId);
}
