package com.corhuila.sgie.Booking.Entity;

import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.common.Auditoria;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private LocalDate fechaReserva;
    //private LocalDate fechaFin;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaInicio;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaFin;
    private String nombre;
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_reserva", nullable = false)
    private TipoReserva tipoReserva;

    @OneToMany(mappedBy = "reserva",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<DetalleReservaInstalacion> detalleReservaInstalaciones = new HashSet<>();

    @OneToMany(mappedBy = "reserva",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<DetalleReservaEquipo> detalleReservaEquipos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    @JsonIgnore
    private MantenimientoEquipo mantenimientoEquipo;

    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    @JsonIgnore
    private MantenimientoInstalacion mantenimientoInstalacion;

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
