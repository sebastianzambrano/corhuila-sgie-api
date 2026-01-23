package com.corhuila.sgie.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("permissionEvaluator")
public class PermissionEvaluator {
    public boolean hasPermission(Authentication authentication, String entidad, String accion) {
        if (authentication == null || !authentication.isAuthenticated()) return false;

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String permisoBuscado = entidad.toUpperCase() + ":" + accion.toUpperCase();

        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(p -> p.equals(permisoBuscado));
    }
}
