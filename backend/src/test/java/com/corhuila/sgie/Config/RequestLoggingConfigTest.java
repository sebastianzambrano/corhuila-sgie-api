package com.corhuila.sgie.Config;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLoggingConfigTest {

    private final RequestLoggingConfig config = new RequestLoggingConfig();

    @Test
    void reutilizaRequestIdDelHeader() throws ServletException, IOException {
        OncePerRequestFilter filter = config.requestIdLoggingFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/demo");
        request.addHeader("X-Request-Id", "abc-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getHeader("X-Request-Id")).isEqualTo("abc-123");
    }

    @Test
    void generaRequestIdCuandoNoExiste() throws ServletException, IOException {
        OncePerRequestFilter filter = config.requestIdLoggingFilter();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(new MockHttpServletRequest("GET", "/demo"), response, new MockFilterChain());

        assertThat(response.getHeader("X-Request-Id")).isNotBlank();
    }
}
