package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.ActualizarReservaDetalleInstalacionRequestDTO;
import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaInstalacionResponseDTO;
import com.corhuila.sgie.Booking.DTO.DetalleReservaInstalacionResponseDTO;
import com.corhuila.sgie.Booking.DTO.IReservaInstalacionDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaInstalacionRepository;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
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
public class DetalleReservaInstalacionService extends BaseService<DetalleReservaInstalacion> implements IDetalleReservaInstalacionService {

    private static final String PLANTILLA_CORREO_RESERVA = """
            Hola %s, tu reserva de la instalacion fue registrada:
            - Fecha: %s
            - Hora inicio: %s
            - Hora fin: %s
            """;

    private final IDetalleReservaInstalacionRepository repository;
    private final IReservaRepository reservaRepository;
    private final NotificacionService notificacionService;

    public DetalleReservaInstalacionService(IDetalleReservaInstalacionRepository repository, IReservaRepository reservaRepository, NotificacionService notificacionService) {
        this.repository = repository;
        this.reservaRepository = reservaRepository;
        this.notificacionService = notificacionService;
    }


    @Override
    protected IBaseRepository<DetalleReservaInstalacion, Long> getRepository() {
        return repository;
    }

    @Override
    protected void beforeSave(DetalleReservaInstalacion detalle) {

        Reserva reserva = detalle.getReserva();

        if (reserva == null || reserva.getFechaReserva() == null || reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            throw new IllegalArgumentException("Fecha y horas son obligatorias.");
        }
        if (!reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            throw new IllegalArgumentException("La hora fin debe ser mayor que la hora inicio.");
        }
        if (detalle.getInstalacion() == null || detalle.getInstalacion().getId() == null) {
            throw new IllegalArgumentException("la instalacion es obligatorio.");
        }

        // DISPONIBILIDAD ***DE EQUIPO*** en creación (idDetalle = null)
        List<LocalTime> disponibles = reservaRepository
                .findHorasDisponiblesEquipo(
                        reserva.getFechaReserva(),
                        detalle.getInstalacion().getId().intValue(),
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
    public CerrarDetalleReservaInstalacionResponseDTO cerrarDetalleReservaInstalacion(Long idDetalle, String entregaInstalacion) {
        DetalleReservaInstalacion detalle = repository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        Reserva reserva = reservaRepository.findById(detalle.getReserva().getId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // actualizar detalle
        detalle.setEntregaInstalacion(entregaInstalacion);
        detalle.setState(false);
        detalle.setUpdatedAt(LocalDateTime.now());

        // verificar si todos los demás detalles ya están cerrados
        boolean todosCerrados = reserva.getDetalleReservaInstalaciones()
                .stream()
                .allMatch(d -> d.getId().equals(idDetalle) || Boolean.FALSE.equals(d.getState()));

        if (todosCerrados) {
            reserva.setState(false);
            reserva.setUpdatedAt(LocalDateTime.now());
            reservaRepository.save(reserva);
        }
        repository.save(detalle);
        return new CerrarDetalleReservaInstalacionResponseDTO(
                detalle.getId(),
                detalle.getState(),
                detalle.getEntregaInstalacion(),
                detalle.getUpdatedAt(),
                reserva.getId());
    }

    @Override
    public List<IReservaInstalacionDTO> findReservaInstalacionByNumeroIdentificacion(String numeroIdentificacion) {
        return repository.findReservaInstalacionByNumeroIdentificacion(numeroIdentificacion);
    }


    @Transactional(rollbackFor = Exception.class)
    public DetalleReservaInstalacionResponseDTO actualizarDetalleReservaInstalacion(
            Long idDetalle,
            ActualizarReservaDetalleInstalacionRequestDTO request) {

        DetalleReservaInstalacion detalle = repository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        Reserva reserva = detalle.getReserva();

        validarDisponibilidadSiCambiaFechaHora(request, reserva, detalle, idDetalle);
        actualizarDatosReserva(request, reserva);
        actualizarDatosDetalle(request, detalle);

        reserva.setUpdatedAt(LocalDateTime.now());
        detalle.setUpdatedAt(LocalDateTime.now());

        reservaRepository.save(reserva);
        DetalleReservaInstalacion guardado = repository.save(detalle);

        return mapearAResponseDTO(guardado, reserva);
    }

    private void validarDisponibilidadSiCambiaFechaHora(
            ActualizarReservaDetalleInstalacionRequestDTO request,
            Reserva reserva,
            DetalleReservaInstalacion detalle,
            Long idDetalle) {

        boolean cambiaFechaHoraInstalacion =
                request.getFechaReserva() != null ||
                        request.getHoraInicio() != null ||
                        request.getHoraFin() != null ||
                        request.getIdInstalacion() != null;

        if (cambiaFechaHoraInstalacion) {
            validarHorasDisponiblesInstalacion(request, reserva, detalle, idDetalle);
        }
    }

    private Integer obtenerIdInstalacionEfectiva(ActualizarReservaDetalleInstalacionRequestDTO request,
                                                 DetalleReservaInstalacion detalle) {
        return (request.getIdInstalacion() != null)
                ? request.getIdInstalacion().intValue()
                : detalle.getInstalacion().getId().intValue();
    }

    private void validarHorasDisponiblesInstalacion(
            ActualizarReservaDetalleInstalacionRequestDTO request,
            Reserva reserva,
            DetalleReservaInstalacion detalle,
            Long idDetalle) {

        LocalDate fecha = obtenerFechaEfectiva(request, reserva);
        LocalTime horaInicio = obtenerHoraInicioEfectiva(request, reserva);
        LocalTime horaFin = obtenerHoraFinEfectiva(request, reserva);
        Integer idInstalacion = obtenerIdInstalacionEfectiva(request, detalle);

        List<LocalTime> disponibles = obtenerHorasDisponiblesInstalacion(fecha, idInstalacion, idDetalle);
        List<LocalTime> rangoSolicitado = generarRangoHorario(horaInicio, horaFin);

        if (!disponibles.containsAll(rangoSolicitado)) {
            throw new IllegalStateException(
                    "El rango de horas seleccionado no está disponible para la instalación.");
        }
    }

    private LocalDate obtenerFechaEfectiva(ActualizarReservaDetalleInstalacionRequestDTO request, Reserva reserva) {
        return request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
    }

    private LocalTime obtenerHoraInicioEfectiva(ActualizarReservaDetalleInstalacionRequestDTO request, Reserva reserva) {
        return request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
    }

    private LocalTime obtenerHoraFinEfectiva(ActualizarReservaDetalleInstalacionRequestDTO request, Reserva reserva) {
        return request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
    }

    private List<LocalTime> obtenerHorasDisponiblesInstalacion(LocalDate fecha, Integer idInstalacion, Long idDetalle) {
        List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesInstalacion(fecha, idInstalacion, idDetalle);
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

    private void actualizarDatosReserva(ActualizarReservaDetalleInstalacionRequestDTO request, Reserva reserva) {
        if (request.getNombreReserva() != null) reserva.setNombre(request.getNombreReserva());
        if (request.getDescripcionReserva() != null) reserva.setDescripcion(request.getDescripcionReserva());
        if (request.getFechaReserva() != null) reserva.setFechaReserva(request.getFechaReserva());
        if (request.getHoraInicio() != null) reserva.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null) reserva.setHoraFin(request.getHoraFin());
    }

    private void actualizarDatosDetalle(ActualizarReservaDetalleInstalacionRequestDTO request, DetalleReservaInstalacion detalle) {
        if (request.getProgramaAcademico() != null) detalle.setProgramaAcademico(request.getProgramaAcademico());
        if (request.getNumeroEstudiantes() != null) detalle.setNumeroEstudiantes(request.getNumeroEstudiantes());

        if (request.getIdInstalacion() != null) {
            Instalacion instalacion = new Instalacion();
            instalacion.setId(request.getIdInstalacion());
            detalle.setInstalacion(instalacion);
        }
    }

    private DetalleReservaInstalacionResponseDTO mapearAResponseDTO(DetalleReservaInstalacion guardado, Reserva reserva) {
        return new DetalleReservaInstalacionResponseDTO(
                guardado.getId(),
                reserva.getNombre(),
                reserva.getDescripcion(),
                reserva.getFechaReserva(),
                reserva.getHoraInicio(),
                reserva.getHoraFin(),
                guardado.getProgramaAcademico(),
                guardado.getNumeroEstudiantes(),
                guardado.getInstalacion().getId(),
                guardado.getInstalacion().getNombre(),
                reserva.getPersona() != null ? reserva.getPersona().getNombres() : null,
                reserva.getTipoReserva() != null ? reserva.getTipoReserva().getNombre() : null
        );
    }


    @Override
    protected void afterSave(DetalleReservaInstalacion detalle) {
        if (detalle.getReserva() != null && detalle.getReserva().getId() != null) {
            reservaRepository.findWithPersonaAndUsuarioById(detalle.getReserva().getId())
                    .ifPresent(reserva -> {
                        if (reserva.getPersona() != null && reserva.getPersona().getUsuario() != null) {
                            String destinatario = reserva.getPersona().getUsuario().getEmail();
                            String asunto = "Confirmación de reserva de instalación";
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
