package com.corhuila.sgie.Booking.IRepository;

import com.corhuila.sgie.Booking.DTO.IReservaGeneralDTO;
import com.corhuila.sgie.Booking.DTO.ReservaGeneralReporteDTO;
import com.corhuila.sgie.Booking.Entity.Reserva;
import com.corhuila.sgie.common.IBaseRepository;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface IReservaRepository extends IBaseRepository<Reserva, Long> {

    @Query(value = "SELECT * FROM horas_disponibles_instalacion(:fecha, :idInstalacion)", nativeQuery = true)
    List<Object[]> findHorasDisponiblesInstalacion(@Param("fecha") LocalDate fecha,
                                                   @Param("idInstalacion") Integer idInstalacion,
                                                   @Param("idDetalle") Long idDetalle);

    @Query(value = "SELECT * FROM horas_disponibles_equipo(:fecha, :idEquipo, :idDetalle)", nativeQuery = true)
    List<Object[]> findHorasDisponiblesEquipo(@Param("fecha") LocalDate fecha,
                                              @Param("idEquipo") Integer idEquipo,
                                              @Param("idDetalle") Long idDetalle);

    @Query("""
                SELECT r 
                FROM Reserva r
                WHERE r.fechaReserva = :fecha
                  AND r.state = true
                  AND (
                      (r.horaInicio < :horaFin AND r.horaFin > :horaInicio)
                  )
                  AND (
                      ( :tipoReserva IN (1,3) AND r.tipoReserva.id IN (1,3) )
                      OR
                      ( :tipoReserva IN (2,4) AND r.tipoReserva.id IN (2,4) )
                  )
            """)
    List<Reserva> findReservasSolapadas(
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("tipoReserva") Long tipoReserva
    );

    @EntityGraph(attributePaths = {"persona", "persona.usuario"})
    Optional<Reserva> findWithPersonaAndUsuarioById(Long id);

    List<Reserva> findByFechaReservaAndStateTrue(LocalDate fecha);

    @Query(value = """
            SELECT
                re.id AS idReserva,
                dre.id AS idDetalleRerservaEquipo,
                CAST(NULL AS BIGINT) AS idDetalleRerservaInstalacion,
                CAST(NULL AS BIGINT) AS idMantenimientoEquipo,
                CAST(NULL AS BIGINT) AS idMantenimientoInstalacion,
                tr.id AS idTipoReserva,
                tr.nombre AS tipoReserva,
                re.nombre AS nombreReserva,
                re.descripcion AS descripcionReserva,
                re.fecha_reserva AS fechaReserva,
                re.hora_inicio AS horaInicioReserva,
                re.hora_fin AS horaFinReserva,
                pe.id AS idPersona,
                pe.nombres AS nombrePersona,
                pe.numero_identificacion AS numeroIdentificacion,
                ins.id AS idInstalacion,
                ins.nombre AS nombreInstalacion,
                eq.id AS idEquipo,
                te.nombre AS nombreEquipo,
                dre.programa_academico AS programaAcademico,
                dre.numero_estudiantes AS numeroEstudiantes,
                dre.id_instalacion_destino AS idInstalacionDestino,
                CAST(NULL AS TEXT) AS tipoMantenimiento,
                CAST(NULL AS TEXT) AS descripcionMantenimiento,
                CAST(NULL AS BIGINT) AS idCategoriaMantenimiento,
                CAST(NULL AS TEXT) AS estadoMantenimiento,
                CAST(re.state AS TEXT) AS estadoReserva,
                CAST(dre.state AS TEXT) AS estadoDetalle
            FROM detalle_reserva_equipo dre
            INNER JOIN reserva re ON dre.id_reserva = re.id
            INNER JOIN instalacion ins ON dre.id_instalacion_destino = ins.id
            INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            INNER JOIN persona pe ON re.id_persona = pe.id
            INNER JOIN equipo eq ON dre.id_equipo = eq.id
            INNER JOIN tipo_equipo te ON eq.id_tipo_equipo = te.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            
            UNION ALL
            
            SELECT
                re.id AS idReserva,
                CAST(NULL AS BIGINT) AS idDetalleRerservaEquipo,
                dri.id AS idDetalleRerservaInstalacion,
                CAST(NULL AS BIGINT) AS idMantenimientoEquipo,
                CAST(NULL AS BIGINT) AS idMantenimientoInstalacion,
                tr.id AS idTipoReserva,
                tr.nombre AS tipoReserva,
                re.nombre AS nombreReserva,
                re.descripcion AS descripcionReserva,
                re.fecha_reserva AS fechaReserva,
                re.hora_inicio AS horaInicioReserva,
                re.hora_fin AS horaFinReserva,
                pe.id AS idPersona,
                pe.nombres AS nombrePersona,
                pe.numero_identificacion AS numeroIdentificacion,
                ins.id AS idInstalacion,
                ins.nombre AS nombreInstalacion,
                CAST(NULL AS BIGINT) AS idEquipo,
                CAST(NULL AS TEXT) AS nombreEquipo,
                dri.programa_academico AS programaAcademico,
                dri.numero_estudiantes AS numeroEstudiantes,
                CAST(NULL AS BIGINT) AS idInstalacionDestino,
                CAST(NULL AS TEXT) AS tipoMantenimiento,
                CAST(NULL AS TEXT) AS descripcionMantenimiento,
                CAST(NULL AS BIGINT) AS idCategoriaMantenimiento,
                CAST(NULL AS TEXT) AS estadoMantenimiento,
                CAST(re.state AS TEXT) AS estadoReserva,
                CAST(dri.state AS TEXT) AS estadoDetalle
            FROM detalle_reserva_instalacion dri
            INNER JOIN reserva re ON dri.id_reserva = re.id
            INNER JOIN instalacion ins ON dri.id_instalacion = ins.id
            INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            INNER JOIN persona pe ON re.id_persona = pe.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            
            UNION ALL
            
            SELECT
                re.id AS idReserva,
                CAST(NULL AS BIGINT) AS idDetalleRerservaEquipo,
                CAST(NULL AS BIGINT) AS idDetalleRerservaInstalacion,
                me.id AS idMantenimientoEquipo,
                CAST(NULL AS BIGINT) AS idMantenimientoInstalacion,
                tr.id AS idTipoReserva,
                tr.nombre AS tipoReserva,
                re.nombre AS nombreReserva,
                re.descripcion AS descripcionReserva,
                re.fecha_reserva AS fechaReserva,
                re.hora_inicio AS horaInicioReserva,
                re.hora_fin AS horaFinReserva,
                pe.id AS idPersona,
                pe.nombres AS nombrePersona,
                pe.numero_identificacion AS numeroIdentificacion,
                CAST(NULL AS BIGINT) AS idInstalacion,
                CAST(NULL AS TEXT) AS nombreInstalacion,
                eq.id AS idEquipo,
                te.nombre AS nombreEquipo,
                CAST(NULL AS TEXT) AS programaAcademico,
                CAST(NULL AS INTEGER) AS numeroEstudiantes,
                CAST(NULL AS BIGINT) AS idInstalacionDestino,
                cme.nombre AS tipoMantenimiento,
                me.descripcion AS descripcionMantenimiento,
                me.id_categoria_mantenimiento_equipo AS idCategoriaMantenimiento,
                CAST(me.state AS TEXT) AS estadoMantenimiento,
                CAST(re.state AS TEXT) AS estadoReserva,
                CAST(NULL AS TEXT) AS estadoDetalle
            FROM mantenimiento_equipo me
            INNER JOIN reserva re ON me.id_reserva = re.id
            INNER JOIN equipo eq ON me.id_equipo = eq.id
            INNER JOIN categoria_mantenimiento_equipo cme ON me.id_categoria_mantenimiento_equipo = cme.id
            INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            INNER JOIN persona pe ON re.id_persona = pe.id
            INNER JOIN tipo_equipo te ON eq.id_tipo_equipo = te.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            
            UNION ALL
            
            SELECT
                re.id AS idReserva,
                CAST(NULL AS BIGINT) AS idDetalleRerservaEquipo,
                CAST(NULL AS BIGINT) AS idDetalleRerservaInstalacion,
                CAST(NULL AS BIGINT) AS idMantenimientoEquipo,
                mi.id AS idMantenimientoInstalacion,
                tr.id AS idTipoReserva,
                tr.nombre AS tipoReserva,
                re.nombre AS nombreReserva,
                re.descripcion AS descripcionReserva,
                re.fecha_reserva AS fechaReserva,
                re.hora_inicio AS horaInicioReserva,
                re.hora_fin AS horaFinReserva,
                pe.id AS idPersona,
                pe.nombres AS nombrePersona,
                pe.numero_identificacion AS numeroIdentificacion,
                ins.id AS idInstalacion,
                ins.nombre AS nombreInstalacion,
                CAST(NULL AS BIGINT) AS idEquipo,
                CAST(NULL AS TEXT) AS nombreEquipo,
                CAST(NULL AS TEXT) AS programaAcademico,
                CAST(NULL AS INTEGER) AS numeroEstudiantes,
                CAST(NULL AS BIGINT) AS idInstalacionDestino,
                cmi.nombre AS tipoMantenimiento,
                mi.descripcion AS descripcionMantenimiento,
                mi.id_categoria_mantenimiento_instalacion AS idCategoriaMantenimiento,
                CAST(mi.state AS TEXT) AS estadoMantenimiento,
                CAST(re.state AS TEXT) AS estadoReserva,
                CAST(NULL AS TEXT) AS estadoDetalle
            FROM mantenimiento_instalacion mi
            INNER JOIN reserva re ON mi.id_reserva = re.id
            INNER JOIN instalacion ins ON mi.id_instalacion = ins.id
            INNER JOIN categoria_mantenimiento_instalacion cmi ON mi.id_categoria_mantenimiento_instalacion = cmi.id
            INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            INNER JOIN persona pe ON re.id_persona = pe.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            """, nativeQuery = true)
    List<IReservaGeneralDTO> findReservasYMantenimientosByNumeroIdentificacion(
            @Param("numeroIdentificacion") String numeroIdentificacion
    );

    @Query(value = """
            SELECT
              re.id                                  AS "idReserva",
              dre.id                                 AS "idDetalleRerservaEquipo",
              CAST(NULL AS BIGINT)                   AS "idDetalleRerservaInstalacion",
              CAST(NULL AS BIGINT)                   AS "idMantenimientoEquipo",
              CAST(NULL AS BIGINT)                   AS "idMantenimientoInstalacion",
              tr.id                                  AS "idTipoReserva",
              tr.nombre                              AS "tipoReserva",
              re.nombre                              AS "nombreReserva",
              re.descripcion                         AS "descripcionReserva",
              re.fecha_reserva                       AS "fechaReserva",
              re.hora_inicio                         AS "horaInicioReserva",
              re.hora_fin                            AS "horaFinReserva",
              pe.id                                  AS "idPersona",
              pe.nombres                             AS "nombrePersona",
              pe.numero_identificacion               AS "numeroIdentificacion",
              ins.id                                 AS "idInstalacion",
              ins.nombre                             AS "nombreInstalacion",
              eq.id                                  AS "idEquipo",
              te.nombre                              AS "nombreEquipo",
              dre.programa_academico                 AS "programaAcademico",
              dre.numero_estudiantes                 AS "numeroEstudiantes",
              dre.id_instalacion_destino             AS "idInstalacionDestino",
              CAST(NULL AS TEXT)                     AS "tipoMantenimiento",
              CAST(NULL AS TEXT)                     AS "descripcionMantenimiento",
              CAST(NULL AS BIGINT)                   AS "idCategoriaMantenimiento",
              CAST(NULL AS TEXT)                     AS "estadoMantenimiento",
              CAST(re.state AS TEXT)                 AS "estadoReserva",
              CAST(dre.state AS TEXT)                AS "estadoDetalle"
            FROM detalle_reserva_equipo dre
            JOIN reserva re ON dre.id_reserva = re.id
            JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            JOIN persona pe ON re.id_persona = pe.id
            JOIN equipo eq ON dre.id_equipo = eq.id
            JOIN tipo_equipo te ON eq.id_tipo_equipo = te.id
            JOIN instalacion ins ON dre.id_instalacion_destino = ins.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            
            UNION ALL
            
            SELECT
              re.id                                  AS "idReserva",
              CAST(NULL AS BIGINT)                   AS "idDetalleRerservaEquipo",
              dri.id                                 AS "idDetalleRerservaInstalacion",
              CAST(NULL AS BIGINT)                   AS "idMantenimientoEquipo",
              CAST(NULL AS BIGINT)                   AS "idMantenimientoInstalacion",
              tr.id                                  AS "idTipoReserva",
              tr.nombre                              AS "tipoReserva",
              re.nombre                              AS "nombreReserva",
              re.descripcion                         AS "descripcionReserva",
              re.fecha_reserva                       AS "fechaReserva",
              re.hora_inicio                         AS "horaInicioReserva",
              re.hora_fin                            AS "horaFinReserva",
              pe.id                                  AS "idPersona",
              pe.nombres                             AS "nombrePersona",
              pe.numero_identificacion               AS "numeroIdentificacion",
              ins.id                                 AS "idInstalacion",
              ins.nombre                             AS "nombreInstalacion",
              CAST(NULL AS BIGINT)                   AS "idEquipo",
              CAST(NULL AS TEXT)                     AS "nombreEquipo",
              dri.programa_academico                 AS "programaAcademico",
              dri.numero_estudiantes                 AS "numeroEstudiantes",
              CAST(NULL AS BIGINT)                   AS "idInstalacionDestino",
              CAST(NULL AS TEXT)                     AS "tipoMantenimiento",
              CAST(NULL AS TEXT)                     AS "descripcionMantenimiento",
              CAST(NULL AS BIGINT)                   AS "idCategoriaMantenimiento",
              CAST(NULL AS TEXT)                     AS "estadoMantenimiento",
              CAST(re.state AS TEXT)                 AS "estadoReserva",
              CAST(dri.state AS TEXT)                AS "estadoDetalle"
            FROM detalle_reserva_instalacion dri
            JOIN reserva re ON dri.id_reserva = re.id
            JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            JOIN persona pe ON re.id_persona = pe.id
            JOIN instalacion ins ON dri.id_instalacion = ins.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            
            UNION ALL
            
            SELECT
              re.id                                  AS "idReserva",
              CAST(NULL AS BIGINT)                   AS "idDetalleRerservaEquipo",
              CAST(NULL AS BIGINT)                   AS "idDetalleRerservaInstalacion",
              me.id                                  AS "idMantenimientoEquipo",
              CAST(NULL AS BIGINT)                   AS "idMantenimientoInstalacion",
              tr.id                                  AS "idTipoReserva",
              tr.nombre                              AS "tipoReserva",
              re.nombre                              AS "nombreReserva",
              re.descripcion                         AS "descripcionReserva",
              re.fecha_reserva                       AS "fechaReserva",
              re.hora_inicio                         AS "horaInicioReserva",
              re.hora_fin                            AS "horaFinReserva",
              pe.id                                  AS "idPersona",
              pe.nombres                             AS "nombrePersona",
              pe.numero_identificacion               AS "numeroIdentificacion",
              CAST(NULL AS BIGINT)                   AS "idInstalacion",
              CAST(NULL AS TEXT)                     AS "nombreInstalacion",
              eq.id                                  AS "idEquipo",
              te.nombre                              AS "nombreEquipo",
              CAST(NULL AS TEXT)                     AS "programaAcademico",
              CAST(NULL AS INTEGER)                  AS "numeroEstudiantes",
              CAST(NULL AS BIGINT)                   AS "idInstalacionDestino",
              cme.nombre                             AS "tipoMantenimiento",
              me.descripcion                         AS "descripcionMantenimiento",
              me.id_categoria_mantenimiento_equipo   AS "idCategoriaMantenimiento",
              CAST(me.state AS TEXT)                 AS "estadoMantenimiento",
              CAST(re.state AS TEXT)                 AS "estadoReserva",
              CAST(NULL AS TEXT)                     AS "estadoDetalle"
            FROM mantenimiento_equipo me
            JOIN reserva re ON me.id_reserva = re.id
            JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            JOIN persona pe ON re.id_persona = pe.id
            JOIN equipo eq ON me.id_equipo = eq.id
            JOIN tipo_equipo te ON eq.id_tipo_equipo = te.id
            JOIN categoria_mantenimiento_equipo cme ON me.id_categoria_mantenimiento_equipo = cme.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            
            UNION ALL
            
            SELECT
              re.id                                  AS "idReserva",
              CAST(NULL AS BIGINT)                   AS "idDetalleRerservaEquipo",
              CAST(NULL AS BIGINT)                   AS "idDetalleRerservaInstalacion",
              CAST(NULL AS BIGINT)                   AS "idMantenimientoEquipo",
              mi.id                                  AS "idMantenimientoInstalacion",
              tr.id                                  AS "idTipoReserva",
              tr.nombre                              AS "tipoReserva",
              re.nombre                              AS "nombreReserva",
              re.descripcion                         AS "descripcionReserva",
              re.fecha_reserva                       AS "fechaReserva",
              re.hora_inicio                         AS "horaInicioReserva",
              re.hora_fin                            AS "horaFinReserva",
              pe.id                                  AS "idPersona",
              pe.nombres                             AS "nombrePersona",
              pe.numero_identificacion               AS "numeroIdentificacion",
              ins.id                                 AS "idInstalacion",
              ins.nombre                             AS "nombreInstalacion",
              CAST(NULL AS BIGINT)                   AS "idEquipo",
              CAST(NULL AS TEXT)                     AS "nombreEquipo",
              CAST(NULL AS TEXT)                     AS "programaAcademico",
              CAST(NULL AS INTEGER)                  AS "numeroEstudiantes",
              CAST(NULL AS BIGINT)                   AS "idInstalacionDestino",
              cmi.nombre                             AS "tipoMantenimiento",
              mi.descripcion                         AS "descripcionMantenimiento",
              mi.id_categoria_mantenimiento_instalacion AS "idCategoriaMantenimiento",
              CAST(mi.state AS TEXT)                 AS "estadoMantenimiento",
              CAST(re.state AS TEXT)                 AS "estadoReserva",
              CAST(NULL AS TEXT)                     AS "estadoDetalle"
            FROM mantenimiento_instalacion mi
            JOIN reserva re ON mi.id_reserva = re.id
            JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
            JOIN persona pe ON re.id_persona = pe.id
            JOIN instalacion ins ON mi.id_instalacion = ins.id
            JOIN categoria_mantenimiento_instalacion cmi ON mi.id_categoria_mantenimiento_instalacion = cmi.id
            WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
            """,
            nativeQuery = true)
    @QueryHints(@QueryHint(name = org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE, value = "1000"))
    Stream<ReservaGeneralReporteDTO> findReservasYMantenimientosByNumeroIdentificacionReport(
            @Param("numeroIdentificacion") String numeroIdentificacion
    );

}
