package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Booking.DTO.HoraDisponibleDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Maintenance.DTO.ActualizarMantenimientoEquipoRequestDTO;
import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoEquipoResponseDTO;
import com.corhuila.sgie.Maintenance.DTO.IMantenimientoEquipoDTO;
import com.corhuila.sgie.Maintenance.DTO.MantenimientoEquipoResponseDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoEquipoRepository;
import com.corhuila.sgie.Maintenance.IService.IMantenimientoEquipoService;

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
public class MantenimientoEquipoService extends BaseService<MantenimientoEquipo> implements IMantenimientoEquipoService {

    @Autowired
    private IMantenimientoEquipoRepository repository;
    @Autowired
    private IReservaRepository reservaRepository;
    @Override
    protected IBaseRepository<MantenimientoEquipo, Long> getRepository() {
        return repository;
    }

    @Transactional
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
    public List<IMantenimientoEquipoDTO> findMantenimientosEquipoByNumeroIdentificacion(String numeroIdentificacionPersona) {
        return repository.findMantenimientosEquipoByNumeroIdentificacion(numeroIdentificacionPersona);
    }

    @Transactional
    public MantenimientoEquipoResponseDTO actualizarMantenimientoEquipo(
            Long idMantenimiento,
            ActualizarMantenimientoEquipoRequestDTO request) {

        MantenimientoEquipo mantenimiento = repository.findById(idMantenimiento)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        Reserva reserva = mantenimiento.getReserva();

        // Determinar si se cambia fecha/hora o equipo
        boolean cambiaFechaHora = request.getFechaReserva() != null
                || request.getHoraInicio() != null
                || request.getHoraFin() != null;

        if (cambiaFechaHora) {
            LocalDate fecha = request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
            LocalTime horaInicio = request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
            LocalTime horaFin = request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
            Integer idEquipo = mantenimiento.getEquipo().getId().intValue();

            // Consultar horas disponibles excluyendo este mantenimiento
            List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesEquipo(fecha, idEquipo, idMantenimiento);
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

        // Actualizar campos del mantenimiento
        if (request.getDescripcion() != null) mantenimiento.setDescripcion(request.getDescripcion());
        if (request.getFechaProximaMantenimiento() != null) mantenimiento.setFechaProximaMantenimiento(request.getFechaProximaMantenimiento());
        if (request.getResultadoMantenimiento() != null) mantenimiento.setResultadoMantenimiento(request.getResultadoMantenimiento());

        // Actualizar campos de la reserva
        if (request.getNombreReserva() != null) reserva.setNombre(request.getNombreReserva());
        if (request.getDescripcionReserva() != null) reserva.setDescripcion(request.getDescripcionReserva());
        if (request.getFechaReserva() != null) reserva.setFechaReserva(request.getFechaReserva());
        if (request.getHoraInicio() != null) reserva.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null) reserva.setHoraFin(request.getHoraFin());

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
}
