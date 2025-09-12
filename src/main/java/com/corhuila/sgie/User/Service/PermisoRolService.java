package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.Entity.PermisoRol;
import com.corhuila.sgie.User.IRepository.IPermisoRolRepository;
import com.corhuila.sgie.User.IService.IPermisoRolService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermisoRolService extends BaseService<PermisoRol> implements IPermisoRolService {
    @Autowired
    private IPermisoRolRepository repository;
    @Override
    protected IBaseRepository<PermisoRol, Long> getRepository() {
        return repository;
    }
}
