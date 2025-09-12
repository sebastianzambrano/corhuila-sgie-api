package com.corhuila.sgie.Booking.Entity;

import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.common.Auditoria;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva extends Auditoria {

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "reserva",fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<DetalleReservaInstalacion> detalleReservaInstalaciones = new HashSet<>();

    @OneToMany(mappedBy = "reserva",fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<DetalleReservaEquipo> detalleReservaEquipos = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona")
    @JsonBackReference
    private Persona persona;


    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva that = (Reserva) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
