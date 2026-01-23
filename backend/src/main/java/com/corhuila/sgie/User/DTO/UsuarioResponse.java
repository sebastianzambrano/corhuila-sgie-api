package com.corhuila.sgie.User.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class UsuarioResponse {

    private Long id;
    private String email;
    private Boolean state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long personaId;
    private String personaNombres;
    private String personaApellidos;
    private String personaNumeroIdentificacion;
    private List<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public String getPersonaNombres() {
        return personaNombres;
    }

    public void setPersonaNombres(String personaNombres) {
        this.personaNombres = personaNombres;
    }

    public String getPersonaApellidos() {
        return personaApellidos;
    }

    public void setPersonaApellidos(String personaApellidos) {
        this.personaApellidos = personaApellidos;
    }

    public String getPersonaNumeroIdentificacion() {
        return personaNumeroIdentificacion;
    }

    public void setPersonaNumeroIdentificacion(String personaNumeroIdentificacion) {
        this.personaNumeroIdentificacion = personaNumeroIdentificacion;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
