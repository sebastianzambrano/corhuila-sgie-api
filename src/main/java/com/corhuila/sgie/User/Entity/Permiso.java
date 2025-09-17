package com.corhuila.sgie.User.Entity;

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
@Table(name = "permiso")
public class Permiso extends Auditoria {

    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "permiso", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<PermisoRol> permisosRol = new HashSet<>();

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permiso)) return false;
        Permiso that = (Permiso) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

