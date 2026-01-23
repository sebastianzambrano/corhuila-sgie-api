package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.DTO.CampusReporteDTO;
import com.corhuila.sgie.Site.Entity.Campus;
import com.corhuila.sgie.Site.IRepository.ICampusRepository;
import com.corhuila.sgie.Site.IService.ICapusService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class CampusService extends BaseService<Campus> implements ICapusService {

    private final ICampusRepository repository;

    public CampusService(ICampusRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<Campus, Long> getRepository() {
        return repository;
    }

    public Supplier<Stream<CampusReporteDTO>> proveedorStream() {
        return repository::generarReporteCampuss;
    }

    @Transactional(readOnly = true)
    public List<CampusReporteDTO> obtenerDatosEnMemoria() {
        Supplier<Stream<CampusReporteDTO>> supplier = proveedorStream();
        try (Stream<CampusReporteDTO> stream = supplier.get()) {
            return stream.toList();
        }
    }
}
