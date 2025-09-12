package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.User.IRepository.IPersonaRepository;
import com.corhuila.sgie.User.IService.IPersonaService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaService extends BaseService<Persona> implements IPersonaService {
    @Autowired
    private IPersonaRepository repository;
    @Override
    protected IBaseRepository<Persona, Long> getRepository() {
        return repository;
    }
}
