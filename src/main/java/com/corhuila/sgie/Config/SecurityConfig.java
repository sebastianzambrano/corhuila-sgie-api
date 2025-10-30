package com.corhuila.sgie.Config;

import com.corhuila.sgie.Security.CsrfCookieFilter;
import com.corhuila.sgie.Security.CustomUserDetailsService;
import com.corhuila.sgie.Security.JwtFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    private static final RequestMatcher OPTIONS_REQUEST_MATCHER = request ->
            "OPTIONS".equalsIgnoreCase(request.getMethod());
    private static final RequestMatcher LOGIN_REQUEST_MATCHER = request ->
            matchesPath(request, "/v1/api/usuario/login");
    private static final RequestMatcher LOGOUT_REQUEST_MATCHER = request ->
            matchesPath(request, "/v1/api/usuario/logout");

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;
    private final JwtCookieProperties jwtCookieProperties;

    @Value("${spring.web.cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JwtFilter jwtFilter,
                          JwtCookieProperties jwtCookieProperties) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtFilter = jwtFilter;
        this.jwtCookieProperties = jwtCookieProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieName("XSRF-TOKEN");
        csrfTokenRepository.setHeaderName(CSRF_HEADER_NAME);
        csrfTokenRepository.setCookiePath("/");
        csrfTokenRepository.setCookieCustomizer(cookie -> cookie
                .secure(jwtCookieProperties.isSecure())
                .sameSite(jwtCookieProperties.getSameSite())
        );

        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null);

        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers(
                                OPTIONS_REQUEST_MATCHER,
                                LOGIN_REQUEST_MATCHER,
                                LOGOUT_REQUEST_MATCHER
                        )
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // rutas públicas
                        .requestMatchers(
                                "/v1/api/usuario/login",
                                "/v1/api/usuario/me",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/api/equipos/reportes/**",
                                "/error"
                        ).permitAll()
                        // todas las demás requieren token
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new CsrfCookieFilter(jwtCookieProperties, CSRF_HEADER_NAME), CsrfFilter.class)
                .build();

    }


    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(parseAllowedOrigins());
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", CSRF_HEADER_NAME));
        config.setExposedHeaders(List.of(CSRF_HEADER_NAME));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private List<String> parseAllowedOrigins() {
        return Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .toList();
    }

    private static boolean matchesPath(HttpServletRequest request, String path) {
        String servletPath = request.getServletPath();
        if (path.equals(servletPath)) {
            return true;
        }
        String requestUri = request.getRequestURI();
        return path.equals(requestUri);
    }
}
