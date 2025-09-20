package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.ActualizarReservaDetalleEquipoRequestDTO;
import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaEquipoResponseDTO;
import com.corhuila.sgie.Booking.DTO.IReservaEquipoDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaEquipoRepository;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IDetalleReservaEquipoService;

import com.corhuila.sgie.Equipment.Entity.Equipo;
import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DetalleReservaEquipoService extends BaseService<DetalleReservaEquipo> implements IDetalleReservaEquipoService {
    @Autowired
    private IDetalleReservaEquipoRepository repository;
    @Autowired
    private IReservaRepository reservaRepository;
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
    public List<IReservaEquipoDTO> findReservasEquipoByNumeroIdentificacion(String numeroIdentificacionPersona) {
        return repository.findReservasEquipoByNumeroIdentificacion(numeroIdentificacionPersona);
    }

    @Transactional
    public DetalleReservaEquipo actualizarDetalleReservaEquipo(Long idDetalle, ActualizarReservaDetalleEquipoRequestDTO request) {
        DetalleReservaEquipo detalle = repository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        Reserva reserva = detalle.getReserva();

        // Si cambia fecha/hora o equipo/instalacion, validar horas disponibles
        boolean cambiaFechaHora = request.getFechaReserva() != null || request.getHoraInicio() != null || request.getHoraFin() != null;
        boolean cambiaEquipoOInstalacion = request.getIdEquipo() != null || request.getIdInstalacionDestino() != null;

        if (cambiaFechaHora || cambiaEquipoOInstalacion) {
            // determinamos los valores efectivos que quedarán (si no vienen, se usan los actuales)
            LocalDate fecha = request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
            LocalTime horaInicio = request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
            LocalTime horaFin = request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
            Integer idEquipo = request.getIdEquipo() != null ? request.getIdEquipo().intValue() : detalle.getEquipo().getId().intValue();

            // consultar horas disponibles para ese equipo en la fecha
            List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesEquipo(fecha, idEquipo);

            // Validación simple: comprobar que la horaInicio solicitada esté en la lista de disponibles
            boolean inicioDisponible = horasDisponibles.stream()
                    .anyMatch(h -> h[0].toString().equals(horaInicio.toString()));

            if (!inicioDisponible) {
                throw new RuntimeException("La hora de inicio no está disponible para el equipo y fecha seleccionados.");
            }

            // Si deseas validar todo el rango (inicio-fin) deberíamos comprobar que
            // no exista una reserva con intervalo que se solape. Puedo implementarlo si quieres.
        }

        // actualizar solo si vienen datos en el request
        if (request.getNombreReserva() != null) reserva.setNombre(request.getNombreReserva());
        if (request.getDescripcionReserva() != null) reserva.setDescripcion(request.getDescripcionReserva());
        if (request.getFechaReserva() != null) reserva.setFechaReserva(request.getFechaReserva());
        if (request.getHoraInicio() != null) reserva.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null) reserva.setHoraFin(request.getHoraFin());

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
        return repository.save(detalle);
    }
}
