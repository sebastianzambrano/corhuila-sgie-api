package com.corhuila.sgie.Maintenance.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CerrarMantenimientoInstalacionDTO {
    private LocalDate fechaProximaMantenimiento;
    private String resultadoMantenimiento;
}
