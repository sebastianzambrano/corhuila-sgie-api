package com.corhuila.sgie.Booking.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IReservaGeneralDTO {

    Long getIdReserva();

    Long getIdDetalleRerservaEquipo();

    Long getIdDetalleRerservaInstalacion();

    Long getIdMantenimientoEquipo();

    Long getIdMantenimientoInstalacion();

    Long getIdTipoReserva();

    String getTipoReserva();

    String getNombreReserva();

    String getDescripcionReserva();

    LocalDate getFechaReserva();

    LocalTime getHoraInicioReserva();

    LocalTime getHoraFinReserva();

    Long getIdPersona();

    String getNombrePersona();

    String getNumeroIdentificacion();

    Long getIdInstalacion();

    String getNombreInstalacion();

    Long getIdEquipo();

    String getNombreEquipo();

    String getProgramaAcademico();

    Integer getNumeroEstudiantes();

    Long getIdInstalacionDestino();

    String getTipoMantenimiento();

    String getDescripcionMantenimiento();

    Long getIdCategoriaMantenimiento();

    String getEstadoMantenimiento();

    String getEstadoReserva();

    String getEstadoDetalle();
}
