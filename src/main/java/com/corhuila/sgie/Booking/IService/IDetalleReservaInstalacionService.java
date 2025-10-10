package com.corhuila.sgie.Booking.IService;

import com.corhuila.sgie.Booking.DTO.IReservaInstalacionDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.common.IBaseService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDetalleReservaInstalacionService extends IBaseService<DetalleReservaInstalacion> {

    List<IReservaInstalacionDTO> findReservaInstalacionByNumeroIdentificacion(
            @Param("numeroIdentificacion") String numeroIdentificacion
    );
}
