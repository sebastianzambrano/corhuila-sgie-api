package com.corhuila.sgie.Equipment.Service;

import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.IRepository.IEquipoRepository;
import com.corhuila.sgie.Equipment.IService.IEquipoService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipoService extends BaseService<Equipo> implements IEquipoService {
    @Autowired
    private IEquipoRepository repository;
    @Override
    protected IBaseRepository<Equipo, Long> getRepository() {
        return repository;
    }
}
