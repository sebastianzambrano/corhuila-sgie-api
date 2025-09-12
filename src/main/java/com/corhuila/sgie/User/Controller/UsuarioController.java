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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    // Constructor único con las dependencias necesarias
    public UsuarioController(IUsuarioService service,
                             AuthenticationManager authManager,
                             UserDetailsService userDetailsService,
                             JwtUtil jwtUtil,
                             IUsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        super(service, "Usuario");
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        UserDetails ud = userDetailsService.loadUserByUsername(req.getEmail());
        String token = jwtUtil.generateToken(ud);

        Usuario usuario = usuarioRepository.findByEmail(req.getEmail()).orElseThrow();

        List<String> roles = ud.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .collect(Collectors.toList());

        List<String> permisos = ud.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(a -> !a.startsWith("ROLE_"))
                .collect(Collectors.toList());

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("email", usuario.getEmail());
        resp.put("roles", roles);
        resp.put("permisos", permisos);
        return resp;
    }

}
