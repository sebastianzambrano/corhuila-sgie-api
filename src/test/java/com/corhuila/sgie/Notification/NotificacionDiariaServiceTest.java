package com.corhuila.sgie.Notification;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.User.Entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionDiariaServiceTest {

    @Mock
    private IReservaRepository reservaRepository;
    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private NotificacionDiariaService service;

    @Test
    void noEnviaCorreosCuandoNoHayReservas() {
        when(reservaRepository.findByFechaReservaAndStateTrue(any())).thenReturn(List.of());
        service.enviarNotificacionReservasAbiertas();
        verifyNoInteractions(notificacionService);
    }

    @Test
    void enviaCorreoPorCadaReserva() {
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraInicio(LocalTime.of(9, 0));
        reserva.setHoraFin(LocalTime.of(10, 0));
        reserva.setNombre("Reserva demo");

        Persona persona = new Persona();
        persona.setNombres("Usuario");
        Usuario usuario = new Usuario();
        usuario.setEmail("user@mail.com");
        persona.setUsuario(usuario);
        reserva.setPersona(persona);

        when(reservaRepository.findByFechaReservaAndStateTrue(any()))
                .thenReturn(List.of(reserva));

        service.enviarNotificacionReservasAbiertas();

        verify(notificacionService).enviarCorreoReserva(eq("user@mail.com"), contains("Resumen"), contains("Reserva demo"));
    }
}
