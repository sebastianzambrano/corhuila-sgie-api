package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Departamento;
import com.corhuila.sgie.Site.IRepository.IDepartamentoRepository;
import com.corhuila.sgie.Site.IService.IDepartamentoService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService extends BaseService<Departamento> implements IDepartamentoService {
    @Autowired
    private IDepartamentoRepository repository;

    @Override
    protected IBaseRepository<Departamento, Long> getRepository() {
        return repository;
    }
}
