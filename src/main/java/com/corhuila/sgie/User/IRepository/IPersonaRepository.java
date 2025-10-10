package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.DTO.IPersonaUsuarioDTO;
import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPersonaRepository extends IBaseRepository<Persona, Long> {
    @Query(value = """

        SELECT
            pe.id AS idPersona,
            us.id AS idUsuario,
            pe.tipo_documento AS tipoDocumento,\s
            pe.numero_identificacion AS numeroIdentificacion,
            pe.nombres AS nombres,\s
            pe.apellidos AS apellidos, \s
            pe.telefono_movil AS telefonoMovil,
            us.email AS email,\s
            us.state AS estado,
            r.nombre AS rol
        FROM persona pe
        LEFT JOIN usuario us ON us.id_persona = pe.id
        LEFT JOIN rol r ON pe.id_rol = r.id
        WHERE (:numeroIdentificacion IS NULL OR :numeroIdentificacion = '' OR pe.numero_identificacion = :numeroIdentificacion)
        """, nativeQuery = true)
    List<IPersonaUsuarioDTO> findUsuariosPersonaPorIdentificacion(
            @Param("numeroIdentificacion") String numeroIdentificacion
    );
}
