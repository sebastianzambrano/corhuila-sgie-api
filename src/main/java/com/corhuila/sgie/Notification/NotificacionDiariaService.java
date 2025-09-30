package com.corhuila.sgie.Notification;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificacionDiariaService {
    private final IReservaRepository reservaRepository;
    private final NotificacionService notificacionService;

    public NotificacionDiariaService(IReservaRepository reservaRepository,
                                     NotificacionService notificacionService) {
        this.reservaRepository = reservaRepository;
        this.notificacionService = notificacionService;
    }

    /**
     * Programa la tarea todos los días a las 11:59 PM
     */
    @Scheduled(cron = "0 59 23 * * *", zone = "America/Bogota")
    public void enviarNotificacionReservasAbiertas() {
        LocalDate hoy = LocalDate.now();

        // 1. Consultar reservas abiertas
        List<Reserva> abiertas = reservaRepository.findByFechaReservaAndStateTrue(hoy);

        if (abiertas.isEmpty()) {
            return;
        }

        // 2. Agrupar por persona (si quieres notificar a cada usuario)
        abiertas.stream()
                .filter(r -> r.getPersona() != null && r.getPersona().getUsuario() != null)
                .forEach(reserva -> {
                    String destinatario = reserva.getPersona().getUsuario().getEmail();
                    String asunto = "Resumen de reservas abiertas - " + hoy;
                    String cuerpo = String.format("""
                        Hola %s, tienes una reserva pendiente:
                        - Nombre: %s
                        - Fecha: %s
                        - Hora inicio: %s
                        - Hora fin: %s
                    """,
                            reserva.getPersona().getNombres(),
                            reserva.getNombre(),
                            reserva.getFechaReserva(),
                            reserva.getHoraInicio(),
                            reserva.getHoraFin()
                    );

                    notificacionService.enviarCorreoReserva(destinatario, asunto, cuerpo);
                });
    }
}
