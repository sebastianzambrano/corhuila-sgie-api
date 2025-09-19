package com.corhuila.sgie.Booking.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IReservaInstalacionDTO {
    String getTipoReserva();
    String getNombreReserva();
    String getNombrePersona();
    String getNumeroIdentificacionPersona();
    String getNombreInstalacion();
    LocalDate getFechaReserva();
    LocalTime getHoraInicioReserva();
    LocalTime getHoraFinReserva();
}
