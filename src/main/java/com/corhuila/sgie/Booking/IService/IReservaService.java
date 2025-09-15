package com.corhuila.sgie.Booking.IService;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.common.IBaseService;

import java.time.LocalDate;
import java.util.List;

public interface IReservaService extends IBaseService<Reserva> {
    List<HoraDisponibleDTO> getHorasDisponiblesInstalacion(LocalDate fecha, Integer idInstalacion);

    List<HoraDisponibleDTO> getHorasDisponiblesEquipo(LocalDate fecha, Integer idEquipo);
}
