package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.ActualizarReservaDetalleEquipoRequestDTO;
import com.corhuila.sgie.Booking.DTO.DetalleReservaEquipoResponseDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.Entity.TipoReserva;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaEquipoRepository;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Equipment.Entity.CategoriaEquipo;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Equipment.Entity.TipoEquipo;
import com.corhuila.sgie.Notification.NotificacionService;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.User.Entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleReservaEquipoServiceTest {

    @Mock
    private IDetalleReservaEquipoRepository detalleRepository;
    @Mock
    private IReservaRepository reservaRepository;
    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private DetalleReservaEquipoService service;

    private DetalleReservaEquipo detalle;
    private Reserva reserva;

    @BeforeEach
    void setup() {
        Persona persona = new Persona();
        persona.setId(10L);
        persona.setNombres("Usuario");

        Usuario usuario = new Usuario();
        usuario.setEmail("user@mail.com");
        persona.setUsuario(usuario);

        TipoReserva tipo = new TipoReserva();
        tipo.setId(2L);

        reserva = new Reserva();
        reserva.setId(20L);
        reserva.setPersona(persona);
        reserva.setTipoReserva(tipo);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(8, 0));
        reserva.setHoraFin(LocalTime.of(10, 0));
        reserva.setDetalleReservaEquipos(new java.util.HashSet<>());

        CategoriaEquipo categoria = new CategoriaEquipo();
        categoria.setNombre("Multimedia");
        TipoEquipo tipoEquipo = new TipoEquipo();
        tipoEquipo.setId(30L);
        tipoEquipo.setNombre("Proyector");
        tipoEquipo.setCategoriaEquipo(categoria);

        Equipo equipo = new Equipo();
        equipo.setId(40L);
        equipo.setCodigo("EQ-1");
        equipo.setTipoEquipo(tipoEquipo);

        detalle = new DetalleReservaEquipo();
        detalle.setId(50L);
        detalle.setReserva(reserva);
        detalle.setEquipo(equipo);
        detalle.setState(true);
        reserva.getDetalleReservaEquipos().add(detalle);

        lenient().when(reservaRepository.findById(reserva.getId())).thenReturn(Optional.of(reserva));
    }

    @Test
    void cerrarDetalleReservaEquipoActualizaReservaCuandoTodosCerrados() {
        reserva.getDetalleReservaEquipos().forEach(d -> d.setState(false));

        when(detalleRepository.findById(50L)).thenReturn(Optional.of(detalle));
        when(reservaRepository.findById(20L)).thenReturn(Optional.of(reserva));
        when(detalleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.cerrarDetalleReservaEquipo(50L, "Devuelto");

        assertThat(response.getEntregaEquipo()).isEqualTo("Devuelto");
        assertThat(reserva.getState()).isFalse();
        verify(reservaRepository).save(reserva);
    }

    @Test
    void cerrarDetalleReservaEquipoLanzaCuandoNoExiste() {
        when(detalleRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.cerrarDetalleReservaEquipo(99L, "Devuelto"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Detalle no encontrado");
    }

    @Test
    void actualizarDetalleReservaEquipoValidaDisponibilidad() {
        reserva.setDetalleReservaEquipos(new java.util.HashSet<>());
        reserva.getDetalleReservaEquipos().add(detalle);

        when(detalleRepository.findById(50L)).thenReturn(Optional.of(detalle));
        java.util.List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"08:00"});
        disponibilidad.add(new Object[]{"09:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(), anyInt(), anyLong(), any()))
                .thenReturn(disponibilidad);
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(detalleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ActualizarReservaDetalleEquipoRequestDTO request = new ActualizarReservaDetalleEquipoRequestDTO();
        request.setFechaReserva(LocalDate.now());
        request.setHoraInicio(LocalTime.of(8, 0));
        request.setHoraFin(LocalTime.of(10, 0));
        request.setProgramaAcademico("Ingeniería");
        request.setNumeroEstudiantes((short) 25);

        DetalleReservaEquipoResponseDTO response = service.actualizarDetalleReservaEquipo(50L, request);

        assertThat(response.getProgramaAcademico()).isEqualTo("Ingeniería");
        assertThat(response.getNumeroEstudiantes()).isEqualTo((short) 25);
    }

    @Test
    void actualizarDetalleReservaEquipoLanzaSiNoHayDisponibilidad() {
        when(detalleRepository.findById(50L)).thenReturn(Optional.of(detalle));
        java.util.List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"08:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(), anyInt(), anyLong(), any()))
                .thenReturn(disponibilidad);

        ActualizarReservaDetalleEquipoRequestDTO request = new ActualizarReservaDetalleEquipoRequestDTO();
        request.setHoraInicio(LocalTime.of(8, 0));
        request.setHoraFin(LocalTime.of(10, 0));

        assertThatThrownBy(() -> service.actualizarDetalleReservaEquipo(50L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no está disponible");
    }

    // Agregar dentro de la clase DetalleReservaEquipoServiceTest
    @Test
    void save_ok_ejecutaBeforeYAfterSave() {
        // Instalación requerida por beforeSave
        Instalacion instalacion = new Instalacion();
        instalacion.setId(70L);
        instalacion.setNombre("Sala A");
        detalle.setInstalacionDestino(instalacion);

        // Disponibilidad completa para el rango [08:00, 10:00): 08:00 y 09:00
        java.util.List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"08:00"});
        disponibilidad.add(new Object[]{"09:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(LocalDate.class), anyInt(), isNull(), any()))
                .thenReturn(disponibilidad);

        // Guardado y afterSave (envío de correo)
        when(detalleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservaRepository.findWithPersonaAndUsuarioById(anyLong())).thenReturn(Optional.of(reserva));

        // Act
        DetalleReservaEquipo guardado = service.save(detalle);

        // Assert
        assertThat(guardado).isNotNull();
        verify(detalleRepository).save(any());
        verify(reservaRepository).findHorasDisponiblesEquipo(any(LocalDate.class), anyInt(), isNull(), any());
        verify(notificacionService).enviarCorreoReserva(eq("user@mail.com"), anyString(), anyString());
    }

    @Test
    void save_fallaPorCamposObligatorios_instalacionDestino() {
        // Reserva válida
        Reserva r = new Reserva();
        r.setId(999L);
        r.setFechaReserva(LocalDate.now());
        r.setHoraInicio(LocalTime.of(8, 0));
        r.setHoraFin(LocalTime.of(9, 0));

        // Equipo OK
        Equipo eq = new Equipo();
        eq.setId(1L);

        // Detalle SIN instalación destino (dispara validación)
        DetalleReservaEquipo d = new DetalleReservaEquipo();
        d.setReserva(r);
        d.setEquipo(eq);

        when(reservaRepository.findById(r.getId())).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> service.save(d))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La instalacion destino es obligatorio.");
    }

    @Test
    void save_fallaPorRangoNoDisponible() {
        // Instalación requerida
        Instalacion instalacion = new Instalacion();
        instalacion.setId(70L);
        detalle.setInstalacionDestino(instalacion);

        // Disponibilidad incompleta: solo 08:00, falta 09:00 para [08:00, 10:00)
        java.util.List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"08:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(LocalDate.class), anyInt(), isNull(), any()))
                .thenReturn(disponibilidad);

        assertThatThrownBy(() -> service.save(detalle))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("El rango no está disponible de equipo.");
    }

}
