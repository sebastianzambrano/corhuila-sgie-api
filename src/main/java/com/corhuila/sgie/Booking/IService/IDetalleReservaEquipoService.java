package com.corhuila.sgie.Booking.IService;

import com.corhuila.sgie.Booking.DTO.IReservaEquipoDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.common.IBaseService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDetalleReservaEquipoService extends IBaseService<DetalleReservaEquipo> {
    List<IReservaEquipoDTO> findReservasEquipoByNumeroIdentificacion(
            @Param("numeroIdentificacionPersona") String numeroIdentificacionPersona
    );
}
