package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.ActualizarReservaDetalleInstalacionRequestDTO;
import com.corhuila.sgie.Booking.DTO.CerrarDetalleReservaInstalacionResponseDTO;
import com.corhuila.sgie.Booking.DTO.IReservaInstalacionDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaInstalacionRepository;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
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
public class DetalleReservaInstalacionService extends BaseService<DetalleReservaInstalacion> implements IDetalleReservaInstalacionService {
    @Autowired
    private IDetalleReservaInstalacionRepository repository;
    @Autowired
    private IReservaRepository reservaRepository;
    @Override
    protected IBaseRepository<DetalleReservaInstalacion, Long> getRepository() {
        return repository;
    }

    @Transactional
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
    public List<IReservaInstalacionDTO> findReservaInstalacionByNumeroIdentificacion(String numeroIdentificacionPersona) {
        return repository.findReservaInstalacionByNumeroIdentificacion(numeroIdentificacionPersona);
    }

    @Transactional
    public DetalleReservaInstalacion actualizarDetalleReserva(
            Long idDetalle, ActualizarReservaDetalleInstalacionRequestDTO request) {

        DetalleReservaInstalacion detalle = repository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        Reserva reserva = detalle.getReserva();

        // Validar solapamiento si el usuario cambia fecha/hora
        if (request.getFechaReserva() != null || request.getHoraInicio() != null || request.getHoraFin() != null) {

            LocalDate fecha = request.getFechaReserva() != null ? request.getFechaReserva() : reserva.getFechaReserva();
            LocalTime horaInicio = request.getHoraInicio() != null ? request.getHoraInicio() : reserva.getHoraInicio();
            LocalTime horaFin = request.getHoraFin() != null ? request.getHoraFin() : reserva.getHoraFin();
            Integer idInstalacion = detalle.getInstalacion().getId().intValue();

            List<Object[]> horasDisponibles = reservaRepository.findHorasDisponiblesInstalacion(fecha, idInstalacion);

            boolean disponible = horasDisponibles.stream()
                    .anyMatch(h -> h[0].toString().equals(horaInicio.toString()));

            if (!disponible) {
                throw new RuntimeException("La hora seleccionada no está disponible para la instalación.");
            }
        }

        // actualizar solo si vienen datos en el request
        if (request.getNombreReserva() != null) reserva.setNombre(request.getNombreReserva());
        if (request.getDescripcionReserva() != null) reserva.setDescripcion(request.getDescripcionReserva());
        if (request.getFechaReserva() != null) reserva.setFechaReserva(request.getFechaReserva());
        if (request.getHoraInicio() != null) reserva.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null) reserva.setHoraFin(request.getHoraFin());

        if (request.getProgramaAcademico() != null) detalle.setProgramaAcademico(request.getProgramaAcademico());
        if (request.getNumeroEstudiantes() != null) detalle.setNumeroEstudiantes(request.getNumeroEstudiantes());
        if (request.getIdInstalacion() != null) {
            Instalacion instalacion = new Instalacion();
            instalacion.setId(request.getIdInstalacion());
            detalle.setInstalacion(instalacion);
        }

        reserva.setUpdatedAt(LocalDateTime.now());
        detalle.setUpdatedAt(LocalDateTime.now());

        reservaRepository.save(reserva);
        return repository.save(detalle);
    }

}
