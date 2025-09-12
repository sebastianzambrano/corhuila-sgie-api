package com.corhuila.sgie.User.Entity;

import com.corhuila.sgie.common.Auditoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "persona")
public class Persona extends Auditoria {

    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroIdentificacion;
    private String telefonoMovil;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @OneToOne(mappedBy = "persona", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Usuario usuario;

}
