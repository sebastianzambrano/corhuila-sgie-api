package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Maintenance.DTO.ActualizarMantenimientoEquipoRequestDTO;
import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoEquipoResponseDTO;
import com.corhuila.sgie.Maintenance.DTO.IMantenimientoEquipoDTO;
import com.corhuila.sgie.Maintenance.DTO.MantenimientoEquipoResponseDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoEquipoRepository;
import com.corhuila.sgie.Maintenance.IService.IMantenimientoEquipoService;
import com.corhuila.sgie.Notification.NotificacionService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MantenimientoEquipoService extends BaseService<MantenimientoEquipo> implements IMantenimientoEquipoService {

    private static final String PLANTILLA_CORREO_MANTENIMIENTO = """
                Hola %s, tu reserva de mantenimiento de equipo fue registrada:
                - Fecha: %s
                - Hora inicio: %s
                - Hora fin: %s
            """;

    private final IMantenimientoEquipoRepository repository;
    private final IReservaRepository reservaRepository;
    private final NotificacionService notificacionService;

    public MantenimientoEquipoService(IMantenimientoEquipoRepository repository, IReservaRepository reservaRepository, NotificacionService notificacionService) {
        this.repository = repository;
        this.reservaRepository = reservaRepository;
        this.notificacionService = notificacionService;
    }

    @Override
    protected IBaseRepository<MantenimientoEquipo, Long> getRepository() {
        return repository;
    }

    @Override
    protected void beforeSave(MantenimientoEquipo mantenimiento) {
        Reserva reserva = mantenimiento.getReserva();

        if (reserva == null || reserva.getFechaReserva() == null || reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            throw new IllegalArgumentException("Fecha y horas son obligatorias.");
        }
        if (!reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            throw new IllegalArgumentException("La hora fin debe ser mayor que la hora inicio.");
        }
        if (mantenimiento.getEquipo() == null || mantenimiento.getEquipo().getId() == null) {
            throw new IllegalArgumentException("El equipo es obligatorio.");
        }

        // DISPONIBILIDAD ***DE EQUIPO*** en creación (idDetalle = null)
        List<LocalTime> disponibles = reservaRepository
                .findHorasDisponiblesEquipo(
                        reserva.getFechaReserva(),
                        mantenimiento.getEquipo().getId().intValue(),
                        null // creación
                )
                .stream()
                .map(h -> LocalTime.parse(h[0].toString()))
                .toList();

        List<LocalTime> rango = generarRangoHorario(reserva.getHoraInicio(), reserva.getHoraFin());

        if (!disponibles.containsAll(rango)) {
            throw new IllegalStateException("El rango no está disponible de equipo.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public CerrarMantenimientoEquipoResponseDTO cerrarMantenimientoEquipo(
            Long idMantenimiento,
            LocalDate fechaProximaMantenimiento,
            String resultadoMantenimiento) {

        MantenimientoEquipo mantenimiento = repository.findById(idMantenimiento)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        Reserva reserva = mantenimiento.getReserva();

        mantenimiento.setFechaProximaMantenimiento(fechaProximaMantenimiento);
        mantenimiento.setResultadoMantenimiento(resultadoMantenimiento);
        mantenimiento.setState(false);
        mantenimiento.setUpdatedAt(LocalDateTime.now());

        reserva.setState(false);
        reserva.setUpdatedAt(LocalDateTime.now());
        reservaRepository.save(reserva);

        MantenimientoEquipo saved = repository.save(mantenimiento);

        return new CerrarMantenimientoEquipoResponseDTO(
                saved.getId(),
                saved.getState(),
                saved.getFechaProximaMantenimiento(),
                saved.getResultadoMantenimiento(),
                saved.getUpdatedAt(),
                reserva.getId()
        );
    }

    @Override
    public List<IMantenimientoEquipoDTO> findMantenimientosEquipoByNumeroIdentificacion(String numeroIdentificacion) {
        return repository.findMantenimientosEquipoByNumeroIdentificacion(numeroIdentificacion);
    }

    @Transactional(rollbackFor = Exception.class)
    public MantenimientoEquipoResponseDTO actualizarMantenimientoEquipo(
            Long idMantenimiento,
            ActualizarMantenimientoEquipoRequestDTO request) {

        MantenimientoEquipo mantenimiento = repository.findById(idMantenimiento)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        Reserva reserva = mantenimiento.getReserva();

        validarDisponibilidadSiCambiaFechaHora(request, reserva, mantenimiento, idMantenimiento);
        actualizarCamposMantenimiento(request, mantenimiento);
        actualizarCamposReserva(request, reserva);

        reserva.setUpdatedAt(LocalDateTime.now());
        mantenimiento.setUpdatedAt(LocalDateTime.now());

        reservaRepository.save(reserva);
        MantenimientoEquipo guardado = repository.save(mantenimiento);

        return new MantenimientoEquipoResponseDTO(
                guardado.getId(),
                guardado.getDescripcion(),
                guardado.getFechaProximaMantenimiento(),
                guardado.getResultadoMantenimiento(),
                reserva.getNombre(),
                reserva.getDescripcion(),
                reserva.getFechaReserva(),
                reserva.getHoraInicio(),
                reserva.getHoraFin()
        );
    }

    private void validarDisponibilidadSiCambiaFechaHora(
            ActualizarMantenimientoEquipoRequestDTO request,
            Reserva reserva,
            MantenimientoEquipo mantenimiento,
            Long idMantenimiento) {

        boolean cambiaFechaHora =
                request.getFechaReserva() != null ||
                        request.getHoraInicio() != null ||
                        request.getHoraFin() != null ||
                        request.getIdEquipo() != null;

        if (!cambiaFechaHora) {
            return;
        }

        LocalDate fecha = request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
        LocalTime horaInicio = request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
        LocalTime horaFin = request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
        Integer idEquipo = obtenerIdEquipoEfectivo(request, mantenimiento);

        validarHorasDisponibles(fecha, horaInicio, horaFin, idEquipo, idMantenimiento);
    }

    private Integer obtenerIdEquipoEfectivo(ActualizarMantenimientoEquipoRequestDTO request,
                                            MantenimientoEquipo mantenimiento) {
        return (request.getIdEquipo() != null)
                ? request.getIdEquipo().intValue()
                : mantenimiento.getEquipo().getId().intValue();
    }

    private void validarHorasDisponibles(
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            Integer idEquipo,
            Long idMantenimiento) {

        List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesEquipo(fecha, idEquipo, idMantenimiento);
        List<LocalTime> disponibles = horasDisponibles.stream()
                .map(h -> LocalTime.parse(h[0].toString()))
                .toList();

        List<LocalTime> rangoSolicitado = generarRangoHorario(horaInicio, horaFin);
        if (!disponibles.containsAll(rangoSolicitado)) {
            throw new IllegalStateException("El rango de horas seleccionado no está disponible para el equipo y fecha seleccionados.");
        }
    }

    private List<LocalTime> generarRangoHorario(LocalTime horaInicio, LocalTime horaFin) {
        List<LocalTime> rango = new ArrayList<>();
        for (LocalTime h = horaInicio; h.isBefore(horaFin); h = h.plusHours(1)) {
            rango.add(h);
        }
        return rango;
    }

    private void actualizarCamposMantenimiento(ActualizarMantenimientoEquipoRequestDTO request, MantenimientoEquipo mantenimiento) {
        if (request.getDescripcion() != null) {
            mantenimiento.setDescripcion(request.getDescripcion());
        }
        if (request.getFechaProximaMantenimiento() != null) {
            mantenimiento.setFechaProximaMantenimiento(request.getFechaProximaMantenimiento());
        }
        if (request.getResultadoMantenimiento() != null) {
            mantenimiento.setResultadoMantenimiento(request.getResultadoMantenimiento());
        }
        if (request.getIdEquipo() != null) {
            Equipo equipo = new Equipo();
            equipo.setId(request.getIdEquipo());
            mantenimiento.setEquipo(equipo);
        }
    }

    private void actualizarCamposReserva(ActualizarMantenimientoEquipoRequestDTO request, Reserva reserva) {
        if (request.getNombreReserva() != null) {
            reserva.setNombre(request.getNombreReserva());
        }
        if (request.getDescripcionReserva() != null) {
            reserva.setDescripcion(request.getDescripcionReserva());
        }
        if (request.getFechaReserva() != null) {
            reserva.setFechaReserva(request.getFechaReserva());
        }
        if (request.getHoraInicio() != null) {
            reserva.setHoraInicio(request.getHoraInicio());
        }
        if (request.getHoraFin() != null) {
            reserva.setHoraFin(request.getHoraFin());
        }
    }


    @Override
    protected void afterSave(MantenimientoEquipo detalle) {
        if (detalle.getReserva() != null && detalle.getReserva().getId() != null) {
            reservaRepository.findWithPersonaAndUsuarioById(detalle.getReserva().getId())
                    .ifPresent(reserva -> {
                        if (reserva.getPersona() != null && reserva.getPersona().getUsuario() != null) {
                            String destinatario = reserva.getPersona().getUsuario().getEmail();
                            String asunto = "Confirmación de mantenimiento de equipo";
                            String cuerpo = String.format(PLANTILLA_CORREO_MANTENIMIENTO,
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
