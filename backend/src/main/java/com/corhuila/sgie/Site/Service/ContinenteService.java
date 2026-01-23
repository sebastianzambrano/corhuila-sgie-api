package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Continente;
import com.corhuila.sgie.Site.IRepository.IContinenteRepository;
import com.corhuila.sgie.Site.IService.IContinenteService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class ContinenteService extends BaseService<Continente> implements IContinenteService {

    private final IContinenteRepository repository;

    public ContinenteService(IContinenteRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Continente, Long> getRepository() {
        return repository;
    }
}
