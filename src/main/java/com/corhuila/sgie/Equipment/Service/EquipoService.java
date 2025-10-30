package com.corhuila.sgie.Equipment.Service;

import com.corhuila.sgie.Equipment.DTO.EquipoReporteDTO;
import com.corhuila.sgie.Equipment.DTO.IEquipoInstalacionDTO;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.IRepository.IEquipoRepository;
import com.corhuila.sgie.Equipment.IService.IEquipoService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class EquipoService extends BaseService<Equipo> implements IEquipoService {
    private final IEquipoRepository repository;

    public EquipoService(IEquipoRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Equipo, Long> getRepository() {
        return repository;
    }

    @Override
    public List<IEquipoInstalacionDTO> findEquiposInstalaciones(String codigoEquipo, String idInstalacion) {
        return repository.findEquiposInstalaciones(codigoEquipo, idInstalacion);
    }

    public Supplier<Stream<EquipoReporteDTO>> proveedorStream() {
        return repository::generarReporteEquipos;
    }

    @Transactional(readOnly = true)
    public List<EquipoReporteDTO> obtenerDatosEnMemoria() {
        Supplier<Stream<EquipoReporteDTO>> supplier = proveedorStream();
        try (Stream<EquipoReporteDTO> stream = supplier.get()) {
            return stream.toList();
        }
    }
}
