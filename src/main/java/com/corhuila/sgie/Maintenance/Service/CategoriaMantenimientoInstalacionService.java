package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IRepository.ICategoriaMantenimientoInstalacionRepository;
import com.corhuila.sgie.Maintenance.IService.ICategoriaMantenimientoInstalacionService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriaMantenimientoInstalacionService extends BaseService<CategoriaMantenimientoInstalacion> implements ICategoriaMantenimientoInstalacionService {
    private final ICategoriaMantenimientoInstalacionRepository repository;

    public CategoriaMantenimientoInstalacionService(ICategoriaMantenimientoInstalacionRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<CategoriaMantenimientoInstalacion, Long> getRepository() {
        return repository;
    }
}
