package com.corhuila.sgie.Maintenance.IRepository;

import com.corhuila.sgie.Maintenance.DTO.IMantenimientoEquipoDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoEquipo;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMantenimientoEquipoRepository extends IBaseRepository <MantenimientoEquipo,Long> {
    @Query(value = """
        SELECT 
            tr.nombre AS tipoReserva, 
            re.nombre AS nombreReserva, 
            pe.nombres AS nombrePersona,
            pe.numero_identificacion AS numeroIdentificacionPersona,
            re.fecha_reserva AS fechaReserva,
            re.hora_inicio AS horaInicioReserva,
            re.hora_fin AS horaFinReserva, 
            eq.nombre AS nombreEquipo, 
            cme.nombre AS tipoMantenimiento
        FROM mantenimiento_equipo me
        INNER JOIN reserva re ON me.id_reserva = re.id
        INNER JOIN equipo eq ON me.id_equipo = eq.id
        INNER JOIN categoria_mantenimiento_equipo cme ON me.id_categoria_mantenimiento_equipo = cme.id
        INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
        INNER JOIN persona pe ON re.id_persona = pe.id
        WHERE (:numeroIdentificacionPersona IS NULL OR :numeroIdentificacionPersona = '' OR pe.numero_identificacion = :numeroIdentificacionPersona)
        """, nativeQuery = true)
    List<IMantenimientoEquipoDTO> findMantenimientosEquipoByNumeroIdentificacion(
            @Param("numeroIdentificacionPersona") String numeroIdentificacionPersona
    );
}
