package com.corhuila.sgie.User.Entity;

import com.corhuila.sgie.common.Auditoria;
import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
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

