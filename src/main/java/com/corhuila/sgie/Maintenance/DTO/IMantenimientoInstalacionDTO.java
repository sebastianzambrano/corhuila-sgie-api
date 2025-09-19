package com.corhuila.sgie.Maintenance.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IMantenimientoInstalacionDTO {
    String getTipoReserva();
    String getNombreReserva();
    String getNombrePersona();
    String getNumeroIdentificacionPersona();
    String getNombreInstalacion();
    LocalDate getFechaReserva();      // si en BD es DATE => LocalDate
    LocalTime getHoraInicioReserva(); // si en BD es TIME => LocalTime
    LocalTime getHoraFinReserva();    // idem
    String getTipoMantenimiento();
}
