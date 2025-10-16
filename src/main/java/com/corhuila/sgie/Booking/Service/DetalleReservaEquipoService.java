package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.ActualizarReservaDetalleEquipoRequestDTO;
import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaEquipoResponseDTO;
import com.corhuila.sgie.Booking.DTO.DetalleReservaEquipoResponseDTO;
import com.corhuila.sgie.Booking.DTO.IReservaEquipoDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaEquipoRepository;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IDetalleReservaEquipoService;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Notification.NotificacionService;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleReservaEquipoService extends BaseService<DetalleReservaEquipo> implements IDetalleReservaEquipoService {
    @Autowired
    private IDetalleReservaEquipoRepository repository;
    @Autowired
    private IReservaRepository reservaRepository;
    @Autowired
    private NotificacionService notificacionService;

    @Override
    protected IBaseRepository<DetalleReservaEquipo, Long> getRepository() {
        return repository;
    }

    @Transactional
    public CerrarDetalleReservaEquipoResponseDTO cerrarDetalleReservaEquipo(Long idDetalle, String entregaEquipo) {
        DetalleReservaEquipo detalle = repository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        Reserva reserva = reservaRepository.findById(detalle.getReserva().getId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // actualizar detalle
        detalle.setEntregaEquipo(entregaEquipo);
        detalle.setState(false);
        detalle.setUpdatedAt(LocalDateTime.now());

        // verificar si todos los demás detalles ya están cerrados
        boolean todosCerrados = reserva.getDetalleReservaEquipos()
                .stream()
                .allMatch(d -> d.getId().equals(idDetalle) || Boolean.FALSE.equals(d.getState()));

        if (todosCerrados) {
            reserva.setState(false);
            reserva.setUpdatedAt(LocalDateTime.now());
            reservaRepository.save(reserva);
        }

        DetalleReservaEquipo saved = repository.save(detalle);
        return new CerrarDetalleReservaEquipoResponseDTO(
                saved.getId(),
                saved.getState(),
                saved.getEntregaEquipo(),
                saved.getUpdatedAt(),
                reserva.getId()
        );
    }


    @Override
    public List<IReservaEquipoDTO> findReservasEquipoByNumeroIdentificacion(String numeroIdentificacion) {
        return repository.findReservasEquipoByNumeroIdentificacion(numeroIdentificacion);
    }

    @Transactional
    public DetalleReservaEquipoResponseDTO actualizarDetalleReservaEquipo(
            Long idDetalle,
            ActualizarReservaDetalleEquipoRequestDTO request) {

        DetalleReservaEquipo detalle = repository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        Reserva reserva = detalle.getReserva();

        // Determinar si se cambia fecha/hora o equipo/instalación
        boolean cambiaFechaHora = request.getFechaReserva() != null
                || request.getHoraInicio() != null
                || request.getHoraFin() != null;
        boolean cambiaEquipoOInstalacion = request.getIdEquipo() != null
                || request.getIdInstalacionDestino() != null;

        if (cambiaFechaHora || cambiaEquipoOInstalacion) {
            LocalDate fecha = request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
            LocalTime horaInicio = request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
            LocalTime horaFin = request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
            Integer idEquipo = request.getIdEquipo() != null ? request.getIdEquipo().intValue() : detalle.getEquipo().getId().intValue();

            // Consultar horas disponibles para el equipo
            List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesEquipo(fecha, idEquipo, idDetalle);
            List<LocalTime> disponibles = horasDisponibles.stream()
                    .map(h -> LocalTime.parse(h[0].toString()))
                    .toList();

            // Generar todas las horas del rango solicitado
            List<LocalTime> rangoSolicitado = new ArrayList<>();
            for (LocalTime h = horaInicio; h.isBefore(horaFin); h = h.plusHours(1)) {
                rangoSolicitado.add(h);
            }

            // Validar que todas las horas estén libres
            boolean disponible = disponibles.containsAll(rangoSolicitado);
            if (!disponible) {
                throw new RuntimeException("El rango de horas seleccionado no está disponible para el equipo y fecha seleccionados.");
            }
        }

        // Actualizar campos de reserva
        if (request.getNombreReserva() != null) reserva.setNombre(request.getNombreReserva());
        if (request.getDescripcionReserva() != null) reserva.setDescripcion(request.getDescripcionReserva());
        if (request.getFechaReserva() != null) reserva.setFechaReserva(request.getFechaReserva());
        if (request.getHoraInicio() != null) reserva.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null) reserva.setHoraFin(request.getHoraFin());

        // Actualizar campos del detalle
        if (request.getProgramaAcademico() != null) detalle.setProgramaAcademico(request.getProgramaAcademico());
        if (request.getNumeroEstudiantes() != null) detalle.setNumeroEstudiantes(request.getNumeroEstudiantes());
        if (request.getIdEquipo() != null) {
            Equipo equipo = new Equipo();
            equipo.setId(request.getIdEquipo());
            detalle.setEquipo(equipo);
        }
        if (request.getIdInstalacionDestino() != null) {
            Instalacion instalacionDestino = new Instalacion();
            instalacionDestino.setId(request.getIdInstalacionDestino());
            detalle.setInstalacionDestino(instalacionDestino);
        }

        reserva.setUpdatedAt(LocalDateTime.now());
        detalle.setUpdatedAt(LocalDateTime.now());

        reservaRepository.save(reserva);
        DetalleReservaEquipo guardado = repository.save(detalle);

        // Mapear a DTO ligero
        return new DetalleReservaEquipoResponseDTO(
                guardado.getId(),
                reserva.getNombre(),
                reserva.getDescripcion(),
                reserva.getFechaReserva(),
                reserva.getHoraInicio(),
                reserva.getHoraFin(),
                guardado.getProgramaAcademico(),
                guardado.getNumeroEstudiantes(),
                guardado.getEquipo() != null ? guardado.getEquipo().getId() : null,
                //guardado.getEquipo() != null ? guardado.getEquipo().getNombre() : null,
                guardado.getEquipo() != null && guardado.getEquipo().getTipoEquipo() != null
                        ? guardado.getEquipo().getTipoEquipo().getNombre()
                        : null,
                guardado.getInstalacionDestino() != null ? guardado.getInstalacionDestino().getId() : null,
                guardado.getInstalacionDestino() != null ? guardado.getInstalacionDestino().getNombre() : null,
                reserva.getPersona() != null ? reserva.getPersona().getNombres() : null,
                reserva.getTipoReserva() != null ? reserva.getTipoReserva().getNombre() : null
        );
    }

    @Override
    protected void afterSave(DetalleReservaEquipo detalle) {
        if (detalle.getReserva() != null && detalle.getReserva().getId() != null) {
            reservaRepository.findWithPersonaAndUsuarioById(detalle.getReserva().getId())
                    .ifPresent(reserva -> {
                        if (reserva.getPersona() != null && reserva.getPersona().getUsuario() != null) {
                            String destinatario = reserva.getPersona().getUsuario().getEmail();
                            String asunto = "Confirmación de reserva de equipo";
                            String cuerpo = String.format("""
                                                Hola %s, tu reserva del equipo fue registrada:
                                                - Fecha: %s
                                                - Hora inicio: %s
                                                - Hora fin: %s
                                            """,
                                    reserva.getPersona().getNombres(),
                                    reserva.getFechaReserva(),
                                    reserva.getHoraInicio(),
                                    reserva.getHoraFin());

                            notificacionService.enviarCorreoReserva(destinatario, asunto, cuerpo);
                        }
                    });
        }
    }
}
