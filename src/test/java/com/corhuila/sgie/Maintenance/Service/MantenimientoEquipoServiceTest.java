package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Maintenance.DTO.ActualizarMantenimientoEquipoRequestDTO;
import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoEquipoResponseDTO;
import com.corhuila.sgie.Maintenance.DTO.IMantenimientoEquipoDTO;
import com.corhuila.sgie.Maintenance.DTO.MantenimientoEquipoResponseDTO;
import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoEquipo;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoEquipoRepository;
import com.corhuila.sgie.Notification.NotificacionService;
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
class MantenimientoEquipoServiceTest {

    @Mock
    private IMantenimientoEquipoRepository mantenimientoRepository;
    @Mock
    private IReservaRepository reservaRepository;
    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private MantenimientoEquipoService service;

    private MantenimientoEquipo mantenimiento;
    private Reserva reserva;

    @BeforeEach
    void setup() {
        reserva = new Reserva();
        reserva.setId(10L);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(8, 0));
        reserva.setHoraFin(LocalTime.of(10, 0));

        CategoriaMantenimientoEquipo categoria = new CategoriaMantenimientoEquipo();
        categoria.setId(5L);
        categoria.setNombre("Preventivo");

        mantenimiento = new MantenimientoEquipo();
        mantenimiento.setId(20L);
        mantenimiento.setReserva(reserva);
        mantenimiento.setCategoriaMantenimientoEquipo(categoria);
        mantenimiento.setState(true);
        mantenimiento.setEquipo(new com.corhuila.sgie.Equipment.Entity.Equipo());
        mantenimiento.getEquipo().setId(30L);
        mantenimiento.setState(true);
    }

    @Test
    void cerrarMantenimientoEquipoActualizaReserva() {
        when(mantenimientoRepository.findById(20L)).thenReturn(Optional.of(mantenimiento));
        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CerrarMantenimientoEquipoResponseDTO response =
                service.cerrarMantenimientoEquipo(20L, LocalDate.now().plusDays(10), "Ok");

        assertThat(response.getFechaProximaMantenimiento()).isEqualTo(LocalDate.now().plusDays(10));
        assertThat(reserva.getState()).isFalse();
        verify(reservaRepository).save(reserva);
    }

    @Test
    void cerrarMantenimientoEquipoLanzaCuandoNoExiste() {
        when(mantenimientoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.cerrarMantenimientoEquipo(99L, LocalDate.now(), "Ok"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    void actualizarMantenimientoEquipoValidaHoras() {
        when(mantenimientoRepository.findById(20L)).thenReturn(Optional.of(mantenimiento));
        List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"08:00"});
        disponibilidad.add(new Object[]{"09:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(), anyInt(), anyLong())).thenReturn(disponibilidad);
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ActualizarMantenimientoEquipoRequestDTO request = new ActualizarMantenimientoEquipoRequestDTO();
        request.setFechaReserva(LocalDate.now());
        request.setHoraInicio(LocalTime.of(8, 0));
        request.setHoraFin(LocalTime.of(10, 0));
        request.setDescripcion("Actualizar");
        request.setResultadoMantenimiento("OK");

        MantenimientoEquipoResponseDTO dto = service.actualizarMantenimientoEquipo(20L, request);

        assertThat(dto.getDescripcion()).isEqualTo("Actualizar");
        assertThat(dto.getResultadoMantenimiento()).isEqualTo("OK");
    }

    @Test
    void actualizarMantenimientoEquipoSinCambiarHorario() {
        when(mantenimientoRepository.findById(20L)).thenReturn(Optional.of(mantenimiento));
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ActualizarMantenimientoEquipoRequestDTO request = new ActualizarMantenimientoEquipoRequestDTO();
        request.setDescripcion("Nueva descr");
        request.setDescripcionReserva("Reserva actualizada");

        MantenimientoEquipoResponseDTO dto = service.actualizarMantenimientoEquipo(20L, request);
        assertThat(dto.getDescripcion()).isEqualTo("Nueva descr");
        assertThat(dto.getDescripcionReserva()).isEqualTo("Reserva actualizada");
    }

    @Test
    void actualizarMantenimientoEquipoLanzaCuandoNoDisponible() {
        when(mantenimientoRepository.findById(20L)).thenReturn(Optional.of(mantenimiento));
        List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"08:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(), anyInt(), anyLong())).thenReturn(disponibilidad);

        ActualizarMantenimientoEquipoRequestDTO request = new ActualizarMantenimientoEquipoRequestDTO();
        request.setHoraInicio(LocalTime.of(8, 0));
        request.setHoraFin(LocalTime.of(10, 0));

        assertThatThrownBy(() -> service.actualizarMantenimientoEquipo(20L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no está disponible");
    }

    @Test
    void findMantenimientosDelegatesToRepository() {
        IMantenimientoEquipoDTO dto = org.mockito.Mockito.mock(IMantenimientoEquipoDTO.class);
        when(mantenimientoRepository.findMantenimientosEquipoByNumeroIdentificacion("123"))
                .thenReturn(List.of(dto));

        assertThat(service.findMantenimientosEquipoByNumeroIdentificacion("123")).containsExactly(dto);
    }

    // java
    @Test
    void saveEnviaCorreoCuandoReservaTieneUsuario() throws Exception {
        MantenimientoEquipo nuevo = new MantenimientoEquipo();
        Reserva reservaRegistrada = new Reserva();
        reservaRegistrada.setId(55L);
        reservaRegistrada.setFechaReserva(LocalDate.now());
        reservaRegistrada.setHoraInicio(LocalTime.of(7, 0));
        reservaRegistrada.setHoraFin(LocalTime.of(8, 0));

        Persona persona = new Persona();
        persona.setNombres("Ana");
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@example.com");
        usuario.setPersona(persona);
        persona.setUsuario(usuario);
        reservaRegistrada.setPersona(persona);

        // Equipo requerido por beforeSave
        com.corhuila.sgie.Equipment.Entity.Equipo equipo = new com.corhuila.sgie.Equipment.Entity.Equipo();
        equipo.setId(3L);
        nuevo.setEquipo(equipo);
        nuevo.setReserva(reservaRegistrada);

        // Disponibilidad debe incluir 07:00 para el rango 07:00-08:00
        List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"07:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(LocalDate.class), anyInt(), isNull()))
                .thenReturn(disponibilidad);

        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservaRepository.findWithPersonaAndUsuarioById(55L)).thenReturn(Optional.of(reservaRegistrada));

        service.save(nuevo);

        verify(notificacionService).enviarCorreoReserva(
                eq("ana@example.com"),
                contains("Confirmación de mantenimiento de equipo"),
                contains("Hola Ana")
        );
    }


    // java
    @Test
    void saveNoEnviaCorreoCuandoReservaSinUsuario() throws Exception {
        MantenimientoEquipo nuevo = new MantenimientoEquipo();
        Reserva reservaSinUsuario = new Reserva();
        reservaSinUsuario.setId(77L);
        reservaSinUsuario.setFechaReserva(LocalDate.now());
        reservaSinUsuario.setHoraInicio(LocalTime.of(9, 0));
        reservaSinUsuario.setHoraFin(LocalTime.of(10, 0));

        Persona personaSinUsuario = new Persona();
        personaSinUsuario.setNombres("Luis");
        // Sin usuario asociado
        reservaSinUsuario.setPersona(personaSinUsuario);

        // Equipo requerido por beforeSave
        com.corhuila.sgie.Equipment.Entity.Equipo equipo = new com.corhuila.sgie.Equipment.Entity.Equipo();
        equipo.setId(3L);
        nuevo.setEquipo(equipo);
        nuevo.setReserva(reservaSinUsuario);

        // Disponibilidad debe incluir 09:00 para el rango 09:00-10:00
        List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"09:00"});
        when(reservaRepository.findHorasDisponiblesEquipo(any(LocalDate.class), anyInt(), isNull()))
                .thenReturn(disponibilidad);

        when(mantenimientoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservaRepository.findWithPersonaAndUsuarioById(77L)).thenReturn(Optional.of(reservaSinUsuario));

        service.save(nuevo);

        verify(notificacionService, never()).enviarCorreoReserva(any(), any(), any());
    }

}
