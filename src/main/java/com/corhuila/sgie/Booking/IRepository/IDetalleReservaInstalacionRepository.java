package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.DTO.IReservaInstalacionDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaInstalacion;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDetalleReservaInstalacionRepository extends IBaseRepository<DetalleReservaInstalacion, Long> {
    @Query(value = """
            SELECT 
                tr.nombre AS tipoReserva, 
                re.nombre AS nombreReserva, 
                pe.nombres AS nombrePersona,
                pe.numero_identificacion AS numeroIdentificacion,
                ins.nombre AS nombreInstalacion, 
                re.fecha_reserva AS fechaReserva,
                re.hora_inicio AS horaInicioReserva,
                re.hora_fin AS horaFinReserva,
                re.state AS estadoReserva,
                dri.state AS estadoDetalleReservaInstalacion
            FROM detalle_reserva_instalacion dri
            INNER JOIN reserva re ON dri.id_reserva = re.id
            INNER JOIN instalacion ins ON dri.id_instalacion = ins.id
            INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            INNER JOIN persona pe ON re.id_persona = pe.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            """, nativeQuery = true)
    List<IReservaInstalacionDTO> findReservaInstalacionByNumeroIdentificacion(
            @Param("numeroIdentificacion") String numeroIdentificacion
    );
}
