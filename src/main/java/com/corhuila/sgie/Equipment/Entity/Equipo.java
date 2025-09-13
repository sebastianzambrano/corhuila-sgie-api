package com.corhuila.sgie.Equipment.Entity;

import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.common.Auditoria;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "equipo")
public class Equipo extends Auditoria {

    private String codigo;
    private String nombre;
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoriaEquipo")
    private CategoriaEquipo categoriaEquipo;

    @OneToMany(mappedBy = "equipo",fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<DetalleReservaEquipo> detalleReservaEquipos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mantenimiento_equipo")
    private MantenimientoEquipo mantenimientoEquipo;

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo)) return false;
        Equipo that = (Equipo) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
