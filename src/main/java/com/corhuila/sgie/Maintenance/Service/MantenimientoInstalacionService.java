package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Maintenance.DTO.ActualizarMantenimientoInstalacionRequestDTO;
import com.corhuila.sgie.Maintenance.DTO.CerrarMantenimientoInstalacionResponseDTO;
import com.corhuila.sgie.Maintenance.DTO.IMantenimientoInstalacionDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IRepository.IMantenimientoInstalacionRepository;
import com.corhuila.sgie.Maintenance.IService.IMantenimientoInstalacionService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MantenimientoInstalacionService extends BaseService<MantenimientoInstalacion> implements IMantenimientoInstalacionService {
    @Autowired
    private IMantenimientoInstalacionRepository repository;
    @Autowired
    private IReservaRepository reservaRepository;
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
    public List<IMantenimientoInstalacionDTO> findMantenimientosInstalacionByNumeroIdentificacion(String numeroIdentificacionPersona) {
        return repository.findMantenimientosInstalacionByNumeroIdentificacion(numeroIdentificacionPersona);
    }

    @Transactional
    public MantenimientoInstalacion actualizarMantenimientoInstalacion(Long idMantenimiento, ActualizarMantenimientoInstalacionRequestDTO request) {
        MantenimientoInstalacion mantenimiento = repository.findById(idMantenimiento)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));

        Reserva reserva = mantenimiento.getReserva();

        if (request.getDescripcion() != null) mantenimiento.setDescripcion(request.getDescripcion());
        if (request.getFechaProximaMantenimiento() != null) mantenimiento.setFechaProximaMantenimiento(request.getFechaProximaMantenimiento());
        if (request.getResultadoMantenimiento() != null) mantenimiento.setResultadoMantenimiento(request.getResultadoMantenimiento());

        if (request.getNombreReserva() != null) reserva.setNombre(request.getNombreReserva());
        if (request.getDescripcionReserva() != null) reserva.setDescripcion(request.getDescripcionReserva());
        if (request.getFechaReserva() != null) reserva.setFechaReserva(request.getFechaReserva());
        if (request.getHoraInicio() != null) reserva.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null) reserva.setHoraFin(request.getHoraFin());

        reserva.setUpdatedAt(LocalDateTime.now());
        mantenimiento.setUpdatedAt(LocalDateTime.now());

        reservaRepository.save(reserva);
        return repository.save(mantenimiento);
    }
}
