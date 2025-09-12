package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaInstalacionRepository;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleReservaInstalacionService extends BaseService<DetalleReservaInstalacion> implements IDetalleReservaInstalacionService {
    @Autowired
    private IDetalleReservaInstalacionRepository repository;
    @Override
    protected IBaseRepository<DetalleReservaInstalacion, Long> getRepository() {
        return repository;
    }
}
