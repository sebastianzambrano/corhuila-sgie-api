package com.corhuila.sgie.Equipment.Service;

import com.corhuila.sgie.Equipment.DTO.IEquipoInstalacionDTO;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.IRepository.IEquipoRepository;
import com.corhuila.sgie.Equipment.IService.IEquipoService;
import com.corhuila.sgie.User.DTO.IPermisoPorPersonaDTO;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class EquipoService extends BaseService<Equipo> implements IEquipoService {
    @Autowired
    private IEquipoRepository repository;
    @Override
    protected IBaseRepository<Equipo, Long> getRepository() {
        return repository;
    }

    @Override
    public List<IEquipoInstalacionDTO> findEquiposInstalaciones(String codigoEquipo, String idInstalacion) {
        return repository.findEquiposInstalaciones(codigoEquipo, idInstalacion);
    }
}
