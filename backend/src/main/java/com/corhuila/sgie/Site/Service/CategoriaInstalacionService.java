package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.CategoriaInstalacion;
import com.corhuila.sgie.Site.IRepository.ICategoriaInstalacionRepository;
import com.corhuila.sgie.Site.IService.ICategoriaInstalacionService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaInstalacionService extends BaseService<CategoriaInstalacion> implements ICategoriaInstalacionService {
    @Autowired
    private ICategoriaInstalacionRepository repository;

    @Override
    protected IBaseRepository<CategoriaInstalacion, Long> getRepository() {
        return repository;
    }
}
