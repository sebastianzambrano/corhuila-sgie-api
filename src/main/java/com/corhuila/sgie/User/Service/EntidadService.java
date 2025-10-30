package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.Entity.Entidad;
import com.corhuila.sgie.User.IRepository.IEntidadRepository;
import com.corhuila.sgie.User.IService.IEntidadService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class EntidadService extends BaseService<Entidad> implements IEntidadService {
    private final IEntidadRepository repository;

    public EntidadService(IEntidadRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Entidad, Long> getRepository() {
        return repository;
    }
}
