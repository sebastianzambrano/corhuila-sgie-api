package com.corhuila.sgie.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
public class RequestLoggingConfig {

    @Bean
    public OncePerRequestFilter requestIdLoggingFilter() {
        return new OncePerRequestFilter() {
            private final Logger log = LoggerFactory.getLogger("RequestLogger");

            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                String headerRequestId = request.getHeader("X-Request-Id");
                String requestId = (headerRequestId != null && !headerRequestId.isBlank())
                        ? headerRequestId
                        : UUID.randomUUID().toString();
                MDC.put("requestId", requestId);
                response.setHeader("X-Request-Id", requestId);
                long start = System.nanoTime();
                try {
                    log.debug("Procesando {} {}", request.getMethod(), request.getRequestURI());
                    filterChain.doFilter(request, response);
                } finally {
                    long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                    if (log.isDebugEnabled()) {
                        log.debug("Completado {} {} con estado {} en {} ms",
                                request.getMethod(), request.getRequestURI(), response.getStatus(), elapsed);
                    }
                    MDC.remove("requestId");
                }
            }
        };
    }
}
