package com.corhuila.sgie.Equipment.DTO;

public interface IEquipoInstalacionDTO {
    Long getIdEquipo();

    String getCodigoEquipo();

    String getNombreEquipo();

    Boolean getEstadoEquipo();

    String getNombreInstalacion();

    Boolean getEstadoInstalacion();

    String getNombreCampus();

    Boolean getEstadoCampus();

    Long getIdCategoriaEquipo();

    String getNombreCategoriaEquipo();
}
