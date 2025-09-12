package com.corhuila.sgie.Equipment.Service;

import com.corhuila.sgie.Equipment.Entity.CategoriaEquipo;
import com.corhuila.sgie.Equipment.IRepository.ICategoriaEquipoRepository;
import com.corhuila.sgie.Equipment.IService.ICategoriaEquipoService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaEquipoService extends BaseService<CategoriaEquipo> implements ICategoriaEquipoService {
    @Autowired
    private ICategoriaEquipoRepository repository;

    @Override
    protected IBaseRepository<CategoriaEquipo, Long> getRepository() {
        return repository;
    }
}
