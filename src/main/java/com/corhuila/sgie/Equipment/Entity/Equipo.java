package com.corhuila.sgie.Equipment.Entity;

import com.corhuila.sgie.User.Entity.Permiso;
import com.corhuila.sgie.User.Entity.Rol;
import com.corhuila.sgie.common.Auditoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "equipo")
public class Equipo extends Auditoria {

    private String codigo;
    private String nombre;
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoriaEquipo")
    private CategoriaEquipo categoriaEquipo;

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
