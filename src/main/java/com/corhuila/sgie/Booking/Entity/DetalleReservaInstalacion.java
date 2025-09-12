package com.corhuila.sgie.Booking.Entity;

import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.Auditoria;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detalle_servicio_instalacion")
public class DetalleReservaInstalacion extends Auditoria {
    private String programaAcademico;
    private Short numeroEstudiantes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_instalacion")
    @JsonBackReference
    private Instalacion instalacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_reserva")
    @JsonBackReference
    private Reserva reserva;

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleReservaInstalacion)) return false;
        DetalleReservaInstalacion that = (DetalleReservaInstalacion) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
