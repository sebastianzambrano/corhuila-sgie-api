package com.corhuila.sgie.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class AuditoriaListenerTest {

    private final AuditoriaListener listener = new AuditoriaListener();

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void setCreatedUserTomaIdDelContexto() {
        TestingAuthenticationToken auth = new TestingAuthenticationToken("user", null);
        auth.setDetails(42L);
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        DummyAuditoria auditoria = new DummyAuditoria();
        listener.setCreatedUser(auditoria);

        assertThat(auditoria.getCreatedUser()).isEqualTo(42L);
    }

    @Test
    void setUpdatedUserRegistraUsuario() {
        TestingAuthenticationToken auth = new TestingAuthenticationToken("user", null);
        auth.setDetails(7L);
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        DummyAuditoria auditoria = new DummyAuditoria();
        listener.setUpdatedUser(auditoria);

        assertThat(auditoria.getUpdatedUser()).isEqualTo(7L);
    }

    @Test
    void setDeletedUserMarcaFechaYUsuario() {
        TestingAuthenticationToken auth = new TestingAuthenticationToken("user", null);
        auth.setDetails(5L);
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        DummyAuditoria auditoria = new DummyAuditoria();
        listener.setDeletedUser(auditoria);

        assertThat(auditoria.getDeletedUser()).isEqualTo(5L);
        assertThat(auditoria.getDeletedAt()).isNotNull();
    }

    private static class DummyAuditoria extends Auditoria {
        private Long id = 1L;

        public Long getId() {
            return id;
        }
    }
}
