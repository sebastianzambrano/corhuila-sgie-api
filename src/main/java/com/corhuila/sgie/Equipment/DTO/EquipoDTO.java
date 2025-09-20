package com.corhuila.sgie.Equipment.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipoDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String instalacion;
    private String categoria;
}
