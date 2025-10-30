package com.corhuila.sgie.Security;

import com.corhuila.sgie.Config.JwtCookieProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class CsrfCookieFilterTest {

    private JwtCookieProperties properties;
    private CsrfCookieFilter filter;

    @BeforeEach
    void setup() {
        properties = new JwtCookieProperties();
        properties.setSameSite("Lax");
        properties.setSecure(false);
        filter = new CsrfCookieFilter(properties, "X-XSRF-TOKEN");
    }

    @Test
    void agregaCookieCuandoTokenNuevo() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CsrfToken token = new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf", "abc");
        request.setAttribute(CsrfToken.class.getName(), token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getHeader("X-XSRF-TOKEN")).isEqualTo("abc");
        assertThat(response.getHeaders("Set-Cookie")).anyMatch(value -> value.contains("XSRF-TOKEN=abc"));
    }

    @Test
    void noRegeneraCookieSiValorNoCambio() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("XSRF-TOKEN", "abc"));
        CsrfToken token = new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf", "abc");
        request.setAttribute(CsrfToken.class.getName(), token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = Mockito.mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getHeaders("Set-Cookie")).isEmpty();
        assertThat(response.getHeader("X-XSRF-TOKEN")).isEqualTo("abc");
    }
}
