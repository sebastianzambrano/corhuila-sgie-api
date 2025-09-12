package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleReservaEquipoRepository extends IBaseRepository<DetalleReservaEquipo,Long> {
}
