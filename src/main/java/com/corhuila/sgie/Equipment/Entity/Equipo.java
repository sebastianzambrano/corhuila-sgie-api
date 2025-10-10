package com.corhuila.sgie.Equipment.Entity;

import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.Auditoria;
import com.corhuila.sgie.common.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
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

    @Column(unique = true, nullable = false)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instalacion", nullable = false)
    @JsonView(Views.Complete.class)
    private Instalacion instalacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_equipo", nullable = false)
    @JsonView(Views.Complete.class)
    private TipoEquipo tipoEquipo;

    @OneToMany(mappedBy = "equipo",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<DetalleReservaEquipo> detalleReservaEquipos = new HashSet<>();

    // Métodos para la vista Simple
    @JsonView(Views.Simple.class)
    @JsonProperty("nombre")
    public String getNombreTipoEquipo() {
        return tipoEquipo != null ? tipoEquipo.getNombre() : null;
    }

    @JsonView(Views.Simple.class)
    @JsonProperty("instalacionNombre")
    public String getNombreInstalacion() {
        return instalacion != null ? instalacion.getNombre() : null;
    }

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
