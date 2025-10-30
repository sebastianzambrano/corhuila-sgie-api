package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Maintenance.DTO.ActualizarMantenimientoInstalacionRequestDTO;
import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoInstalacionResponseDTO;
import com.corhuila.sgie.Maintenance.DTO.IMantenimientoInstalacionDTO;
import com.corhuila.sgie.Maintenance.DTO.MantenimientoInstalacionResponseDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoInstalacionRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MantenimientoInstalacionServiceTest {

    @Mock
    private IMantenimientoInstalacionRepository mantenimientoRepository;
    @Mock
    private IReservaRepository reservaRepository;
    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private MantenimientoInstalacionService service;

    private MantenimientoInstalacion mantenimiento;
    private Reserva reserva;

    @BeforeEach
    void setup() {
        reserva = new Reserva();
        reserva.setId(11L);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(9, 0));
        reserva.setHoraFin(LocalTime.of(11, 0));

        Instalacion instalacion = new Instalacion();
        instalacion.setId(3L);

        mantenimiento = new MantenimientoInstalacion();
        mantenimiento.setId(22L);
        mantenimiento.setReserva(reserva);
        mantenimiento.setInstalacion(instalacion);
        mantenimiento.setState(true);
    }

    @Test
    void cerrarMantenimientoInstalacionActualizaReserva() {
        when(mantenimientoRepository.findById(22L)).thenReturn(Optional.of(mantenimiento));
        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CerrarMantenimientoInstalacionResponseDTO response =
                service.cerrarMantenimientoInstalacion(22L, LocalDate.now().plusDays(5), "Ok");

        assertThat(response.getFechaProximaMantenimiento()).isEqualTo(LocalDate.now().plusDays(5));
        assertThat(reserva.getState()).isFalse();
    }

    @Test
    void cerrarMantenimientoInstalacionLanzaCuandoNoExiste() {
        when(mantenimientoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.cerrarMantenimientoInstalacion(99L, LocalDate.now(), "Ok"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void actualizarMantenimientoInstalacionValidaHoras() {
        when(mantenimientoRepository.findById(22L)).thenReturn(Optional.of(mantenimiento));
        List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"09:00"});
        disponibilidad.add(new Object[]{"10:00"});
        when(reservaRepository.findHorasDisponiblesInstalacion(any(), anyInt(), anyLong())).thenReturn(disponibilidad);
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ActualizarMantenimientoInstalacionRequestDTO request = new ActualizarMantenimientoInstalacionRequestDTO();
        request.setFechaReserva(LocalDate.now());
        request.setHoraInicio(LocalTime.of(9, 0));
        request.setHoraFin(LocalTime.of(11, 0));
        request.setDescripcion("Actualizar");

        MantenimientoInstalacionResponseDTO dto = service.actualizarMantenimientoInstalacion(22L, request);

        assertThat(dto.getDescripcion()).isEqualTo("Actualizar");
        assertThat(dto.getHoraFin()).isEqualTo(LocalTime.of(11, 0));
    }

    @Test
    void actualizarMantenimientoInstalacionSinCambiarHorario() {
        when(mantenimientoRepository.findById(22L)).thenReturn(Optional.of(mantenimiento));
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ActualizarMantenimientoInstalacionRequestDTO request = new ActualizarMantenimientoInstalacionRequestDTO();
        request.setDescripcion("Detalles");
        request.setDescripcionReserva("Reserva");

        MantenimientoInstalacionResponseDTO dto = service.actualizarMantenimientoInstalacion(22L, request);
        assertThat(dto.getDescripcion()).isEqualTo("Detalles");
        assertThat(dto.getDescripcionReserva()).isEqualTo("Reserva");
    }

    @Test
    void actualizarMantenimientoInstalacionLanzaCuandoNoDisponible() {
        when(mantenimientoRepository.findById(22L)).thenReturn(Optional.of(mantenimiento));
        List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"09:00"});
        when(reservaRepository.findHorasDisponiblesInstalacion(any(), anyInt(), anyLong())).thenReturn(disponibilidad);

        ActualizarMantenimientoInstalacionRequestDTO request = new ActualizarMantenimientoInstalacionRequestDTO();
        request.setHoraInicio(LocalTime.of(9, 0));
        request.setHoraFin(LocalTime.of(11, 0));

        assertThatThrownBy(() -> service.actualizarMantenimientoInstalacion(22L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no está disponible");
    }

    @Test
    void findMantenimientosDelegatesToRepository() {
        IMantenimientoInstalacionDTO dto = org.mockito.Mockito.mock(IMantenimientoInstalacionDTO.class);
        when(mantenimientoRepository.findMantenimientosInstalacionByNumeroIdentificacion("123"))
                .thenReturn(List.of(dto));

        assertThat(service.findMantenimientosInstalacionByNumeroIdentificacion("123"))
                .containsExactly(dto);
    }

    // java
    @Test
    void saveEnviaCorreoCuandoReservaTieneUsuario() throws Exception {
        MantenimientoInstalacion nuevo = new MantenimientoInstalacion();
        Reserva reservaRegistrada = new Reserva();
        reservaRegistrada.setId(88L);
        reservaRegistrada.setFechaReserva(LocalDate.now());
        reservaRegistrada.setHoraInicio(LocalTime.of(10, 0));
        reservaRegistrada.setHoraFin(LocalTime.of(11, 0));

        Persona persona = new Persona();
        persona.setNombres("Maria");
        Usuario usuario = new Usuario();
        usuario.setEmail("maria@example.com");
        usuario.setPersona(persona);
        persona.setUsuario(usuario);
        reservaRegistrada.setPersona(persona);

        Instalacion instalacion = new Instalacion();
        instalacion.setId(3L);
        nuevo.setInstalacion(instalacion);
        nuevo.setReserva(reservaRegistrada);

        // Disponibilidad debe incluir 10:00 para el rango 10:00-11:00
        List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"10:00"});
        when(reservaRepository.findHorasDisponiblesInstalacion(any(LocalDate.class), anyInt(), isNull()))
                .thenReturn(disponibilidad);

        when(mantenimientoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(reservaRepository.findWithPersonaAndUsuarioById(88L)).thenReturn(Optional.of(reservaRegistrada));

        service.save(nuevo);

        verify(notificacionService).enviarCorreoReserva(
                eq("maria@example.com"),
                contains("Mantenimiento"),
                contains("Hola Maria")
        );
    }


    // java
    @Test
    void saveNoEnviaCorreoCuandoReservaSinUsuario() throws Exception {
        MantenimientoInstalacion nuevo = new MantenimientoInstalacion();
        Reserva reservaSinUsuario = new Reserva();
        Instalacion instalacion = new Instalacion();

        reservaSinUsuario.setId(91L);
        reservaSinUsuario.setFechaReserva(LocalDate.now());
        reservaSinUsuario.setHoraInicio(LocalTime.of(14, 0));
        reservaSinUsuario.setHoraFin(LocalTime.of(15, 0));

        Persona persona = new Persona();
        persona.setNombres("Carlos");
        reservaSinUsuario.setPersona(persona);

        instalacion.setId(1L);
        instalacion.setNombre("laboratorio");

        nuevo.setInstalacion(instalacion);
        nuevo.setReserva(reservaSinUsuario);

        // Disponibilidad: debe incluir 14:00 para que el rango 14:00-15:00 sea válido
        List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"14:00"});
        when(reservaRepository.findHorasDisponiblesInstalacion(any(LocalDate.class), anyInt(), isNull()))
                .thenReturn(disponibilidad);

        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservaRepository.findWithPersonaAndUsuarioById(91L)).thenReturn(Optional.of(reservaSinUsuario));

        service.save(nuevo);

        verify(notificacionService, never()).enviarCorreoReserva(any(), any(), any());
    }

}
