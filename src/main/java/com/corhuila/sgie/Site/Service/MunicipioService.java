package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Municipio;
import com.corhuila.sgie.Site.IRepository.IMunicipioRepository;
import com.corhuila.sgie.Site.IService.IMunicipioService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class MunicipioService extends BaseService<Municipio> implements IMunicipioService {

    private final IMunicipioRepository repository;

    public MunicipioService(IMunicipioRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Municipio, Long> getRepository() {
        return repository;
    }
}
