package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservaRepository extends IBaseRepository<Reserva,Long> {
}
