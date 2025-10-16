package com.corhuila.sgie.Booking.DTO;

import com.corhuila.sgie.common.Reporting.ReportColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaGeneralReporteDTO {
    @ReportColumn(header = "ID Reserva", order = 0)
    private Long idReserva;

    @ReportColumn(header = "ID Detalle Reserva Equipo", order = 1)
    private Long idDetalleRerservaEquipo;

    @ReportColumn(header = "ID Detalle Reserva Instalación", order = 2)
    private Long idDetalleRerservaInstalacion;

    @ReportColumn(header = "ID Mantenimiento Equipo", order = 3)
    private Long idMantenimientoEquipo;

    @ReportColumn(header = "ID Mantenimiento Instalación", order = 4)
    private Long idMantenimientoInstalacion;

    @ReportColumn(header = "ID Tipo Reserva", order = 5)
    private Long idTipoReserva;

    @ReportColumn(header = "Tipo de Reserva", order = 6)
    private String tipoReserva;

    @ReportColumn(header = "Nombre de la Reserva", order = 7)
    private String nombreReserva;

    @ReportColumn(header = "Descripción de la Reserva", order = 8)
    private String descripcionReserva;

    @ReportColumn(header = "Fecha de Reserva", order = 9)
    private java.sql.Date fechaReserva;

    @ReportColumn(header = "Hora Inicio", order = 10)
    private java.sql.Time horaInicioReserva;

    @ReportColumn(header = "Hora Fin", order = 11)
    private java.sql.Time horaFinReserva;

    @ReportColumn(header = "ID Persona", order = 12)
    private Long idPersona;

    @ReportColumn(header = "Nombre Persona", order = 13)
    private String nombrePersona;

    @ReportColumn(header = "N° Identificación", order = 14)
    private String numeroIdentificacion;

    @ReportColumn(header = "ID Instalación", order = 15)
    private Long idInstalacion;

    @ReportColumn(header = "Nombre Instalación", order = 16)
    private String nombreInstalacion;

    @ReportColumn(header = "ID Equipo", order = 17)
    private Long idEquipo;

    @ReportColumn(header = "Nombre Equipo", order = 18)
    private String nombreEquipo;

    @ReportColumn(header = "Programa Académico", order = 19)
    private String programaAcademico;

    @ReportColumn(header = "N° Estudiantes", order = 20)
    private Integer numeroEstudiantes;

    @ReportColumn(header = "ID Instalación Destino", order = 21)
    private Long idInstalacionDestino;

    @ReportColumn(header = "Tipo Mantenimiento", order = 22)
    private String tipoMantenimiento;

    @ReportColumn(header = "Descripción Mantenimiento", order = 23)
    private String descripcionMantenimiento;

    @ReportColumn(header = "ID Categoría Mantenimiento", order = 24)
    private Long idCategoriaMantenimiento;

    @ReportColumn(header = "Estado Mantenimiento", order = 25)
    private String estadoMantenimiento;

    @ReportColumn(header = "Estado Reserva", order = 26)
    private String estadoReserva;

    @ReportColumn(header = "Estado Detalle", order = 27)
    private String estadoDetalle;

}
