package com.corhuila.sgie.Booking.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IReservaEquipoDTO {
    String getTipoReserva();
    String getNombreReserva();
    String getNombrePersona();
    String getNumeroIdentificacion();
    String getNombreInstalacion();
    LocalDate getFechaReserva();
    LocalTime getHoraInicioReserva();
    LocalTime getHoraFinReserva();
    String getNombreEquipo();
    String getEstadoReserva();
    String getEstadoDetalleReservaInstalacion();
}
