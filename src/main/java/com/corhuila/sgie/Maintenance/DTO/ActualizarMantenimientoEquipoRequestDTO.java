package com.corhuila.sgie.Maintenance.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarMantenimientoEquipoRequestDTO {
    private String nombreReserva;
    private String descripcionReserva;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    private String descripcion;
    private LocalDate fechaProximaMantenimiento;
    private String resultadoMantenimiento;
    private Long idEquipo;
}
