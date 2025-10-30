package com.corhuila.sgie.Booking.DTO;

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
public class ActualizarReservaDetalleEquipoRequestDTO {
    private String nombreReserva;
    private String descripcionReserva;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    private String programaAcademico;
    private Short numeroEstudiantes;
    private Long idEquipo;
    private Long idInstalacionDestino;
}
