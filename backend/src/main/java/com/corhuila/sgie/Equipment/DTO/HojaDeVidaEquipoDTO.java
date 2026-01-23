package com.corhuila.sgie.Equipment.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HojaDeVidaEquipoDTO {
    private EquipoDTO equipo;
    private List<ReservaEquipoHistorialDTO> reservas;
    private List<MantenimientoEquipoHistorialDTO> mantenimientos;
    private String estadoActual;
}
