package com.corhuila.sgie.common;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class AuditoriaListener {

    @PrePersist
    public void setCreatedUser(Auditoria entidad) {
        Long usuarioId = getUsuarioActual();
        entidad.setCreatedUser(usuarioId);

    }

    @PreUpdate
    public void setUpdatedUser(Auditoria entidad) {
        Long usuarioId = getUsuarioActual();
        entidad.setUpdatedUser(usuarioId);
    }

    @PreRemove
    public void setDeletedUser(Auditoria entidad) {
        Long usuarioId = getUsuarioActual();
        entidad.setDeletedUser(usuarioId);
        entidad.setDeletedAt(LocalDateTime.now());
    }

    private Long getUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object details = auth.getDetails();
            if (details instanceof Long longDetails) {
                return longDetails;
            }
        }
        return 0L;
    }
}
