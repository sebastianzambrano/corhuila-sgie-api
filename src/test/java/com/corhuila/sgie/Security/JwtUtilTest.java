package com.corhuila.sgie.Security;

import com.corhuila.sgie.User.IRepository.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "test-secret-12345678901234567890123456789012");
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);
        jwtUtil.init();
    }

    @Test
    void generateTokenIncluyeInformacionBasica() {
        User principal = new User("demo@mail.com", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        String token = jwtUtil.generateToken(5L, principal.getUsername(), principal.getAuthorities());

        assertThat(token).isNotBlank();
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("demo@mail.com");
        assertThat(jwtUtil.extractUserId(token)).isEqualTo(5L);
        assertThat(jwtUtil.validateToken(token, principal)).isTrue();
    }

    @Test
    void validateTokenFallaCuandoUsuarioNoCoincide() {
        User principal = new User("otro@mail.com", "pass", List.of());
        String token = jwtUtil.generateToken(1L, "demo@mail.com", List.of());

        assertThat(jwtUtil.validateToken(token, principal)).isFalse();
    }
}
