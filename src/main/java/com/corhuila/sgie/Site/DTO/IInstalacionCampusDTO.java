package com.corhuila.sgie.Site.DTO;

public interface IInstalacionCampusDTO {
    Long getIdContinente();
    String getNombreContinente();
    Long getIdPais();
    String getNombrePais();
    Long getIdDepartamento();
    String getNombreDepartamento();
    Long getIdMunicipio();
    String getNombreMunicipio();
    Long getIdCampus();
    String getNombreCampus();
    Long getIdInstalacion();
    String getNombreInstalacion();
    String getDescripcionInstalacion();
    Long getIdCategoriaInstalacion();
    String getNombreCategoriaInstalacion();
    String getDescripcionCampus();
    Boolean getEstadoInstalacion();
}
