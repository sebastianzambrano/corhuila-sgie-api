package com.corhuila.sgie.Maintenance.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IMantenimientoEquipoDTO {
    String getTipoReserva();
    String getNombreReserva();
    String getNombrePersona();
    String getNumeroIdentificacionPersona();
    LocalDate getFechaReserva();      // Si es DATE en BD, mejor LocalDate
    LocalTime getHoraInicioReserva(); // Si es TIME, mejor LocalTime
    LocalTime getHoraFinReserva();    // idem
    String getNombreEquipo();
    String getTipoMantenimiento();
    String getEstadoMantenimiento();
    String getEstadoReserva();
}
