package com.corhuila.sgie.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditoriaListener.class)
public abstract class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state")
    private Boolean state;

    @Schema(description = "Fecha de creación del dato")
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de actualización del dato")
    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime updatedAt;

    @Schema(description = "Usuario cración")
    @Column(name = "usuario_creacion")
    private Long createdUser;

    @Schema(description = "Usuario modificación")
    @Column(name = "usuario_modificacion")
    private Long updatedUser;

    @Schema(description = "Usuarios eliminación")
    @Column(name = "usuario_eliminacion")
    private Long deletedUser;

    @Schema(description = "Fecha de eliminación del dato")
    @Column(name = "fecha_eliminacion")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.state = Boolean.TRUE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
