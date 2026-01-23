package com.corhuila.sgie.Maintenance.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CerrarMantenimientoEquipoResponseDTO {
    private Long id;
    private Boolean state;
    private LocalDate fechaProximaMantenimiento;
    private String resultadoMantenimiento;
    private LocalDateTime updatedAt;
    private Long idReserva;
}
