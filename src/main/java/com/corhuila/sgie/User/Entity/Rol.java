package com.corhuila.sgie.User.Entity;

import com.corhuila.sgie.common.Auditoria;
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
@Table(name = "rol")
public class Rol extends Auditoria {

    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "rol", fetch = FetchType.EAGER)
    private Set<PermisoRol> permisosRol = new HashSet<>();

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol)) return false;
        Rol that = (Rol) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
