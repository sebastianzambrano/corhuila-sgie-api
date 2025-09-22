package com.corhuila.sgie.Equipment.Entity;

import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.Auditoria;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String nombre;
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instalacion", nullable = false)
    private Instalacion instalacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoriaEquipo")
    private CategoriaEquipo categoriaEquipo;

    @OneToMany(mappedBy = "equipo",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<DetalleReservaEquipo> detalleReservaEquipos = new HashSet<>();

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
