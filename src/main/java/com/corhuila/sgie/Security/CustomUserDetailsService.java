package com.corhuila.sgie.Security;

import com.corhuila.sgie.User.Entity.PermisoRolEntidad;
import com.corhuila.sgie.User.Entity.Rol;
import com.corhuila.sgie.User.Entity.Usuario;
import com.corhuila.sgie.User.IRepository.IPermisoRolEntidadRepository;
import com.corhuila.sgie.User.IRepository.IUsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;

    private final IPermisoRolEntidadRepository permisoRolEntidadRepository;

    public CustomUserDetailsService(IUsuarioRepository usuarioRepository,
                                    IPermisoRolEntidadRepository permisoRolEntidadRepository) {
        this.usuarioRepository = usuarioRepository;
        this.permisoRolEntidadRepository = permisoRolEntidadRepository;
    }

    @Override
    @Transactional(readOnly = true)
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

        // Permisos dinámicos por entidad
        List<PermisoRolEntidad> permisosRolEntidad = permisoRolEntidadRepository.findByRolIdAndStateTrue(rol.getId());
        if (permisosRolEntidad != null) {
            for (PermisoRolEntidad pre : permisosRolEntidad) {
                if (pre != null && pre.getPermiso() != null && pre.getEntidad() != null) {
                    String authority = pre.getEntidad().getNombre().toUpperCase() + ":"
                            + pre.getPermiso().getNombre().toUpperCase();
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
