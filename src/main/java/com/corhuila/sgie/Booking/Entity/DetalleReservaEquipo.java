package com.corhuila.sgie.Booking.Entity;

import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.Auditoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detalle_reserva_equipo")
public class DetalleReservaEquipo extends Auditoria {

    private String programaAcademico;
    private Short numeroEstudiantes;
    private String EntregaEquipo;

    @Column(name = "fecha_devolucion_real")
    private LocalDateTime fechaDevolucionReal;

    @Column(name = "observaciones_devolucion", columnDefinition = "TEXT")
    private String observacionesDevolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instalacion_destino")
    private Instalacion instalacionDestino;


    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleReservaEquipo)) return false;
        DetalleReservaEquipo that = (DetalleReservaEquipo) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
