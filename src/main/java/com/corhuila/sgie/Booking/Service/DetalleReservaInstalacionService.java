package com.corhuila.sgie.Booking.Service;

import com.corhuila.sgie.Booking.DTO.IReservaInstalacionDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.Booking.IRepository.IDetalleReservaInstalacionRepository;
import com.corhuila.sgie.Booking.IRepository.IReservaRepository;
import com.corhuila.sgie.Booking.IService.IDetalleReservaInstalacionService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public DetalleReservaInstalacion cerrarDetalleReservaInstalacion(Long idDetalle, String entregaInstalacion) {
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

        return repository.save(detalle);
    }

    @Override
    public List<IReservaInstalacionDTO> findReservaInstalacionByNumeroIdentificacion(String numeroIdentificacionPersona) {
        return repository.findReservaInstalacionByNumeroIdentificacion(numeroIdentificacionPersona);
    }
}
