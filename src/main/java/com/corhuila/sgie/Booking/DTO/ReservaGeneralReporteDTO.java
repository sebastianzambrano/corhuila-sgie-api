package com.corhuila.sgie.Booking.DTO;

import com.corhuila.sgie.common.Reporting.ReportColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaGeneralReporteDTO {

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

    @ReportColumn(header = "Nombre Persona", order = 13)
    private String nombrePersona;

    @ReportColumn(header = "N° Identificación", order = 14)
    private String numeroIdentificacion;

    @ReportColumn(header = "Nombre Instalación", order = 16)
    private String nombreInstalacion;

    @ReportColumn(header = "Nombre Equipo", order = 18)
    private String nombreEquipo;

    @ReportColumn(header = "Programa Académico", order = 19)
    private String programaAcademico;

    @ReportColumn(header = "N° Estudiantes", order = 20)
    private Integer numeroEstudiantes;

    @ReportColumn(header = "Tipo Mantenimiento", order = 22)
    private String tipoMantenimiento;

    @ReportColumn(header = "Descripción Mantenimiento", order = 23)
    private String descripcionMantenimiento;

    @ReportColumn(header = "Estado Reserva", order = 26)
    private String estadoReserva;

}
