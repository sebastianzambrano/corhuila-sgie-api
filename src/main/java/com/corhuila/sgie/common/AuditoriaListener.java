package com.corhuila.sgie.common;

import com.corhuila.sgie.Security.JwtUtil;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class AuditoriaListener  {

    private final JwtUtil jwtUtil;

    public AuditoriaListener(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

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
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String token = null;

                // Dependiendo de cómo lo seteaste en tu JwtAuthenticationFilter
                if (auth.getCredentials() instanceof String) {
                    token = (String) auth.getCredentials();
                } else if (auth.getDetails() instanceof String) {
                    token = (String) auth.getDetails();
                }

                if (token != null) {
                    return jwtUtil.extractUserId(token);
                }
            }
        } catch (Exception e) {
            // log.warn("No se pudo obtener el usuario autenticado", e);
        }
        return 0L; // fallback
    }
}
