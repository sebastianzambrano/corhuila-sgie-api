package com.corhuila.sgie.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt.cookie")
public class JwtCookieProperties {

    /**
     * Nombre de la cookie que transporta el JWT.
     */
    private String name = "token";

    /**
     * Define si la cookie debe marcarse como segura (HTTPS obligatorio).
     */
    private boolean secure = false;

    /**
     * Política SameSite para la cookie (None, Lax, Strict).
     */
    private String sameSite = "Lax";

    /**
     * Tiempo de vida en segundos.
     */
    private long maxAgeSeconds = 3600L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getSameSite() {
        return sameSite;
    }

    public void setSameSite(String sameSite) {
        this.sameSite = sameSite;
    }

    public long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public void setMaxAgeSeconds(long maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
    }
}
