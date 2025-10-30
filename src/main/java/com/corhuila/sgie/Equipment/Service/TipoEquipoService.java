package com.corhuila.sgie.Equipment.Service;

import com.corhuila.sgie.Equipment.Entity.TipoEquipo;
import com.corhuila.sgie.Equipment.IRepository.ITipoEquipoRepository;
import com.corhuila.sgie.Equipment.IService.ITipoEquipoService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoEquipoService extends BaseService<TipoEquipo> implements ITipoEquipoService {
    private final ITipoEquipoRepository repository;

    public TipoEquipoService(ITipoEquipoRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<TipoEquipo, Long> getRepository() {
        return repository;
    }
}
