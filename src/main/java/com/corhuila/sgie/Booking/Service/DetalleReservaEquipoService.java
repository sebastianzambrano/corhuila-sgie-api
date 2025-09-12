package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaEquipoRepository;
import com.corhuila.sgie.Booking.IService.IDetalleReservaEquipoService;

import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleReservaEquipoService extends BaseService<DetalleReservaEquipo> implements IDetalleReservaEquipoService {
    @Autowired
    private IDetalleReservaEquipoRepository repository;
    @Override
    protected IBaseRepository<DetalleReservaEquipo, Long> getRepository() {
        return repository;
    }
}
