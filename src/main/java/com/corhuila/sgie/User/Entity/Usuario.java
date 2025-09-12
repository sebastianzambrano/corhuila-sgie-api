package com.corhuila.sgie.User.Entity;

import com.corhuila.sgie.common.Auditoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario extends Auditoria {

    private String email;
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona", unique = true)
    private Persona persona;

}

