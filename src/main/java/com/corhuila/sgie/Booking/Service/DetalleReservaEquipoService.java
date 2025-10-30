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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleReservaEquipoService extends BaseService<DetalleReservaEquipo> implements IDetalleReservaEquipoService {

    private static final String PLANTILLA_CORREO_RESERVA = """
                Hola %s, tu reserva del equipo fue registrada:
                - Fecha: %s
                - Hora inicio: %s
                - Hora fin: %s
            """;

    private final IDetalleReservaEquipoRepository repository;
    private final IReservaRepository reservaRepository;
    private final NotificacionService notificacionService;

    public DetalleReservaEquipoService(IDetalleReservaEquipoRepository repository, IReservaRepository reservaRepository, NotificacionService notificacionService) {
        this.repository = repository;
        this.reservaRepository = reservaRepository;
        this.notificacionService = notificacionService;
    }

    @Override
    protected IBaseRepository<DetalleReservaEquipo, Long> getRepository() {
        return repository;
    }


    @Override
    protected void beforeSave(DetalleReservaEquipo detalle) {
        Reserva reserva = detalle.getReserva();

        if (reserva == null || reserva.getFechaReserva() == null || reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            throw new IllegalArgumentException("Fecha y horas son obligatorias.");
        }
        if (!reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            throw new IllegalArgumentException("La hora fin debe ser mayor que la hora inicio.");
        }
        if (detalle.getEquipo() == null || detalle.getEquipo().getId() == null) {
            throw new IllegalArgumentException("El equipo es obligatorio.");
        }

        if (detalle.getInstalacionDestino() == null || detalle.getInstalacionDestino().getId() == null) {
            throw new IllegalArgumentException("La instalacion destino es obligatorio.");
        }

        // DISPONIBILIDAD ***DE EQUIPO*** en creación (idDetalle = null)
        List<LocalTime> disponibles = reservaRepository
                .findHorasDisponiblesEquipo(
                        reserva.getFechaReserva(),
                        detalle.getEquipo().getId().intValue(),
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

    @Transactional(rollbackFor = Exception.class)
    public DetalleReservaEquipoResponseDTO actualizarDetalleReservaEquipo(
            Long idDetalle,
            ActualizarReservaDetalleEquipoRequestDTO request) {

        DetalleReservaEquipo detalle = repository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        Reserva reserva = detalle.getReserva();

        validarDisponibilidadSiEsNecesario(request, reserva, detalle, idDetalle);
        actualizarDatosReserva(request, reserva);
        actualizarDatosDetalle(request, detalle);

        reserva.setUpdatedAt(LocalDateTime.now());
        detalle.setUpdatedAt(LocalDateTime.now());

        reservaRepository.save(reserva);
        DetalleReservaEquipo guardado = repository.save(detalle);

        return mapearAResponseDTO(guardado, reserva);
    }

    private void validarDisponibilidadSiEsNecesario(
            ActualizarReservaDetalleEquipoRequestDTO request,
            Reserva reserva,
            DetalleReservaEquipo detalle,
            Long idDetalle) {

        boolean cambiaFechaHoraInstalacion =
                request.getFechaReserva() != null ||
                        request.getHoraInicio() != null ||
                        request.getHoraFin() != null ||
                        request.getIdEquipo() != null;

        boolean cambiaEquipoOInstalacion = request.getIdEquipo() != null
                || request.getIdInstalacionDestino() != null;

        if (cambiaFechaHoraInstalacion || cambiaEquipoOInstalacion) {
            validarHorasDisponibles(request, reserva, detalle, idDetalle);
        }
    }

    private void validarHorasDisponibles(
            ActualizarReservaDetalleEquipoRequestDTO request,
            Reserva reserva,
            DetalleReservaEquipo detalle,
            Long idDetalle) {

        LocalDate fecha = obtenerFechaEfectiva(request, reserva);
        LocalTime horaInicio = obtenerHoraInicioEfectiva(request, reserva);
        LocalTime horaFin = obtenerHoraFinEfectiva(request, reserva);
        Integer idEquipo = obtenerIdEquipoEfectivo(request, detalle);

        List<LocalTime> disponibles = obtenerHorasDisponibles(fecha, idEquipo, idDetalle);
        List<LocalTime> rangoSolicitado = generarRangoHorario(horaInicio, horaFin);

        if (!disponibles.containsAll(rangoSolicitado)) {
            throw new IllegalStateException(
                    "El rango de horas seleccionado no está disponible para el equipo y fecha seleccionados.");
        }
    }

    private LocalDate obtenerFechaEfectiva(ActualizarReservaDetalleEquipoRequestDTO request, Reserva reserva) {
        return request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
    }

    private LocalTime obtenerHoraInicioEfectiva(ActualizarReservaDetalleEquipoRequestDTO request, Reserva reserva) {
        return request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
    }

    private LocalTime obtenerHoraFinEfectiva(ActualizarReservaDetalleEquipoRequestDTO request, Reserva reserva) {
        return request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
    }

    private Integer obtenerIdEquipoEfectivo(ActualizarReservaDetalleEquipoRequestDTO request, DetalleReservaEquipo detalle) {
        return request.getIdEquipo() != null ? request.getIdEquipo().intValue() : detalle.getEquipo().getId().intValue();
    }

    private List<LocalTime> obtenerHorasDisponibles(LocalDate fecha, Integer idEquipo, Long idDetalle) {
        List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesEquipo(fecha, idEquipo, idDetalle);
        return horasDisponibles.stream()
                .map(h -> LocalTime.parse(h[0].toString()))
                .toList();
    }

    private List<LocalTime> generarRangoHorario(LocalTime horaInicio, LocalTime horaFin) {
        List<LocalTime> rango = new ArrayList<>();
        for (LocalTime h = horaInicio; h.isBefore(horaFin); h = h.plusHours(1)) {
            rango.add(h);
        }
        return rango;
    }

    private void actualizarDatosReserva(ActualizarReservaDetalleEquipoRequestDTO request, Reserva reserva) {
        if (request.getNombreReserva() != null) reserva.setNombre(request.getNombreReserva());
        if (request.getDescripcionReserva() != null) reserva.setDescripcion(request.getDescripcionReserva());
        if (request.getFechaReserva() != null) reserva.setFechaReserva(request.getFechaReserva());
        if (request.getHoraInicio() != null) reserva.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null) reserva.setHoraFin(request.getHoraFin());
    }

    private void actualizarDatosDetalle(ActualizarReservaDetalleEquipoRequestDTO request, DetalleReservaEquipo detalle) {
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
    }

    private DetalleReservaEquipoResponseDTO mapearAResponseDTO(DetalleReservaEquipo guardado, Reserva reserva) {
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
                            String cuerpo = String.format(PLANTILLA_CORREO_RESERVA,
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
