package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Pais;
import com.corhuila.sgie.Site.IRepository.IPaisRepository;
import com.corhuila.sgie.Site.IService.IPaisService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaisService extends BaseService<Pais> implements IPaisService {
    @Autowired
    private IPaisRepository repository;

    @Override
    protected IBaseRepository<Pais, Long> getRepository() {
        return repository;
    }
}
