package com.corhuila.sgie.Booking.Entity;

import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.Auditoria;
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
@Table(name = "detalle_reserva_instalacion")
public class DetalleReservaInstalacion extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String programaAcademico;
    private Short numeroEstudiantes;
    private String entregaInstalacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instalacion")
    private Instalacion instalacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva")
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
