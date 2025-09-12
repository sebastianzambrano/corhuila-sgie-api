package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.Site.IRepository.IInstalacionRepository;
import com.corhuila.sgie.Site.IService.IInstalacionService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstalacionService extends BaseService<Instalacion> implements IInstalacionService {
    @Autowired
    private IInstalacionRepository repository;

    @Override
    protected IBaseRepository<Instalacion, Long> getRepository() {
        return repository;
    }
}
