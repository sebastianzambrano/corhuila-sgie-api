package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.DTO.IPersonaUsuarioDTO;
import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.User.IRepository.IPersonaRepository;
import com.corhuila.sgie.User.IService.IPersonaService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService extends BaseService<Persona> implements IPersonaService {
    private final IPersonaRepository repository;

    public PersonaService(IPersonaRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Persona, Long> getRepository() {
        return repository;
    }

    @Override
    public List<IPersonaUsuarioDTO> findUsuariosPersonaPorIdentificacion(String numeroIdentificacion) {
        return repository.findUsuariosPersonaPorIdentificacion(numeroIdentificacion);
    }
}
