package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.IReservaEquipoDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaEquipoRepository;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IDetalleReservaEquipoService;

import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public DetalleReservaEquipo cerrarDetalleReservaEquipo(Long idDetalle, String entregaEquipo) {
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

        return repository.save(detalle);
    }

    @Override
    public List<IReservaEquipoDTO> findReservasEquipoByNumeroIdentificacion(String numeroIdentificacionPersona) {
        return repository.findReservasEquipoByNumeroIdentificacion(numeroIdentificacionPersona);
    }
}
