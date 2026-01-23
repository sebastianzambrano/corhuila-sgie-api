package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Pais;
import com.corhuila.sgie.Site.IRepository.IPaisRepository;
import com.corhuila.sgie.Site.IService.IPaisService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class PaisService extends BaseService<Pais> implements IPaisService {

    private final IPaisRepository repository;

    public PaisService(IPaisRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Pais, Long> getRepository() {
        return repository;
    }
}
