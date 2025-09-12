package com.corhuila.sgie.Site.Entity;

import com.corhuila.sgie.common.Auditoria;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoria_instalacion")
public class CategoriaInstalacion extends Auditoria {

    private String nombre;
    private String descripcion;


    @OneToMany(mappedBy = "categoriaInstalacion",fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Instalacion> instalaciones = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoriaInstalacion)) return false;
        CategoriaInstalacion that = (CategoriaInstalacion) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
