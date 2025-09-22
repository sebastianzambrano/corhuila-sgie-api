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
public class MantenimientoInstalacionResponseDTO {
    private Long idMantenimiento;
    private String descripcion;
    private LocalDate fechaProximaMantenimiento;
    private String resultadoMantenimiento;

    // Datos de la reserva
    private String nombreReserva;
    private String descripcionReserva;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
