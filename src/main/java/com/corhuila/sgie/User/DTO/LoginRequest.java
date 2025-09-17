package com.corhuila.sgie.User.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
