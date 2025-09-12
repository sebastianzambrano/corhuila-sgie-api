package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.Entity.Rol;
import com.corhuila.sgie.User.IRepository.IRolRepository;
import com.corhuila.sgie.User.IService.IRolService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService extends BaseService<Rol> implements IRolService {
    @Autowired
    private IRolRepository repository;
    @Override
    protected IBaseRepository<Rol, Long> getRepository() {
        return repository;
    }
}
