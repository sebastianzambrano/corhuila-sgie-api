package com.corhuila.sgie.Site.Entity;

import com.corhuila.sgie.common.Auditoria;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "instalacion")
public class Instalacion extends Auditoria {

    private String nombre;
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_campus")
    @JsonBackReference
    private Campus campus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_instalacion_id")
    @JsonBackReference
    private CategoriaInstalacion categoriaInstalacion;

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instalacion)) return false;
        Instalacion that = (Instalacion) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
