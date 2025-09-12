package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleReservaInstalacionRepository extends IBaseRepository<DetalleReservaInstalacion,Long> {
}
