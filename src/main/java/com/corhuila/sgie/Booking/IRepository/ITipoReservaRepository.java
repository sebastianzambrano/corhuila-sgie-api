package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.Entity.TipoReserva;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoReservaRepository extends IBaseRepository<TipoReserva,Long> {
}
