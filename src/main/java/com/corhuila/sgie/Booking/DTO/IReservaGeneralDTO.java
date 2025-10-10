package com.corhuila.sgie.Booking.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IReservaGeneralDTO {
    // Identificadores
    Long getIdReserva();
    Long getIdDetalleRerservaEquipo();
    Long getIdDetalleRerservaInstalacion();
    Long getIdMantenimientoEquipo();
    Long getIdMantenimientoInstalacion();

    // Tipo de reserva
    Long getIdTipoReserva();
    String getTipoReserva();

    // Información general de la reserva
    String getNombreReserva();
    String getDescripcionReserva();
    LocalDate getFechaReserva();
    LocalTime getHoraInicioReserva();
    LocalTime getHoraFinReserva();

    // Persona
    Long getIdPersona();
    String getNombrePersona();
    String getNumeroIdentificacion();

    // Instalación / Equipo
    Long getIdInstalacion();
    String getNombreInstalacion();
    Long getIdEquipo();
    String getNombreEquipo();

    // Información académica
    String getProgramaAcademico();
    Integer getNumeroEstudiantes();
    Long getIdInstalacionDestino();

    // Mantenimiento
    String getTipoMantenimiento();
    String getDescripcionMantenimiento();
    Long getIdCategoriaMantenimiento();
    String getEstadoMantenimiento();

    // Estados
    String getEstadoReserva();
    String getEstadoDetalle();
}
