package com.corhuila.sgie.User.Controller;

import com.corhuila.sgie.Security.JwtUtil;
import com.corhuila.sgie.User.DTO.LoginRequest;
import com.corhuila.sgie.User.Entity.Usuario;
import com.corhuila.sgie.User.IRepository.IUsuarioRepository;
import com.corhuila.sgie.User.IService.IUsuarioService;
import com.corhuila.sgie.User.Service.UsuarioService;
import com.corhuila.sgie.common.BaseController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/api/usuario")
public class UsuarioController extends BaseController<Usuario, IUsuarioService> {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final IUsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public UsuarioController(IUsuarioService service,
                             AuthenticationManager authManager,
                             UserDetailsService userDetailsService,
                             JwtUtil jwtUtil,
                             IUsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        super(service, "USUARIO");
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest req) {

        // Autenticación
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        // Obtener UserDetails
        UserDetails ud = userDetailsService.loadUserByUsername(req.getEmail());

        // Obtener usuario desde DB para capturar idUsuario
        Usuario usuario = usuarioRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Generar token pasando idUsuario explícitamente
        String token = jwtUtil.generateToken(usuario.getId(), ud.getUsername(), ud.getAuthorities());

        // Extraer roles y permisos como antes
        List<String> roles = ud.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .collect(Collectors.toList());

        List<String> permisos = ud.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .collect(Collectors.toList());

        // Construir respuesta
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("email", usuario.getEmail());
        resp.put("idUsuario", usuario.getId()); // opcional, útil para frontend
        resp.put("roles", roles);
        resp.put("permisos", permisos);
        return resp;
    }
}
