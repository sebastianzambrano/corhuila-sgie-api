package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoEquipo;
import com.corhuila.sgie.Maintenance.IRepository.ICategoriaMantenimientoEquipoRepository;
import com.corhuila.sgie.Maintenance.IService.ICategoriaMantenimientoEquipoService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriaMantenimientoEquipoService extends BaseService<CategoriaMantenimientoEquipo> implements ICategoriaMantenimientoEquipoService {
    private final ICategoriaMantenimientoEquipoRepository repository;

    public CategoriaMantenimientoEquipoService(ICategoriaMantenimientoEquipoRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<CategoriaMantenimientoEquipo, Long> getRepository() {
        return repository;
    }
}
