package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.Entity.Rol;
import com.corhuila.sgie.User.IRepository.IRolRepository;
import com.corhuila.sgie.User.IService.IRolService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class RolService extends BaseService<Rol> implements IRolService {
    private final IRolRepository repository;

    public RolService(IRolRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Rol, Long> getRepository() {
        return repository;
    }
}
