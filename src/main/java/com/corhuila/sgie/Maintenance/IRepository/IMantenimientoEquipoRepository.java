package com.corhuila.sgie.Maintenance.IRepository;

import com.corhuila.sgie.Maintenance.DTO.IMantenimientoEquipoDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMantenimientoEquipoRepository extends IBaseRepository<MantenimientoEquipo, Long> {
    @Query(value = """
            SELECT 
                tr.nombre AS tipoReserva, 
                re.nombre AS nombreReserva, 
                pe.nombres AS nombrePersona,
                pe.numero_identificacion AS numeroIdentificacion,
                re.fecha_reserva AS fechaReserva,
                re.hora_inicio AS horaInicioReserva,
                re.hora_fin AS horaFinReserva,
                te.nombre AS nombreEquipo,
                cme.nombre AS tipoMantenimiento,
                me.state AS estadoMantenimiento,
                re.state AS estadoReserva
            FROM mantenimiento_equipo me
            INNER JOIN reserva re ON me.id_reserva = re.id
            INNER JOIN equipo eq ON me.id_equipo = eq.id
            INNER JOIN categoria_mantenimiento_equipo cme ON me.id_categoria_mantenimiento_equipo = cme.id
            INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            INNER JOIN persona pe ON re.id_persona = pe.id
            INNER JOIN tipo_equipo te ON eq.id_tipo_equipo = te.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            """, nativeQuery = true)
    List<IMantenimientoEquipoDTO> findMantenimientosEquipoByNumeroIdentificacion(
            @Param("numeroIdentificacion") String numeroIdentificacion
    );

    @Query(value = """
            SELECT 
                me.fecha_proxima_mantenimiento,
                me.descripcion,
                me.resultado_mantenimiento,
                cme.nombre AS categoriaMantenimiento
            FROM mantenimiento_equipo me
            INNER JOIN categoria_mantenimiento_equipo cme ON me.id_categoria_mantenimiento_equipo = cme.id
            WHERE me.id_equipo = :idEquipo
            ORDER BY me.fecha_modificacion DESC
            """, nativeQuery = true)
    List<Object[]> findHistorialMantenimientosByEquipo(@Param("idEquipo") Long idEquipo);
}
