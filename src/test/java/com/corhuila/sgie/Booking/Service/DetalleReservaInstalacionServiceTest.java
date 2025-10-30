package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.ActualizarReservaDetalleInstalacionRequestDTO;
import com.corhuila.sgie.Booking.DTO.DetalleReservaInstalacionResponseDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.Entity.TipoReserva;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaInstalacionRepository;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Notification.NotificacionService;
import com.corhuila.sgie.Site.Entity.Instalacion;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleReservaInstalacionServiceTest {

    @Mock
    private IDetalleReservaInstalacionRepository detalleRepository;
    @Mock
    private IReservaRepository reservaRepository;
    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private DetalleReservaInstalacionService service;

    private DetalleReservaInstalacion detalle;
    private Reserva reserva;

    @BeforeEach
    void setup() {
        reserva = new Reserva();
        reserva.setId(30L);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(8, 0));
        reserva.setHoraFin(LocalTime.of(10, 0));
        reserva.setState(true);
        reserva.setTipoReserva(new TipoReserva());
        reserva.getTipoReserva().setId(5L);

        Instalacion instalacion = new Instalacion();
        instalacion.setId(6L);
        instalacion.setNombre("Laboratorio");

        detalle = new DetalleReservaInstalacion();
        detalle.setId(40L);
        detalle.setReserva(reserva);
        detalle.setInstalacion(instalacion);
        detalle.setState(true);
        reserva.setDetalleReservaInstalaciones(new java.util.HashSet<>());
        reserva.getDetalleReservaInstalaciones().add(detalle);
    }

    @Test
    void cerrarDetalleReservaInstalacionActualizaReserva() {
        when(detalleRepository.findById(40L)).thenReturn(Optional.of(detalle));
        when(reservaRepository.findById(30L)).thenReturn(Optional.of(reserva));
        when(detalleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.cerrarDetalleReservaInstalacion(40L, "Entregado");

        assertThat(response.getEntregaInstalacion()).isEqualTo("Entregado");
        assertThat(reserva.getState()).isFalse();
    }

    @Test
    void cerrarDetalleReservaInstalacionMantieneReservaAbiertaSiHayActivos() {
        DetalleReservaInstalacion otro = new DetalleReservaInstalacion();
        otro.setId(41L);
        otro.setReserva(reserva);
        otro.setInstalacion(new Instalacion());
        otro.setState(true);
        reserva.getDetalleReservaInstalaciones().add(otro);

        when(detalleRepository.findById(40L)).thenReturn(Optional.of(detalle));
        when(reservaRepository.findById(30L)).thenReturn(Optional.of(reserva));
        when(detalleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.cerrarDetalleReservaInstalacion(40L, "Entregado");

        assertThat(reserva.getState()).isTrue();
        verify(reservaRepository, never()).save(reserva);
    }

    @Test
    void actualizarDetalleReservaInstalacionValidaDisponibilidad() {
        when(detalleRepository.findById(40L)).thenReturn(Optional.of(detalle));
        java.util.List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"08:00"});
        disponibilidad.add(new Object[]{"09:00"});
        when(reservaRepository.findHorasDisponiblesInstalacion(any(LocalDate.class), anyInt(), anyLong()))
                .thenReturn(disponibilidad);
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(detalleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ActualizarReservaDetalleInstalacionRequestDTO request = new ActualizarReservaDetalleInstalacionRequestDTO();
        request.setFechaReserva(LocalDate.now());
        request.setHoraInicio(LocalTime.of(8, 0));
        request.setHoraFin(LocalTime.of(10, 0));
        request.setProgramaAcademico("Ingeniería");
        request.setNumeroEstudiantes((short) 25);

        DetalleReservaInstalacionResponseDTO response = service.actualizarDetalleReservaInstalacion(40L, request);

        assertThat(response.getProgramaAcademico()).isEqualTo("Ingeniería");
        assertThat(response.getNumeroEstudiantes()).isEqualTo((short) 25);
    }

    @Test
    void actualizarDetalleReservaInstalacionPermiteCambiarInstalacion() {
        when(detalleRepository.findById(40L)).thenReturn(Optional.of(detalle));

        // Simula disponibilidad para 08:00 y 09:00 en instalación 99 y detalle 40
        java.util.List<Object[]> disponibilidad = new ArrayList<>();
        disponibilidad.add(new Object[]{"08:00"});
        disponibilidad.add(new Object[]{"09:00"});
        when(reservaRepository.findHorasDisponiblesInstalacion(any(LocalDate.class), eq(99), eq(40L)))
                .thenReturn(disponibilidad);

        when(reservaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(detalleRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ActualizarReservaDetalleInstalacionRequestDTO request = new ActualizarReservaDetalleInstalacionRequestDTO();
        request.setIdInstalacion(99L);

        DetalleReservaInstalacionResponseDTO response = service.actualizarDetalleReservaInstalacion(40L, request);

        assertThat(response.getIdInstalacion()).isEqualTo(99L);
    }

    @Test
    void findReservaInstalacionDelegatesToRepository() {
        var dto = org.mockito.Mockito.mock(com.corhuila.sgie.Booking.DTO.IReservaInstalacionDTO.class);
        when(detalleRepository.findReservaInstalacionByNumeroIdentificacion("123"))
                .thenReturn(java.util.List.of(dto));
        assertThat(service.findReservaInstalacionByNumeroIdentificacion("123"))
                .containsExactly(dto);
    }
}
