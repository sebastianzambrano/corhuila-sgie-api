package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.DTO.IReservaEquipoDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDetalleReservaEquipoRepository extends IBaseRepository<DetalleReservaEquipo,Long> {
    @Query(value = """
        SELECT 
            tr.nombre AS tipoReserva, 
            re.nombre AS nombreReserva, 
            pe.nombres AS nombrePersona,
            pe.numero_identificacion AS numeroIdentificacionPersona,
            ins.nombre AS nombreInstalacion, 
            re.fecha_reserva AS fechaReserva,
            re.hora_inicio AS horaInicioReserva,
            re.hora_fin AS horaFinReserva,
            eq.nombre AS nombreEquipo
        FROM detalle_reserva_equipo dre
        INNER JOIN reserva re ON dre.id_reserva = re.id
        INNER JOIN instalacion ins ON dre.id_instalacion_destino = ins.id
        INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
        INNER JOIN persona pe ON re.id_persona = pe.id
        INNER JOIN equipo eq ON dre.id_equipo = eq.id
        WHERE (:numeroIdentificacionPersona IS NULL OR :numeroIdentificacionPersona = '' OR pe.numero_identificacion = :numeroIdentificacionPersona)
        """, nativeQuery = true)
    List<IReservaEquipoDTO> findReservasEquipoByNumeroIdentificacion(
            @Param("numeroIdentificacionPersona") String numeroIdentificacionPersona
    );
}
