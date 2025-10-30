package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Maintenance.DTO.ActualizarMantenimientoInstalacionRequestDTO;
import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoInstalacionResponseDTO;
import com.corhuila.sgie.Maintenance.DTO.IMantenimientoInstalacionDTO;
import com.corhuila.sgie.Maintenance.DTO.MantenimientoInstalacionResponseDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoInstalacionRepository;
import com.corhuila.sgie.Maintenance.IService.IMantenimientoInstalacionService;
import com.corhuila.sgie.Notification.NotificacionService;
import com.corhuila.sgie.Site.Entity.Instalacion;
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
public class MantenimientoInstalacionService extends BaseService<MantenimientoInstalacion> implements IMantenimientoInstalacionService {

    private static final String PLANTILLA_CORREO_MANTENIMIENTO = """
                Hola %s, tu reserva de mantenimiento de instalacion fue registrada:
                - Fecha: %s
                - Hora inicio: %s
                - Hora fin: %s
            """;

    private final IMantenimientoInstalacionRepository repository;
    private final IReservaRepository reservaRepository;
    private final NotificacionService notificacionService;

    public MantenimientoInstalacionService(IMantenimientoInstalacionRepository repository, IReservaRepository reservaRepository, NotificacionService notificacionService) {
        this.repository = repository;
        this.reservaRepository = reservaRepository;
        this.notificacionService = notificacionService;
    }

    @Override
    protected IBaseRepository<MantenimientoInstalacion, Long> getRepository() {
        return repository;
    }

    @Override
    protected void beforeSave(MantenimientoInstalacion mantenimiento) {

        Reserva reserva = mantenimiento.getReserva();

        if (reserva == null || reserva.getFechaReserva() == null || reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            throw new IllegalArgumentException("Fecha y horas son obligatorias.");
        }
        if (!reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            throw new IllegalArgumentException("La hora fin debe ser mayor que la hora inicio.");
        }
        if (mantenimiento.getInstalacion() == null || mantenimiento.getInstalacion().getId() == null) {
            throw new IllegalArgumentException("la instalacion es obligatorio.");
        }

        // DISPONIBILIDAD ***DE EQUIPO*** en creación (idDetalle = null)
        List<LocalTime> disponibles = reservaRepository
                .findHorasDisponiblesInstalacion(
                        reserva.getFechaReserva(),
                        mantenimiento.getInstalacion().getId().intValue(),
                        null // creación
                )
                .stream()
                .map(h -> LocalTime.parse(h[0].toString()))
                .toList();

        List<LocalTime> rango = generarRangoHorario(reserva.getHoraInicio(), reserva.getHoraFin());

        if (!disponibles.containsAll(rango)) {
            throw new IllegalStateException("El rango no está disponible de instalacion.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public CerrarMantenimientoInstalacionResponseDTO cerrarMantenimientoInstalacion(
            Long idMantenimiento,
            LocalDate fechaProximaMantenimiento,
            String resultadoMantenimiento) {

        MantenimientoInstalacion mantenimiento = repository.findById(idMantenimiento)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        Reserva reserva = mantenimiento.getReserva();

        // actualizar mantenimiento
        mantenimiento.setFechaProximaMantenimiento(fechaProximaMantenimiento);
        mantenimiento.setResultadoMantenimiento(resultadoMantenimiento);
        mantenimiento.setState(false);
        mantenimiento.setUpdatedAt(LocalDateTime.now());

        // cerrar reserva directamente porque es 1:1
        reserva.setState(false);
        reserva.setUpdatedAt(LocalDateTime.now());
        reservaRepository.save(reserva);

        MantenimientoInstalacion saved = repository.save(mantenimiento);

        return new CerrarMantenimientoInstalacionResponseDTO(
                saved.getId(),
                saved.getState(),
                saved.getFechaProximaMantenimiento(),
                saved.getResultadoMantenimiento(),
                saved.getUpdatedAt(),
                reserva.getId()
        );
    }

    @Override
    public List<IMantenimientoInstalacionDTO> findMantenimientosInstalacionByNumeroIdentificacion(String numeroIdentificacion) {
        return repository.findMantenimientosInstalacionByNumeroIdentificacion(numeroIdentificacion);
    }


    @Transactional(rollbackFor = Exception.class)
    public MantenimientoInstalacionResponseDTO actualizarMantenimientoInstalacion(
            Long idMantenimiento,
            ActualizarMantenimientoInstalacionRequestDTO request) {

        MantenimientoInstalacion mantenimiento = repository.findById(idMantenimiento)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        Reserva reserva = mantenimiento.getReserva();

        validarDisponibilidadSiCambiaFechaHora(request, reserva, mantenimiento, idMantenimiento);
        actualizarCamposMantenimiento(request, mantenimiento);
        actualizarCamposReserva(request, reserva);

        reserva.setUpdatedAt(LocalDateTime.now());
        mantenimiento.setUpdatedAt(LocalDateTime.now());

        reservaRepository.save(reserva);
        MantenimientoInstalacion guardado = repository.save(mantenimiento);

        return new MantenimientoInstalacionResponseDTO(
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
            ActualizarMantenimientoInstalacionRequestDTO request,
            Reserva reserva,
            MantenimientoInstalacion mantenimiento,
            Long idMantenimiento) {

        boolean cambiaFechaHora =
                request.getFechaReserva() != null ||
                        request.getHoraInicio() != null ||
                        request.getHoraFin() != null ||
                        request.getIdInstalacion() != null;

        if (!cambiaFechaHora) {
            return;
        }

        LocalDate fecha = request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
        LocalTime horaInicio = request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
        LocalTime horaFin = request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
        Integer idInstalacion = obtenerIdInstalacionEfectiva(request, mantenimiento);

        validarHorasDisponibles(fecha, horaInicio, horaFin, idInstalacion, idMantenimiento);
    }

    private Integer obtenerIdInstalacionEfectiva(ActualizarMantenimientoInstalacionRequestDTO request,
                                                 MantenimientoInstalacion mantenimiento) {
        return (request.getIdInstalacion() != null)
                ? request.getIdInstalacion().intValue()
                : mantenimiento.getInstalacion().getId().intValue();
    }

    private void validarHorasDisponibles(
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            Integer idInstalacion,
            Long idMantenimiento) {

        List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesInstalacion(fecha, idInstalacion, idMantenimiento);
        List<LocalTime> disponibles = horasDisponibles.stream()
                .map(h -> LocalTime.parse(h[0].toString()))
                .toList();

        List<LocalTime> rangoSolicitado = generarRangoHorario(horaInicio, horaFin);

        if (!disponibles.containsAll(rangoSolicitado)) {
            throw new IllegalStateException("El rango de horas seleccionado no está disponible para la instalación y fecha seleccionados.");
        }
    }

    private List<LocalTime> generarRangoHorario(LocalTime horaInicio, LocalTime horaFin) {
        List<LocalTime> rango = new ArrayList<>();
        for (LocalTime h = horaInicio; h.isBefore(horaFin); h = h.plusHours(1)) {
            rango.add(h);
        }
        return rango;
    }

    private void actualizarCamposMantenimiento(ActualizarMantenimientoInstalacionRequestDTO request, MantenimientoInstalacion mantenimiento) {
        if (request.getDescripcion() != null) {
            mantenimiento.setDescripcion(request.getDescripcion());
        }
        if (request.getFechaProximaMantenimiento() != null) {
            mantenimiento.setFechaProximaMantenimiento(request.getFechaProximaMantenimiento());
        }
        if (request.getResultadoMantenimiento() != null) {
            mantenimiento.setResultadoMantenimiento(request.getResultadoMantenimiento());
        }
        if (request.getIdInstalacion() != null) {
            Instalacion instalacion = new Instalacion();
            instalacion.setId(request.getIdInstalacion());
            mantenimiento.setInstalacion(instalacion);
        }
    }

    private void actualizarCamposReserva(ActualizarMantenimientoInstalacionRequestDTO request, Reserva reserva) {
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
    protected void afterSave(MantenimientoInstalacion detalle) {
        if (detalle.getReserva() != null && detalle.getReserva().getId() != null) {
            reservaRepository.findWithPersonaAndUsuarioById(detalle.getReserva().getId())
                    .ifPresent(reserva -> {
                        if (reserva.getPersona() != null && reserva.getPersona().getUsuario() != null) {
                            String destinatario = reserva.getPersona().getUsuario().getEmail();
                            String asunto = "Confirmación Mantenimiento de instalación";
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
