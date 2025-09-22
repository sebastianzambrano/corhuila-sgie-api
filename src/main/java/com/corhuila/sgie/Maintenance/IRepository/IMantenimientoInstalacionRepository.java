package com.corhuila.sgie.Maintenance.IRepository;

import com.corhuila.sgie.Maintenance.DTO.IMantenimientoInstalacionDTO;
import com.corhuila.sgie.Maintenance.Entity.MantenimientoInstalacion;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMantenimientoInstalacionRepository extends IBaseRepository<MantenimientoInstalacion, Long> {
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
            cmi.nombre AS tipoMantenimiento,
            mi.state AS estadoMantenimiento,
            re.state AS estadoReserva
        FROM mantenimiento_instalacion mi
        INNER JOIN reserva re ON mi.id_reserva = re.id
        INNER JOIN instalacion ins ON mi.id_instalacion = ins.id
        INNER JOIN categoria_mantenimiento_instalacion cmi ON mi.id_categoria_mantenimiento_instalacion = cmi.id
        INNER JOIN tipo_reserva tr ON re.id_tipo_reserva = tr.id
        INNER JOIN persona pe ON re.id_persona = pe.id
        WHERE (:numeroIdentificacionPersona IS NULL OR :numeroIdentificacionPersona = '' OR pe.numero_identificacion = :numeroIdentificacionPersona)
        """, nativeQuery = true)
    List<IMantenimientoInstalacionDTO> findMantenimientosInstalacionByNumeroIdentificacion(
            @Param("numeroIdentificacionPersona") String numeroIdentificacionPersona
    );
}
