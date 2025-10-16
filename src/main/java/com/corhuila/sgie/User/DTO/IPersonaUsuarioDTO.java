package com.corhuila.sgie.User.DTO;

public interface IPersonaUsuarioDTO {
    Long getIdPersona();

    Long getIdUsuario();

    String getTipoDocumento();

    String getNumeroIdentificacion();

    String getNombres();

    String getApellidos();

    String getEmail();

    String getRol();

    String getTelefonoMovil();

    Boolean getEstado();
}
