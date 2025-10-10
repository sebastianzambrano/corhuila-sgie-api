package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Maintenance.DTO.ActualizarMantenimientoInstalacionRequestDTO;
import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoInstalacionResponseDTO;
import com.corhuila.sgie.Maintenance.DTO.IMantenimientoInstalacionDTO;
import com.corhuila.sgie.Maintenance.DTO.MantenimientoInstalacionResponseDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoInstalacionRepository;
import com.corhuila.sgie.Maintenance.IService.IMantenimientoInstalacionService;
import com.corhuila.sgie.Notification.NotificacionService;
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
public class MantenimientoInstalacionService extends BaseService<MantenimientoInstalacion> implements IMantenimientoInstalacionService {
    @Autowired
    private IMantenimientoInstalacionRepository repository;
    @Autowired
    private IReservaRepository reservaRepository;
    @Autowired
    private NotificacionService notificacionService;
    @Override
    protected IBaseRepository<MantenimientoInstalacion, Long> getRepository() {
        return repository;
    }

    @Transactional
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

    @Transactional
    public MantenimientoInstalacionResponseDTO actualizarMantenimientoInstalacion(
            Long idMantenimiento,
            ActualizarMantenimientoInstalacionRequestDTO request) {

        MantenimientoInstalacion mantenimiento = repository.findById(idMantenimiento)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        Reserva reserva = mantenimiento.getReserva();

        // Determinar si se cambia fecha/hora o instalación
        boolean cambiaFechaHora = request.getFechaReserva() != null
                || request.getHoraInicio() != null
                || request.getHoraFin() != null;

        if (cambiaFechaHora) {
            LocalDate fecha = request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
            LocalTime horaInicio = request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
            LocalTime horaFin = request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
            Integer idInstalacion = mantenimiento.getInstalacion().getId().intValue();

            // Consultar horas disponibles excluyendo este mantenimiento
            List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesInstalacion(fecha, idInstalacion, idMantenimiento);
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
                throw new RuntimeException("El rango de horas seleccionado no está disponible para la instalación y fecha seleccionados.");
            }
        }

        // Actualizar campos del mantenimiento
        if (request.getDescripcion() != null) mantenimiento.setDescripcion(request.getDescripcion());
        if (request.getFechaProximaMantenimiento() != null) mantenimiento.setFechaProximaMantenimiento(request.getFechaProximaMantenimiento());
        if (request.getResultadoMantenimiento() != null) mantenimiento.setResultadoMantenimiento(request.getResultadoMantenimiento());

        // Actualizar campos de la reserva relacionados (opcionales)
        if (request.getNombreReserva() != null) reserva.setNombre(request.getNombreReserva());
        if (request.getDescripcionReserva() != null) reserva.setDescripcion(request.getDescripcionReserva());
        if (request.getFechaReserva() != null) reserva.setFechaReserva(request.getFechaReserva());
        if (request.getHoraInicio() != null) reserva.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null) reserva.setHoraFin(request.getHoraFin());

        reserva.setUpdatedAt(LocalDateTime.now());
        mantenimiento.setUpdatedAt(LocalDateTime.now());

        reservaRepository.save(reserva);
        MantenimientoInstalacion guardado = repository.save(mantenimiento);

        // Mapear a DTO ligero
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
    @Override
    protected void afterSave(MantenimientoInstalacion detalle) {
        if (detalle.getReserva() != null && detalle.getReserva().getId() != null) {
            reservaRepository.findWithPersonaAndUsuarioById(detalle.getReserva().getId())
                    .ifPresent(reserva -> {
                        if (reserva.getPersona() != null && reserva.getPersona().getUsuario() != null) {
                            String destinatario = reserva.getPersona().getUsuario().getEmail();
                            String asunto = "Confirmación Mantenimiento de instalación";
                            String cuerpo = String.format("""
                                                Hola %s, tu reserva de mantenimiento de instalacion fue registrada:
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
