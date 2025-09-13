package com.corhuila.sgie.Maintenance.Entity;

import com.corhuila.sgie.common.Auditoria;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoria_mantenimiento_instalacion")
public class CategoriaMantenimientoInstalacion extends Auditoria {
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "categoriaMantenimientoInstalacion", fetch = FetchType.EAGER)
    private Set<MantenimientoInstalacion> mantenimientoInstalaciones = new HashSet<>();

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoriaMantenimientoInstalacion)) return false;
        CategoriaMantenimientoInstalacion that = (CategoriaMantenimientoInstalacion) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
