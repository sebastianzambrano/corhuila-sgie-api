package com.corhuila.sgie.Maintenance.Entity;

import com.corhuila.sgie.Booking.Entity.Reserva;
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
@Table(name = "mantenimiento_instalacion")
public class MantenimientoInstalacion extends Auditoria {
    private String descripcion;
    private LocalDate fechaProximaMantenimiento;
    private String resultadoMantenimiento;

    @OneToMany(mappedBy = "mantenimientoInstalacion", fetch = FetchType.EAGER)
    private Set<Reserva> reservas = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria_mantenimiento_instalacion")
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
