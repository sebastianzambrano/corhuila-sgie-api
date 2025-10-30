package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.Entity.TipoReserva;
import com.corhuila.sgie.Booking.IRepository.ITipoReservaRepository;
import com.corhuila.sgie.Booking.IService.ITipoReservaService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoReservaService extends BaseService<TipoReserva> implements ITipoReservaService {

    private final ITipoReservaRepository repository;

    public TipoReservaService(ITipoReservaRepository repository) {
        this.repository = repository;
    }

    @Override
    protected IBaseRepository<TipoReserva, Long> getRepository() {
        return repository;
    }
}
