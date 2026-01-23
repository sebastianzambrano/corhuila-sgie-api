package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.DTO.IInstalacionCampusDTO;
import com.corhuila.sgie.Site.DTO.InstalacionReporteDTO;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.Site.IRepository.IInstalacionRepository;
import com.corhuila.sgie.Site.IService.IInstalacionService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class InstalacionService extends BaseService<Instalacion> implements IInstalacionService {

    private final IInstalacionRepository repository;

    public InstalacionService(IInstalacionRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Instalacion, Long> getRepository() {
        return repository;
    }

    @Override
    public List<IInstalacionCampusDTO> findInstalacionesCampus(String nombreInstalacion, String nombreCampus) {
        return repository.findInstalacionesCampus(nombreInstalacion, nombreCampus);
    }

    public Supplier<Stream<InstalacionReporteDTO>> proveedorStream() {
        return repository::generarReporteInstalaciones;
    }

    @Transactional(readOnly = true)
    public List<InstalacionReporteDTO> obtenerDatosEnMemoria() {
        Supplier<Stream<InstalacionReporteDTO>> supplier = proveedorStream();
        try (Stream<InstalacionReporteDTO> stream = supplier.get()) {
            return stream.toList();
        }
    }

}
