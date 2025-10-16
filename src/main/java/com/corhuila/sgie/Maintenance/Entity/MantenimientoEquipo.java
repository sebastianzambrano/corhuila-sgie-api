package com.corhuila.sgie.Maintenance.Entity;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.common.Auditoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mantenimiento_equipo")
public class MantenimientoEquipo extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private LocalDate fechaProximaMantenimiento;
    private String resultadoMantenimiento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", unique = true, nullable = false)
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_mantenimiento_equipo", nullable = false)
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
