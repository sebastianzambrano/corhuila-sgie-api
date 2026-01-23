package com.corhuila.sgie.common;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PermissionEvaluatorTest {

    private final PermissionEvaluator evaluator = new PermissionEvaluator();

    @Test
    void hasPermissionRetornaTrueCuandoExisteAuthority() {
        User principal = new User("demo", "pass",
                List.of(new SimpleGrantedAuthority("USUARIO:CONSULTAR")));
        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(principal, null, principal.getAuthorities());
        auth.setAuthenticated(true);

        assertThat(evaluator.hasPermission(auth, "usuario", "consultar")).isTrue();
    }

    @Test
    void hasPermissionRetornaFalseCuandoNoEstaAutenticado() {
        TestingAuthenticationToken auth = new TestingAuthenticationToken("user", null);
        auth.setAuthenticated(false);
        assertThat(evaluator.hasPermission(auth, "usuario", "consultar")).isFalse();
    }

    @Test
    void hasPermissionRetornaFalseCuandoNoExistePermiso() {
        User principal = new User("demo", "pass",
                List.of(new SimpleGrantedAuthority("USUARIO:CREAR")));
        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(principal, null, principal.getAuthorities());
        auth.setAuthenticated(true);

        assertThat(evaluator.hasPermission(auth, "usuario", "consultar")).isFalse();
    }
}
