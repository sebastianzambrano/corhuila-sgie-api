package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IReservaService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService extends BaseService<Reserva> implements IReservaService {

    @Autowired
    private IReservaRepository repository;

    @Override
    protected IBaseRepository<Reserva, Long> getRepository() {
        return repository;
    }

    @Override
    public List<HoraDisponibleDTO> getHorasDisponiblesInstalacion(LocalDate fecha, Integer idInstalacion) {
        List<Object[]> results =repository.findHorasDisponiblesInstalacion(fecha, idInstalacion);

        return results.stream()
                .map(r -> new HoraDisponibleDTO(r[0].toString()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HoraDisponibleDTO> getHorasDisponiblesEquipo(LocalDate fecha, Integer idEquipo) {
        List<Object[]> results =repository.findHorasDisponiblesEquipo(fecha, idEquipo);

        return results.stream()
                .map(r -> new HoraDisponibleDTO(r[0].toString()))
                .collect(Collectors.toList());
    }
}
