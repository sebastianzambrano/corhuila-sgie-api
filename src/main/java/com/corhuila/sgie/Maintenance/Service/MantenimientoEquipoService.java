package com.corhuila.sgie.Maintenance.Service;

import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Maintenance.DTO.IMantenimientoEquipoDTO;
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
    public MantenimientoEquipo cerrarMantenimientoEquipo(
            Long idMantenimiento,
            LocalDate fechaProximaMantenimiento,
            String resultadoMantenimiento) {

        MantenimientoEquipo mantenimiento = repository.findById(idMantenimiento)
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

        return repository.save(mantenimiento);
    }

    @Override
    public List<IMantenimientoEquipoDTO> findMantenimientosEquipoByNumeroIdentificacion(String numeroIdentificacionPersona) {
        return repository.findMantenimientosEquipoByNumeroIdentificacion(numeroIdentificacionPersona);
    }
}
