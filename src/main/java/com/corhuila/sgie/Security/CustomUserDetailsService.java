package com.corhuila.sgie.Security;

import com.corhuila.sgie.User.Entity.*;
import com.corhuila.sgie.User.IRepository.IPermisoRolRepository;
import com.corhuila.sgie.User.IRepository.IUsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;
    private final IPermisoRolRepository permisoRolRepository;

    public CustomUserDetailsService(IUsuarioRepository usuarioRepository,
                                    IPermisoRolRepository permisoRolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.permisoRolRepository = permisoRolRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(">>> loadUserByUsername llamado con: " + email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        System.out.println(">>> Usuario encontrado: " + usuario.getEmail());

        Rol rol = usuario.getPersona() != null ? usuario.getPersona().getRol() : null;
        if (rol == null) {
            throw new UsernameNotFoundException("El usuario no tiene rol asignado");
        }

        System.out.println(">>> Rol encontrado: " + rol.getNombre());

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Rol principal
        authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre().toUpperCase()));

        // Permisos asociados al rol
        List<PermisoRol> permisosRol = permisoRolRepository.findByRolIdAndStateTrue(rol.getId());
        if (permisosRol != null) {
            for (PermisoRol pr : permisosRol) {
                if (pr != null && pr.getPermiso() != null && pr.getPermiso().getNombre() != null) {
                    String authority = "PERM_" + pr.getPermiso().getNombre().toUpperCase().trim();
                    authorities.add(new SimpleGrantedAuthority(authority));
                }
            }
        }

        System.out.println(">>> Authorities finales: " + authorities);

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountLocked(false)
                .disabled(false)
                .build();
    }
}
