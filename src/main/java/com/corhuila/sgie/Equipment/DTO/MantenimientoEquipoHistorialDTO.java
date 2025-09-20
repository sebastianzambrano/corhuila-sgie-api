package com.corhuila.sgie.Equipment.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MantenimientoEquipoHistorialDTO {
    private LocalDate fechaProximaMantenimiento;
    private String descripcion;
    private String resultadoMantenimiento;
    private String categoriaMantenimiento;
}
