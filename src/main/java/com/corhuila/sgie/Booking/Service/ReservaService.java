package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IReservaService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService extends BaseService<Reserva> implements IReservaService {
    @Autowired
    private IReservaRepository repository;
    @Override
    protected IBaseRepository<Reserva, Long> getRepository() {
        return repository;
    }
}
