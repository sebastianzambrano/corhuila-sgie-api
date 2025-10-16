package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.Entity.Permiso;
import com.corhuila.sgie.User.IRepository.IPermisoRepository;
import com.corhuila.sgie.User.IService.IPermisoService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermisoService extends BaseService<Permiso> implements IPermisoService {
    @Autowired
    private IPermisoRepository repository;

    @Override
    protected IBaseRepository<Permiso, Long> getRepository() {
        return repository;
    }
}
