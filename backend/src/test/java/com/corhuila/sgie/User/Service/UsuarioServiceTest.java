package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.User.Entity.Rol;
import com.corhuila.sgie.User.Entity.Usuario;
import com.corhuila.sgie.User.IRepository.IPersonaRepository;
import com.corhuila.sgie.User.IRepository.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private IUsuarioRepository usuarioRepository;
    @Mock
    private IPersonaRepository personaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Persona persona;

    @BeforeEach
    void setUp() {
        Rol rol = new Rol();
        rol.setId(9L);
        rol.setNombre("ADMIN");

        persona = new Persona();
        persona.setId(5L);
        persona.setRol(rol);
        persona.setNumeroIdentificacion("123");
    }

    @Test
    void saveDebeCodificarPasswordYAsociarPersona() throws Exception {
        Usuario request = new Usuario();
        request.setEmail("demo@mail.com");
        request.setPassword("plain");
        request.setPersona(persona);

        when(personaRepository.findById(persona.getId())).thenReturn(Optional.of(persona));
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        Usuario saved = usuarioService.save(request);

        assertThat(saved.getId()).isEqualTo(10L);
        assertThat(saved.getPassword()).isEqualTo("encoded");
        assertThat(persona.getUsuario()).isSameAs(saved);
        verify(usuarioRepository).save(saved);
    }

    @Test
    void saveDebeFallarCuandoPersonaYaTieneUsuario() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        persona.setUsuario(existente);

        Usuario request = new Usuario();
        request.setPersona(persona);

        when(personaRepository.findById(persona.getId())).thenReturn(Optional.of(persona));

        assertThatThrownBy(() -> usuarioService.save(request))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("ya tiene un usuario");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void updateDebeActualizarDatosBasicos() throws Exception {
        Usuario original = new Usuario();
        original.setId(20L);
        original.setEmail("old@mail.com");
        original.setPersona(persona);

        Usuario cambios = new Usuario();
        cambios.setEmail("new@mail.com");
        cambios.setPassword("newpass");
        cambios.setPersona(persona);

        when(usuarioRepository.findById(20L)).thenReturn(Optional.of(original));
        when(personaRepository.findById(persona.getId())).thenReturn(Optional.of(persona));
        when(passwordEncoder.encode("newpass")).thenReturn("encoded");

        usuarioService.update(20L, cambios);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario actualizado = captor.getValue();

        assertThat(actualizado.getEmail()).isEqualTo("new@mail.com");
        assertThat(actualizado.getPassword()).isEqualTo("encoded");
    }

    @Test
    void updateDebeFallarCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.update(999L, new Usuario()))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void saveDebeFallarCuandoPersonaEsNull() {
        Usuario request = new Usuario();
        request.setEmail("test@mail.com");
        request.setPassword("pwd");

        assertThatThrownBy(() -> usuarioService.save(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("persona asociada es obligatoria");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void saveDebeFallarCuandoPersonaNoTieneId() {
        Usuario request = new Usuario();
        request.setPersona(new Persona());
        request.setPassword("pwd");

        assertThatThrownBy(() -> usuarioService.save(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("persona asociada es obligatoria");
    }

    @Test
    void saveDebeFallarCuandoPersonaNoExiste() {
        Usuario request = new Usuario();
        Persona ref = new Persona();
        ref.setId(99L);
        request.setPersona(ref);
        request.setPassword("pwd");

        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.save(request))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("La persona no existe");
    }

    @Test
    void updateDebeFallarCuandoUsuarioInhabilitado() {
        Usuario existente = new Usuario();
        existente.setId(30L);
        existente.setDeletedAt(java.time.LocalDateTime.now());

        when(usuarioRepository.findById(30L)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> usuarioService.update(30L, new Usuario()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Usuario inhabilitado");
    }

    @Test
    void updateDebeFallarCuandoPersonaIdEsNull() {
        Usuario existente = new Usuario();
        existente.setId(40L);
        when(usuarioRepository.findById(40L)).thenReturn(Optional.of(existente));

        Usuario cambios = new Usuario();
        Persona personaNueva = new Persona();
        cambios.setPersona(personaNueva);

        assertThatThrownBy(() -> usuarioService.update(40L, cambios))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("persona asociada es obligatoria");
    }

    @Test
    void updateDebeFallarCuandoPersonaNoExiste() {
        Usuario existente = new Usuario();
        existente.setId(41L);
        when(usuarioRepository.findById(41L)).thenReturn(Optional.of(existente));

        Persona ref = new Persona();
        ref.setId(77L);

        Usuario cambios = new Usuario();
        cambios.setPersona(ref);

        when(personaRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.update(41L, cambios))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("La persona no existe");
    }

    @Test
    void updateDebeFallarCuandoPersonaTieneOtroUsuario() throws Exception {
        Usuario existente = new Usuario();
        existente.setId(42L);
        existente.setPersona(persona);

        when(usuarioRepository.findById(42L)).thenReturn(Optional.of(existente));

        Persona otraPersona = new Persona();
        otraPersona.setId(88L);
        Usuario asociado = new Usuario();
        asociado.setId(999L);
        otraPersona.setUsuario(asociado);

        when(personaRepository.findById(88L)).thenReturn(Optional.of(otraPersona));

        Usuario cambios = new Usuario();
        Persona ref = new Persona();
        ref.setId(88L);
        cambios.setPersona(ref);

        assertThatThrownBy(() -> usuarioService.update(42L, cambios))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ya tiene un usuario asociado");
    }

    @Test
    void updateNoCodificaPasswordCuandoEsBlanco() throws Exception {
        Usuario existente = new Usuario();
        existente.setId(43L);
        existente.setPersona(persona);
        when(usuarioRepository.findById(43L)).thenReturn(Optional.of(existente));

        Usuario cambios = new Usuario();
        cambios.setPassword("   "); // espacios en blanco

        usuarioService.update(43L, cambios);

        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository).save(existente);
    }
}
