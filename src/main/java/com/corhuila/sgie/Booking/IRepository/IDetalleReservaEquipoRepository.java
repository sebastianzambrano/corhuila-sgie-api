package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.DTO.IReservaEquipoDTO;
import com.corhuila.sgie.Booking.Entity.DetalleReservaEquipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDetalleReservaEquipoRepository extends IBaseRepository<DetalleReservaEquipo, Long> {
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
                te.nombre AS nombreEquipo,
                re.state AS estadoReserva,
                dre.state AS estadoDetalleReservaEquipo
            FROM detalle_reserva_equipo dre
            INNER JOIN reserva re ON dre.id_reserva = re.id
            INNER JOIN instalacion ins ON dre.id_instalacion_destino = ins.id
            INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            INNER JOIN persona pe ON re.id_persona = pe.id
            INNER JOIN equipo eq ON dre.id_equipo = eq.id
            INNER JOIN tipo_equipo te ON eq.id_tipo_equipo = te.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            """, nativeQuery = true)
    List<IReservaEquipoDTO> findReservasEquipoByNumeroIdentificacion(
            @Param("numeroIdentificacion") String numeroIdentificacion
    );

    @Query(value = """
            SELECT 
                re.fecha_reserva AS fechaReserva,
                re.hora_inicio AS horaInicio,
                re.hora_fin AS horaFin,
                re.nombre AS nombreReserva,
                pe.nombres AS persona,
                dre.programa_academico AS programaAcademico
            FROM detalle_reserva_equipo dre
            INNER JOIN reserva re ON dre.id_reserva = re.id
            INNER JOIN persona pe ON re.id_persona = pe.id
            WHERE dre.id_equipo = :idEquipo
            ORDER BY re.fecha_reserva DESC
            """, nativeQuery = true)
    List<Object[]> findHistorialReservasByEquipo(@Param("idEquipo") Long idEquipo);
}
