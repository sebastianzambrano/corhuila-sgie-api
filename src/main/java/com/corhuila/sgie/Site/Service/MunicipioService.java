package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Municipio;
import com.corhuila.sgie.Site.IRepository.IMunicipioRepository;
import com.corhuila.sgie.Site.IService.IMunicipioService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MunicipioService extends BaseService<Municipio> implements IMunicipioService {
    @Autowired
    private IMunicipioRepository repository;

    @Override
    protected IBaseRepository<Municipio, Long> getRepository() {
        return repository;
    }
}
