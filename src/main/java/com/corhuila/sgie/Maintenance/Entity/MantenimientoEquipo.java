package com.corhuila.sgie.Maintenance.Entity;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.common.Auditoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mantenimiento_equipo")
public class MantenimientoEquipo extends Auditoria {
    private String descripcion;
    private LocalDate fechaProximaMantenimiento;
    private String resultadoMantenimiento;

    @OneToMany(mappedBy = "mantenimientoEquipo", fetch = FetchType.EAGER)
    private Set<Reserva> reservas = new HashSet<>();

    @OneToMany(mappedBy = "mantenimientoEquipo", fetch = FetchType.EAGER)
    private Set<Equipo> equipos = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria_mantenimiento_equipo")
    private CategoriaMantenimientoEquipo categoriaMantenimientoEquipo;

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MantenimientoEquipo)) return false;
        MantenimientoEquipo that = (MantenimientoEquipo) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
