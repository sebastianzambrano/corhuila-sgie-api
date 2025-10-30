package com.corhuila.sgie.Security;

import com.corhuila.sgie.Config.JwtCookieProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * Expone el token CSRF en una cookie accesible por el frontend (no httpOnly)
 * y lo reenvía en un header para facilitar su consumo desde SPA.
 */
public class CsrfCookieFilter extends OncePerRequestFilter {

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private final String headerName;

    private final JwtCookieProperties jwtCookieProperties;

    public CsrfCookieFilter(JwtCookieProperties jwtCookieProperties, String headerName) {
        this.jwtCookieProperties = jwtCookieProperties;
        this.headerName = headerName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null && csrfToken.getToken() != null) {
            String token = csrfToken.getToken();

            Cookie existingCookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
            boolean shouldAddCookie = existingCookie == null || !Objects.equals(existingCookie.getValue(), token);

            if (shouldAddCookie) {
                ResponseCookie cookie = ResponseCookie.from(CSRF_COOKIE_NAME, token)
                        .httpOnly(false)
                        .secure(resolveSecureFlag(request))
                        .sameSite(jwtCookieProperties.getSameSite())
                        .path("/")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }

            response.setHeader(headerName, token);
        }

        filterChain.doFilter(request, response);
    }

    private boolean resolveSecureFlag(HttpServletRequest request) {
        return jwtCookieProperties.isSecure() || request.isSecure();
    }
}
