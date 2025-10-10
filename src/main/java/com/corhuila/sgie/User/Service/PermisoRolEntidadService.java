package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.DTO.IPermisoPorPersonaDTO;
import com.corhuila.sgie.User.DTO.IPermisoRolEntidadDTO;
import com.corhuila.sgie.User.Entity.PermisoRolEntidad;
import com.corhuila.sgie.User.IRepository.IPermisoRolEntidadRepository;
import com.corhuila.sgie.User.IService.IPermisoRolEntidadService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermisoRolEntidadService extends BaseService<PermisoRolEntidad> implements IPermisoRolEntidadService {
    @Autowired
    private IPermisoRolEntidadRepository repository;

    @Override
    protected IBaseRepository<PermisoRolEntidad, Long> getRepository() {
        return repository;
    }

    public List<IPermisoPorPersonaDTO> obtenerPermisos(String numeroIdentificacion) {
        return repository.findPermisosPorNumeroIdentificacion(numeroIdentificacion);
    }
    public List<IPermisoRolEntidadDTO> findPermisosByRolByEntidad() {
        return repository.findPermisosByRolByEntidad();
    }

}
