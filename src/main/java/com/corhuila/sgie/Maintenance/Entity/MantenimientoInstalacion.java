package com.corhuila.sgie.Maintenance.Entity;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.Auditoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mantenimiento_instalacion")
public class MantenimientoInstalacion extends Auditoria {
    private String descripcion;
    private LocalDate fechaProximaMantenimiento;
    private String resultadoMantenimiento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", unique = true, nullable = false)
    private Reserva reserva; // Una reserva = Un mantenimiento

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instalacion", nullable = false)
    private Instalacion instalacion; // QUÉ instalación se mantiene

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_mantenimiento_instalacion", nullable = false)
    private CategoriaMantenimientoInstalacion categoriaMantenimientoInstalacion;

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MantenimientoInstalacion)) return false;
        MantenimientoInstalacion that = (MantenimientoInstalacion) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
