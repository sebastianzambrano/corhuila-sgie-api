package com.corhuila.sgie.Booking.IService;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.DTO.IReservaGeneralDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.common.IBaseService;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IReservaService extends IBaseService<Reserva> {
    List<HoraDisponibleDTO> getHorasDisponiblesInstalacion(LocalDate fecha, Integer idInstalacion, Long idDetalle);

    List<HoraDisponibleDTO> getHorasDisponiblesEquipo(LocalDate fecha, Integer idEquipo, Long idDetalle);

    List<IReservaGeneralDTO> findReservasYMantenimientosByNumeroIdentificacion(String numeroIdentificacion);
}
